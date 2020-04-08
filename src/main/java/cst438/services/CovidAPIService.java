package cst438.services;

import java.time.LocalDate;
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
import cst438.domain.CovidNationalRepository;
import cst438.domain.CovidRepository;
import cst438.domain.JsonCovidCurrentHelper;
import cst438.domain.JsonCovidHistoryHelper;
import cst438.domain.JsonNationalStatsHelper;

/* This service is responsible for pulling the data from the external API
 * It will be used by 'CovidService' to pull current data.*/

@Service
public class CovidAPIService {
   @Autowired
   private CovidRepository covidRepository;
   @Autowired
   private CovidNationalRepository covidNationalRepository;
   
   private static final Logger log = 
         LoggerFactory.getLogger(CovidAPIService.class);
   private RestTemplate restTemplate;
   private String covidDataStatesUrl;
   private String covidDataNationUrl;
   
   public CovidAPIService(
         @Value("${covidDataStates.url}") final String covidStatesUrl,
         @Value("${covidDataNation.url}") final String covidNationUrl,
         CovidRepository covidRepository) {
      this.covidRepository = covidRepository;
      this.restTemplate = new RestTemplate();
      this.covidDataStatesUrl = covidStatesUrl;
      this.covidDataNationUrl = covidNationUrl;
   }
       
   // This method will be used frequently to pull current COVID data
//   public void pullCurrentCovidData() {
//      ResponseEntity<List<JsonCovidCurrentHelper>> response = 
//            restTemplate.exchange(
//                  this.covidDataUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonCovidCurrentHelper>>() {});
//      List<JsonCovidCurrentHelper> covidDataJson = response.getBody();
//      log.info("Status code from weather server:" + 
//                  response.getStatusCodeValue());
//      
//      // parse the json list into new entries and save them into the db
//      for(JsonCovidCurrentHelper stateData : covidDataJson) {
//         covidRepository.insertDataPoint(
//               stateData.getState(),
//               stateData.getPositive(),
//               stateData.getNegative(),
//               stateData.getHospitalizedCurrently(),
//               stateData.getHospitalizedCumulative(),
//               stateData.getInIcuCurrently(),
//               stateData.getInIcuCumulative(),
//               stateData.getOnVentilatorCurrently(),
//               stateData.getOnVentilatorCumulative(),
//               stateData.getRecovered(),
//               stateData.getDeath()
//               );
//      }
//   }
   
   public void populateStates() {
      // pull data from external API
      ResponseEntity<List<JsonCovidHistoryHelper>> response = 
            restTemplate.exchange(
                  this.covidDataStatesUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonCovidHistoryHelper>>()
                  {});
      List<JsonCovidHistoryHelper> covidDataJson = response.getBody();
      log.info("Status code from weather server:" + 
                  response.getStatusCodeValue());
      
      // parse the json list into new entries and save them into the db only 
      // if it's empty
      if (covidRepository.findByID(1) == null) {
         for(JsonCovidHistoryHelper stateData : covidDataJson) {
            covidRepository.insertHistoricalDataPoint(
                  stateData.getDate(),
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
   
   // currently used to add some info to the national statistics table
   // can use this to add data points on a chron job?
   public void insertNationalStats(long date) {
      // pull data from external API
      ResponseEntity<List<JsonNationalStatsHelper>> response = 
            restTemplate.exchange(
                  this.covidDataNationUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonNationalStatsHelper>>() {});
      JsonNationalStatsHelper covidDataJson = response.getBody().get(0);
      log.info("Status code from weather server:" + 
                  response.getStatusCodeValue());
      
      // parse the json list into new entries and save them into the db
      // only if the date entry doesn't exist in the db
      if (covidNationalRepository.findByDate(
            CovidData.formatDate(LocalDate.now())) == null) {
         covidNationalRepository.insertHistoricalDataPoint(
               date,
               covidDataJson.getPositive(),
               covidDataJson.getNegative(),
               covidDataJson.getHospitalizedCurrently(),
               covidDataJson.getHospitalizedCumulative(),
               covidDataJson.getInIcuCurrently(),
               covidDataJson.getInIcuCumulative(),
               covidDataJson.getOnVentilatorCurrently(),
               covidDataJson.getOnVentilatorCumulative(),
               covidDataJson.getRecovered(),
               covidDataJson.getDeath()
               );
      }
   }
}