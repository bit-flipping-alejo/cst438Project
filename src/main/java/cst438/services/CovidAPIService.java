package cst438.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cst438.domain.Model.CovidData;
import cst438.domain.Model.CovidNationalData;
import cst438.domain.Repository.CovidRepository;
import cst438.domain.Helper.JsonCovidCurrentHelper;
import cst438.domain.Helper.JsonNationalStatsHelper;

/* This service is responsible for pulling the data from the external API
 * It will be used by the controller to pull current data.*/

@Service
public class CovidAPIService {
   private RestTemplate restTemplate;
   private String currentStatesDataUrl;
   private String currentNationalDataUrl;
   
   public CovidAPIService(
         @Value("${currentCovidStatesData.url}") final String currentStatesUrl,
         @Value("${currentCovidNationalData.url}") final String currNationUrl,
         @Value("${historicalCovidStatesData.url}") final String histStatesUrl,
         @Value("${historicalCovidNationalData.url}") final String histNationalUrl,
         CovidRepository covidRepository) {
      this.restTemplate = new RestTemplate();
      this.currentStatesDataUrl = currentStatesUrl;
      this.currentNationalDataUrl = currNationUrl;
   }
       
   // This method will be used frequently to pull current state COVID data
   // Will immediately construct the List<CovidData>, ready to pass to the
   // controller. This method does not save to the database.
   public List<CovidData> pullCurrentStateData() {
      ResponseEntity<List<JsonCovidCurrentHelper>> response = 
            restTemplate.exchange(
                  this.currentStatesDataUrl, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<List<JsonCovidCurrentHelper>>
                  () {});
      List<JsonCovidCurrentHelper> covidDataJson = response.getBody();
      
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
}