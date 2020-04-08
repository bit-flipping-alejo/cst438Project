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
   
   public List<CovidData> fetchByDate(long date) {
      List<CovidData> todayStateData = covidRepository.findByDate(date);
      return todayStateData;
   }
   
   public List<CovidData> fetchAll() {
      List<CovidData> allStates = covidRepository.findAll();
      return allStates;
   }
   
   public CovidNationalData fetchNationalStats(long todayDate) {
      CovidNationalData NationalStats = covidNationalRepository.findByDate(todayDate);
      return NationalStats;
   }
   
   // Use this to populate your MySQL database
   // **** IMPORTANT: USE IT ONCE, THEN COMMENT OUT ****
   public void populate(long date) {
      // populateStates pulls the historical data from the API. We'll ensure the
      // table is empty before updating.
      covidAPIService.populateStates();
      
      // insertNationalStats does not require a clean table
      covidAPIService.insertNationalStats(date);
   }
}
