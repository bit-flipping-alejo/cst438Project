package cst438.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.CovidData;
import cst438.domain.CovidNationalData;
import cst438.domain.CovidNationalRepository;
import cst438.domain.CovidRepository;

/* This service is the 'front' facing service meaning it pulls all data from
 * the repositories. No external API calls in here.
 * */

@Service
public class CovidService {
   @Autowired
   private CovidRepository covidRepository;
   @Autowired
   private CovidAPIService covidAPIService;
   @Autowired
   private CovidNationalRepository covidNationalRepository;
   
   public CovidService() {
      
   }
   
   public CovidService(CovidRepository covidRepository,
         CovidAPIService covidAPIService) {
      this.covidRepository = covidRepository;
      this.covidAPIService = covidAPIService;
   }
   
   public List<CovidData> fetchCurrentDayStates() {
      return covidRepository.findCurrent();
   }
   public List<CovidData> fetchByDate(long date) {
      return covidRepository.findByDate(date);
   }
   
   public List<CovidData> fetchAll() {
      return covidRepository.findAll();
   }
   
   public CovidNationalData fetchNationalStats(long todayDate) {
      return covidNationalRepository.findByDate(todayDate);
   }
   
   public void populate() {
      // populateStates pulls the historical data from the API. We'll ensure the
      // table is empty within the method before updating.
      covidAPIService.populateStateStats();
      
      // populateNationalStats pulls the historical data from the API. We'll ensure the
      // table is empty within the method before updating.
      covidAPIService.populateNationalStats();
   }
}
