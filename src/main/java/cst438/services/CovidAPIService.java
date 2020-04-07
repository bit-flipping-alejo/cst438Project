package cst438.services;

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

import cst438.domain.CovidRepository;
import cst438.domain.JsonCovidCurrentHelper;
import cst438.domain.JsonCovidHistoryHelper;

/* This service is responsible for pulling the data from the external API
 * It will be used by 'CovidService' to pull current data.*/

@Service
public class CovidAPIService {
   @Autowired
   private CovidRepository covidRepository;
   
   public CovidAPIService() {
      
   }
   
   public CovidAPIService(CovidRepository covidRepository) {
      this.covidRepository = covidRepository;
   }
   
   private static final Logger log = 
         LoggerFactory.getLogger(CovidAPIService.class);
   private RestTemplate restTemplate;
   private String covidDataUrl;
   
   public CovidAPIService(
         @Value("${covidData.url}") final String covidUrl) {
      
      this.restTemplate = new RestTemplate();
      this.covidDataUrl = covidUrl;
   }
   
   // This method will be used frequently to pull current COVID data
   public void pullCurrentCovidData() {
      ResponseEntity<List<JsonCovidCurrentHelper>> response = 
            restTemplate.exchange(
                  covidDataUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonCovidCurrentHelper>>() {});
      List<JsonCovidCurrentHelper> covidDataJson = response.getBody();
      log.info("Status code from weather server:" + 
                  response.getStatusCodeValue());
      
      // parse the json list into new entries and save them into the db
      for(JsonCovidCurrentHelper stateData : covidDataJson) {
         covidRepository.insertDataPoint(
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
   
   public void populate() {
      ResponseEntity<List<JsonCovidHistoryHelper>> response = 
            restTemplate.exchange(
                  covidDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonCovidHistoryHelper>>() {});
      List<JsonCovidHistoryHelper> covidDataJson = response.getBody();
      log.info("Status code from weather server:" + 
                  response.getStatusCodeValue());
      
      // parse the json list into new entries and save them into the db
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