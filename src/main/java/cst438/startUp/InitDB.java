package cst438.startUp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cst438.domain.Helper.JsonCovidHistoryHelper;
import cst438.domain.Helper.JsonNationalStatsHelper;
import cst438.domain.Model.CovidData;
import cst438.domain.Model.CovidNationalData;
import cst438.domain.Repository.CovidNationalRepository;
import cst438.domain.Repository.CovidRepository;
import cst438.services.CovidAPIService;

/** Description: The purpose of this class is to execute the methods responsible 
 * for calling The COVID Tracking Project history APIs. They return JSON 
 * containing daily entries for all states and national statistics. In addition,
 * but not relevant to this class, is a third table that is created and populated
 * on server start containing the state name and postal code.
 * 
 * */

@Component
public class InitDB {
   @Autowired
   private CovidRepository covidRepository;
   @Autowired
   private CovidNationalRepository covidNationalRepository;
   
   private static final Logger log = 
         LoggerFactory.getLogger(CovidAPIService.class);
   private RestTemplate restTemplate;
   private String historicalStatesDataUrl;
   private String historicalNationalDataUrl;
   private DateTimeFormatter dateFormat = 
         DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.US);
   
   public InitDB(
         @Value("${currentCovidStatesData.url}") final String currentStatesUrl,
         @Value("${currentCovidNationalData.url}") final String currNationUrl,
         @Value("${historicalCovidStatesData.url}") final String histStatesUrl,
         @Value("${historicalCovidNationalData.url}") final String histNationalUrl,
         CovidRepository covidRepository) {
      this.covidRepository = covidRepository;
      this.restTemplate = new RestTemplate();
      this.historicalStatesDataUrl = histStatesUrl;
      this.historicalNationalDataUrl = histNationalUrl;
   }
   
   // This method populates the database with the external historical api
   @PostConstruct
   public void populateStateStats() {
      // pull data from external API
      ResponseEntity<List<JsonCovidHistoryHelper>> response = 
            restTemplate.exchange(
                  this.historicalStatesDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonCovidHistoryHelper>>()
                  {});
      List<JsonCovidHistoryHelper> covidDataJson = response.getBody();

      // parse the json list into new entries and save them into the db only 
      // if it's empty
      // if we have data in the table, update the stats to the current date.

      if (covidRepository.findByID(1) == null) {
         log.info("Status code: " + response.getStatusCodeValue() + " Updated"
               + " state statistics");
         Collections.reverse(covidDataJson);
         for(JsonCovidHistoryHelper stateData : covidDataJson) {
            covidRepository.insertHistoricalDataPoint(
                  LocalDate.parse(stateData.getDate(), dateFormat),
                  stateData.getState(),
                  stateData.getPositive(),
                  stateData.getNegative(),
                  stateData.getHospitalizedCurrently(),
                  stateData.getHospitalizedCumulative(),
                  stateData.getInIcuCurrently(),
                  stateData.getInIcuCumulative(),
                  stateData.getOnVentilatorCurrently(),
                  stateData.getOnVentilatorCumulative(),
                  stateData.getRecovered(),
                  stateData.getDeath()
                  );
         }
      
      } else {
         System.out.println("Updating state historical data...");
         updateStateStats(covidDataJson);
         System.out.println("Updated state entries.");
      }
   }
   
   // This method populates the database with the external historical api
   @PostConstruct
   public void populateNationalStats() {
      // pull data from external API
      ResponseEntity<List<JsonNationalStatsHelper>> response = 
            restTemplate.exchange(
                  this.historicalNationalDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonNationalStatsHelper>>() {});
      List<JsonNationalStatsHelper> covidDataJson = response.getBody();
      Collections.reverse(covidDataJson);
      
      // parse the json list into new entries and save them into the db
      // only if the date entry doesn't exist in the db
      if (covidNationalRepository.findByID(1) == null) {
         log.info("Status code: " + response.getStatusCodeValue() + " Updated"
               + " national statistics");
         
         for(JsonNationalStatsHelper nationalData : covidDataJson) {
            System.out.print(nationalData.getDate());
            covidNationalRepository.insertHistoricalDataPoint(
                  LocalDate.parse(nationalData.getDate(), dateFormat),
                  nationalData.getStates(),
                  nationalData.getPositive(),
                  nationalData.getNegative(),
                  nationalData.getHospitalizedCurrently(),
                  nationalData.getHospitalizedCumulative(),
                  nationalData.getInIcuCurrently(),
                  nationalData.getInIcuCumulative(),
                  nationalData.getOnVentilatorCurrently(),
                  nationalData.getOnVentilatorCumulative(),
                  nationalData.getRecovered(),
                  nationalData.getDeath()
                  );
         }
         
      
      } else {
         System.out.println("Updating national historical data...");
         updateNationalStats(covidDataJson);
         System.out.println("Updated national data.");
      }
   }
   
   // This method and the partner method for national history stats are both
   // called by the populate methods above to update the tables, otherwise they
   // run every night around 8pm PST to update the table automatically.
   
   @Scheduled(cron="0 0 20 * * *")
   public void updateStateStats() {
      System.out.println("update DB");
      
      // grab the current date
      LocalDate currentDate = LocalDate.now();
      
      // query the external API for the historical data
      ResponseEntity<List<JsonCovidHistoryHelper>> response = 
            restTemplate.exchange(
                  this.historicalStatesDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonCovidHistoryHelper>>()
                  {});
      List<JsonCovidHistoryHelper> covidDataJson = response.getBody();
      Collections.reverse(covidDataJson);
      
      // query db with that date, see if any entries have the date
      List<CovidData> results = covidRepository.findByState("CA");
      LocalDate recentDateInRepo = results.get(0).getDate();
      
      // if we have entries, update up to the current date
      if (recentDateInRepo.compareTo(currentDate) < 0) {
         for (JsonCovidHistoryHelper entry : covidDataJson) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), dateFormat);
            
            // compare the json entry date to now, if it's more recent, enter
            if (entryDate.compareTo(currentDate) > 0) {
               // add that point into the database
               covidRepository.insertHistoricalDataPoint(
                     entryDate,
                     entry.getState(),
                     entry.getPositive(),
                     entry.getNegative(),
                     entry.getHospitalizedCurrently(),
                     entry.getHospitalizedCumulative(),
                     entry.getInIcuCurrently(),
                     entry.getInIcuCumulative(),
                     entry.getOnVentilatorCurrently(),
                     entry.getOnVentilatorCumulative(),
                     entry.getRecovered(),
                     entry.getDeath()
                     );
            }
         }
      } else {
         return;
      }
   }
   
   public void updateStateStats(List<JsonCovidHistoryHelper> covidDataJson) {
      // grab the current date
      LocalDate currentDate = LocalDate.now();
      
      // query db to a specific date. looking for the recent date in the table
      List<CovidData> results = covidRepository.findByStateDesc("CA");
      LocalDate recentDateInRepo = results.get(0).getDate();

      System.out.println(currentDate);
      results.get(0).printToConsole();
      // if we have entries, update up to the current date
      if (recentDateInRepo.compareTo(currentDate) < 0) {
         for (JsonCovidHistoryHelper entry : covidDataJson) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), dateFormat);
            
            // compare the json entry date to now, if it's more recent, enter
            if (entryDate.compareTo(currentDate) > 0) {
               // add that point into the database
               covidRepository.insertHistoricalDataPoint(
                     entryDate,
                     entry.getState(),
                     entry.getPositive(),
                     entry.getNegative(),
                     entry.getHospitalizedCurrently(),
                     entry.getHospitalizedCumulative(),
                     entry.getInIcuCurrently(),
                     entry.getInIcuCumulative(),
                     entry.getOnVentilatorCurrently(),
                     entry.getOnVentilatorCumulative(),
                     entry.getRecovered(),
                     entry.getDeath()
                     );
            }
         }
      } else {
         return;
      }
   }
   
   @Scheduled(cron="0 0 20 * * *")
   public void updateNationalStats() {
      System.out.println("update DB");
      
      // grab the current date
      LocalDate currentDate = LocalDate.now();
      
      // query the external API for the historical data
      ResponseEntity<List<JsonNationalStatsHelper>> response = 
            restTemplate.exchange(
                  this.historicalNationalDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonNationalStatsHelper>>() {});
      List<JsonNationalStatsHelper> covidDataJson = response.getBody();
      Collections.reverse(covidDataJson);
      
      // query db with that date, see if any entries have the date
      CovidNationalData results = covidNationalRepository.findByRecentDate();
      LocalDate recentDateInRepo = results.getDate();
      
      // if we have entries, update up to the current date
      if (recentDateInRepo.compareTo(currentDate) < 0) {
         for (JsonNationalStatsHelper entry : covidDataJson) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), dateFormat);
            
            // compare the json entry date to now, if it's more recent, enter
            if (entryDate.compareTo(recentDateInRepo) > 0) {
               // add that point into the database
               covidNationalRepository.insertHistoricalDataPoint(
                     entryDate,
                     entry.getStates(),
                     entry.getPositive(),
                     entry.getNegative(),
                     entry.getHospitalizedCurrently(),
                     entry.getHospitalizedCumulative(),
                     entry.getInIcuCurrently(),
                     entry.getInIcuCumulative(),
                     entry.getOnVentilatorCurrently(),
                     entry.getOnVentilatorCumulative(),
                     entry.getRecovered(),
                     entry.getDeath()
                     );
            }
         }
      } else {
         return;
      }
   }
   
   public void updateNationalStats(List<JsonNationalStatsHelper> covidDataJson) {
      // grab the current date
      LocalDate currentDate = LocalDate.now();
      
      // query db to a specific date. looking for the recent date in the table
      CovidNationalData results = covidNationalRepository.findByRecentDate();
      LocalDate recentDateInRepo = results.getDate();

      // if we have entries, update up to the current date
      if (recentDateInRepo.compareTo(currentDate) < 0) {
         for (JsonNationalStatsHelper entry : covidDataJson) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), dateFormat);
            
            // compare the json entry date to now, if it's more recent, enter
            if (entryDate.compareTo(recentDateInRepo) > 0) {
               // add that point into the database
               covidNationalRepository.insertHistoricalDataPoint(
                     entryDate,
                     entry.getStates(),
                     entry.getPositive(),
                     entry.getNegative(),
                     entry.getHospitalizedCurrently(),
                     entry.getHospitalizedCumulative(),
                     entry.getInIcuCurrently(),
                     entry.getInIcuCumulative(),
                     entry.getOnVentilatorCurrently(),
                     entry.getOnVentilatorCumulative(),
                     entry.getRecovered(),
                     entry.getDeath()
                     );
            }
         }
      } else {
         return;
      }
   }
}