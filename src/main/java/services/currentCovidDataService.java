package services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import domain.covidRepository;

// Create the logic to pull from the api all current state covid data
// create a new instance of the covidData class for each state

@Service
public class currentCovidDataService {
   private static final Logger log = 
         LoggerFactory.getLogger(currentCovidDataService.class);
   private RestTemplate restTemplate;
   private String covidDataUrl;
   
   public currentCovidDataService(
         @Value("${covidData.url}") final String weatherUrl) {
      
      this.restTemplate = new RestTemplate();
      this.covidDataUrl = weatherUrl;
   }
   
   public void saveCurrentCovidData() {
      ResponseEntity<List<JsonCovidHelper>> response = 
            restTemplate.exchange(
                  covidDataUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonCovidHelper>>() {});
      List<JsonCovidHelper> covidDataJson = response.getBody();
      log.info("Status code from weather server:" + 
                  response.getStatusCodeValue());
      
      // parse the json list into new entries and save them into the db
      for(JsonCovidHelper stateData : covidDataJson) {
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
}

// jSON helper class
@JsonIgnoreProperties(ignoreUnknown = true)
class JsonCovidHelper {
   private String state;
   private long positive;
   private long negative;
   private long hospitalizedCurrently;
   private long hospitalizedCumulative;
   private long inIcuCurrently;
   private long inIcuCumulative;
   private long onVentilatorCurrently;
   private long onVentilatorCumulative;
   private long recovered;
   private long death;
   private long hospitalized;
   private long totalTestResults;
   
   public String getState() {
      return state;
   }
   public void setState(String state) {
      this.state = state;
   }
   public long getPositive() {
      return positive;
   }
   public void setPositive(long positive) {
      this.positive = positive;
   }
   public long getNegative() {
      return negative;
   }
   public void setNegative(long negative) {
      this.negative = negative;
   }
   public long getHospitalizedCurrently() {
      return hospitalizedCurrently;
   }
   public void setHospitalizedCurrently(long hospitalizedCurrently) {
      this.hospitalizedCurrently = hospitalizedCurrently;
   }
   public long getHospitalizedCumulative() {
      return hospitalizedCumulative;
   }
   public void setHospitalizedCumulative(long hospitalizedCumulative) {
      this.hospitalizedCumulative = hospitalizedCumulative;
   }
   public long getInIcuCurrently() {
      return inIcuCurrently;
   }
   public void setInIcuCurrently(long inIcuCurrently) {
      this.inIcuCurrently = inIcuCurrently;
   }
   public long getInIcuCumulative() {
      return inIcuCumulative;
   }
   public void setInIcuCumulative(long inIcuCumulative) {
      this.inIcuCumulative = inIcuCumulative;
   }
   public long getOnVentilatorCurrently() {
      return onVentilatorCurrently;
   }
   public void setOnVentilatorCurrently(long onVentilatorCurrently) {
      this.onVentilatorCurrently = onVentilatorCurrently;
   }
   public long getOnVentilatorCumulative() {
      return onVentilatorCumulative;
   }
   public void setOnVentilatorCumulative(long onVentilatorCumulative) {
      this.onVentilatorCumulative = onVentilatorCumulative;
   }
   public long getRecovered() {
      return recovered;
   }
   public void setRecovered(long recovered) {
      this.recovered = recovered;
   }
   public long getDeath() {
      return death;
   }
   public void setDeath(long death) {
      this.death = death;
   }
   public long getHospitalized() {
      return hospitalized;
   }
   public void setHospitalized(long hospitalized) {
      this.hospitalized = hospitalized;
   }
   public long getTotalTestResults() {
      return totalTestResults;
   }
   public void setTotalTestResults(long totalTestResults) {
      this.totalTestResults = totalTestResults;
   }

}
