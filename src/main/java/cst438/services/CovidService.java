package cst438.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.CovidData;
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
   
   // Use this to populate your MySQL database
   // **** IMPORTANT: USE IT ONCE, THEN COMMENT OUT ****
   public void populate() {
      covidAPIService.populate();
   }
}
