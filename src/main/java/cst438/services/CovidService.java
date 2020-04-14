package cst438.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.Model.CovidData;
import cst438.domain.Model.CovidNationalData;
import cst438.domain.Helper.NationalDisplayHelper;
import cst438.domain.Repository.CovidNationalRepository;
import cst438.domain.Repository.CovidRepository;
import cst438.services.CovidAPIService;

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
   
   public List<CovidData> fetchByStateAndDate(String state, String daysBack,
         String direction) {
      if (daysBack.compareTo("all") == 0) {
         if (direction.compareTo("asc") == 0) {
            return covidRepository.findByState(state);
         } else {
            return covidRepository.findByStateDesc(state);
         }
      } else {
         LocalDate dateCheck = 
               LocalDate.now().minusDays(Integer.parseInt(daysBack));
         if (direction.compareTo("asc") == 0) {
            return covidRepository.findByStateAndDate(state, dateCheck);
         } else {
            return covidRepository.findByStateAndDateDesc(state, dateCheck);
         }
         
      }
      
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
   
    public NationalDisplayHelper fetchCurrentNationalStats() {
      // grab our working data
      CovidNationalData currentNationalStats = 
            covidAPIService.pullCurrentNationalStats();
      CovidNationalData recentNationalHist = 
            covidNationalRepository.findByRecentDate();
      NationalDisplayHelper displayInfo = new NationalDisplayHelper();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, YYYY");
      
      // set our input variables
      String positive = String.format("%,d", 
            currentNationalStats.getTestedPositive());
      String dead = String.format("%,d", 
            currentNationalStats.getDeaths());
      long positiveChange = currentNationalStats.getTestedPositive() -
            recentNationalHist.getTestedPositive();
      long deadChange = currentNationalStats.getDeaths() -
            recentNationalHist.getDeaths();
      boolean isPositiveIncrease = (positiveChange > 0);
      boolean isDeadIncrease = (deadChange > 0);

      // assign our variables into the class container
      displayInfo.setPositive(positive);
      displayInfo.setDead(dead);
      displayInfo.setPositiveIncrease(isPositiveIncrease);
      displayInfo.setDeadIncrease(isDeadIncrease);
      displayInfo.setDate(formatter.format(recentNationalHist.getDate()));
      
      if (isPositiveIncrease) {
         displayInfo.setPositiveChange("+" + 
               String.format("%,d", positiveChange));
      } else {
         displayInfo.setPositiveChange("-" + 
               String.format("%,d", positiveChange));
      }
      
      if (isDeadIncrease) {
         displayInfo.setDeadChange("+" + String.format("%,d",deadChange));
      } else {
         displayInfo.setDeadChange("-" + String.format("%,d",deadChange));
      }
      
      return displayInfo;
   }
}
