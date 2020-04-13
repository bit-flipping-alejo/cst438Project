package cst438.startUp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cst438.domain.Helper.JsonCovidHistoryHelper;
import cst438.domain.Helper.JsonNationalStatsHelper;
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
         DateTimeFormatter.ofPattern("yyyyMMdd");
   
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

      // parse the json list into new entries and save them into the db
      // only if the date entry doesn't exist in the db
      if (covidNationalRepository.findByID(1) == null) {
         log.info("Status code: " + response.getStatusCodeValue() + " Updated"
               + " national statistics");
         Collections.reverse(covidDataJson);
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
      }
   }
}
