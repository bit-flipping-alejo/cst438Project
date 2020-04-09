package cst438.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cst438.domain.Coctail;
import cst438.domain.CovidData;
import cst438.domain.CovidNationalData;
import cst438.services.CoctailService;
import cst438.services.CovidService;

@Controller
public class projectController {
   @Autowired 
   private CovidService covidService;
   
   @Autowired 
   private CoctailService coctailServ;
   
   
   @GetMapping("/home")
   public String getTodaysData(Model model) {
      // Covid section
      Long todayDate = CovidData.formatDate(LocalDate.now());
      List<CovidData> currentData = covidService.fetchByDate(todayDate); 
      CovidNationalData currentNationalStats = covidService.fetchNationalStats(todayDate);    
      String positive = String.format("%,d",currentNationalStats.getTestedPositive());      
      String dead = String.format("%,d", currentNationalStats.getDeaths());
      
      model.addAttribute("allStateData", currentData);
      model.addAttribute("nationalPositive", positive);
      model.addAttribute("nationalDead", dead);
      
      // Coctail section
      Coctail thisCoctail = coctailServ.getARandomCoctail();
      model.addAttribute("coctail", thisCoctail);
      
      
      return "home";
   }
   
   @GetMapping("/populate")
   public String populateDB() {
      Long todayDate = CovidData.formatDate(LocalDate.now());
      covidService.populate(todayDate);
      return "home";
   }
}
