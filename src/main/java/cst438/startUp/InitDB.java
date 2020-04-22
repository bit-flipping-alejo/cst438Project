package cst438.startUp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
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
import cst438.domain.Model.CovidStateData;
import cst438.domain.Model.CovidNationalData;
import cst438.domain.Repository.CovidNationalRepository;
import cst438.domain.Repository.CovidRepository;

/** Description: The purpose of this class is to execute the methods responsible 
 * for calling The COVID Tracking Project history APIs. They return JSON 
 * containing daily entries for all states and national statistics. In addition,
 * but not relevant to this java class, is a third table that is created and populated
 * on server start containing the state name and postal code.
 * 
 * */

@Component
public class InitDB {
   @Autowired
   private CovidRepository covidRepository;
   @Autowired
   private CovidNationalRepository covidNationalRepository;
   
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
   
   /** Description: 
    * This method and its sibling method are both responsible for populating and updating
    * the data contained in the repositories. Both of these methods are marked as 
    * PostContrusct, meaning the are executed by Spring after the server is initialized.
    * Each method is responsible for filling its respective repository. In summary, the 
    * method will either completely fill the tables if they are empty, or only fill the missing 
    * data points (by date). 
    * */
   @PostConstruct
   public void populateStateStats() {
      // pull data from external API
      ResponseEntity<List<JsonCovidHistoryHelper>> response = 
            restTemplate.exchange(
                  this.historicalStatesDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonCovidHistoryHelper>>()
                  {});
      List<JsonCovidHistoryHelper> covidDataJson = response.getBody();

      // we reverse because the data from the API is in ascending order,
      // we want in the the database as the oldest date is the first row.
      Collections.reverse(covidDataJson);
      
      // parse the json list into new entries and save them into the db only 
      // if it's empty
      // if we have data in the table, update the stats to the current date.
      if (covidRepository.findByID(1) == null) {
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
      }
   }
   
   /** Description: 
    * This method and its sibling method are both responsible for populating and updating
    * the data contained in the repositories. Both of these methods are marked as 
    * PostContrusct, meaning the are executed by Spring after the server is initialized.
    * Each method is responsible for filling its respective repository. In summary, the 
    * method will either completely fill the tables if they are empty, or only fill the missing 
    * data points (by date). 
    * */
   
   @PostConstruct
   public void populateNationalStats() {
      // pull data from external API
      ResponseEntity<List<JsonNationalStatsHelper>> response = 
            restTemplate.exchange(
                  this.historicalNationalDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonNationalStatsHelper>>() {});
      List<JsonNationalStatsHelper> covidDataJson = response.getBody();
      
      // we reverse because the data from the API is in ascending order,
      // we want in the the database as the oldest date is the first row.
      Collections.reverse(covidDataJson);
      
      // parse the json list into new entries and save them into the db
      // only if the date entry doesn't exist in the db
      if (covidNationalRepository.findByID(1) == null) {
         for(JsonNationalStatsHelper nationalData : covidDataJson) {
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
      }
   }
   
   /** Description:
    * The following two methods are actually overloaded methods of the cron jobs
    * at the bottom of this script. They are utilized by the above two methods
    * respectively. 
    * */
   public void updateStateStats(List<JsonCovidHistoryHelper> covidDataJson) {
      // grab the current date
      LocalDate currentDate = LocalDate.now();
      
      // query db to a specific date. looking for the recent date in the table
      List<CovidStateData> results = covidRepository.findByStateDesc("CA");
      LocalDate recentDateInRepo = results.get(0).getDate();

      // if we have entries, update up to the current date
      if (recentDateInRepo.compareTo(currentDate) < 0) {
         for (JsonCovidHistoryHelper entry : covidDataJson) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), dateFormat);
            
            // compare the json entry date to now, if it's more recent, enter
            if (entryDate.compareTo(recentDateInRepo) > 0) {
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
         
         System.out.println("Updated state entries.");
         return;
      } else {
         System.out.println("State stats current.");
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
         
         System.out.println("Updated national data.");
         return;
      } else {
         System.out.println("National stats current.");
         return;
      }
   }
   
   /** Description:
    * The following two methods are scheduled jobs. They are executed at 8PM server time
    * each day. They are responsible for querying the external API, and similar to the
    * populate methods, update the tables so they are made current. 
    * */
   
   @Scheduled(cron="0 0 20 * * *")
   public void updateStateStats() {
      System.out.println("updating state DB");
      
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
      List<CovidStateData> results = covidRepository.findByState("CA");
      LocalDate recentDateInRepo = results.get(0).getDate();
      
      // if we have entries, update up to the current date
      if (recentDateInRepo.compareTo(currentDate) < 0) {
         for (JsonCovidHistoryHelper entry : covidDataJson) {
            LocalDate entryDate = LocalDate.parse(entry.getDate(), dateFormat);
            
            // compare the json entry date to now, if it's more recent, enter
            if (entryDate.compareTo(recentDateInRepo) > 0) {
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
         
         System.out.println("Updated state entries.");
         return;
      } else {
         System.out.println("State stats current.");
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
         
         System.out.println("Updated national data.");
         return;
      } else {
         System.out.println("National stats current.");
         return;
      }
   }
}