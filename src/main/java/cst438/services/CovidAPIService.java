package cst438.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cst438.domain.CovidData;
import cst438.domain.CovidNationalData;
import cst438.domain.CovidNationalRepository;
import cst438.domain.CovidRepository;
import cst438.domain.JsonCovidCurrentHelper;
import cst438.domain.JsonCovidHistoryHelper;
import cst438.domain.JsonNationalStatsHelper;

/* This service is responsible for pulling the data from the external API
 * It will be used by the controller to pull current data.*/

@Service
public class CovidAPIService {
   @Autowired
   private CovidRepository covidRepository;
   @Autowired
   private CovidNationalRepository covidNationalRepository;
   
   private static final Logger log = 
         LoggerFactory.getLogger(CovidAPIService.class);
   private RestTemplate restTemplate;
   private String currentStatesDataUrl;
   private String currentNationalDataUrl;
   private String historicalStatesDataUrl;
   private String historicalNationalDataUrl;
   private DateTimeFormatter dateFormat = 
         DateTimeFormatter.ofPattern("yyyyMMdd");
   
   public CovidAPIService(
         @Value("${currentCovidStatesData.url}") final String currentStatesUrl,
         @Value("${currentCovidNationalData.url}") final String currNationUrl,
         @Value("${historicalCovidStatesData.url}") final String histStatesUrl,
         @Value("${historicalCovidNationalData.url}") final String histNationalUrl,
         CovidRepository covidRepository) {
      this.covidRepository = covidRepository;
      this.restTemplate = new RestTemplate();
      this.currentStatesDataUrl = currentStatesUrl;
      this.currentNationalDataUrl = currNationUrl;
      this.historicalStatesDataUrl = histStatesUrl;
      this.historicalNationalDataUrl = histNationalUrl;
   }
       
   // This method will be used frequently to pull current state COVID data
   // Will immediately construct the List<CovidData>, ready to pass to the
   // controller. This method does not save to the database.
   public List<CovidData> pullCurrentStateData() {
      ResponseEntity<List<JsonCovidCurrentHelper>> response = 
            restTemplate.exchange(
                  this.currentStatesDataUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonCovidCurrentHelper>>() {});
      List<JsonCovidCurrentHelper> covidDataJson = response.getBody();
      log.info("Status code from weather server:" + 
                  response.getStatusCodeValue());
      
      // parse the json list into new entries
      List<CovidData> currentStateData = new ArrayList<CovidData>();
      for(JsonCovidCurrentHelper stateData : covidDataJson) {
         CovidData currentData =  new CovidData();
         currentData.setState(stateData.getState());
         currentData.setTestedPositive(stateData.getPositive());
         currentData.setTestedNegative(stateData.getNegative());
         currentData.setCurrentlyHospitalized(
               stateData.getHospitalizedCurrently());
         currentData.setTotalHospitalized(
               stateData.getHospitalizedCumulative());
         currentData.setCurrentICUCount(stateData.getInIcuCurrently());
         currentData.setTotalICUCount(stateData.getInIcuCumulative());
         currentData.setCurrentlyOnVentilator(
               stateData.getOnVentilatorCurrently());
         currentData.setTotalVentilated(
               stateData.getOnVentilatorCumulative());
         currentData.setRecovered(stateData.getRecovered());
         currentData.setDeaths(stateData.getDeath());
         
         currentStateData.add(currentData);
      }
      
      return currentStateData;
   }
   
   // This method will be used frequently to pull current national COVID data
   // Will immediately construct CovidNationalData, ready to pass to the
   // controller. This method does not save to the database.
   public CovidNationalData pullCurrentNationalStats() {
      // pull data from external API
      ResponseEntity<List<JsonNationalStatsHelper>> response = 
            restTemplate.exchange(
                  this.currentNationalDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonNationalStatsHelper>>
                  () {});
      JsonNationalStatsHelper covidDataJson = response.getBody().get(0);

      CovidNationalData currentNationalData = new CovidNationalData();
            
      currentNationalData.setTestedPositive(covidDataJson.getPositive());
      currentNationalData.setDeaths(covidDataJson.getDeath());
      
      return currentNationalData;
   }

   // This method populates the database with the external historical api
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