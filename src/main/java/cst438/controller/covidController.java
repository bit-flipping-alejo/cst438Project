package cst438.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cst438.domain.CovidData;
import cst438.services.CovidService;

@Controller
public class covidController {
   @Autowired 
   private CovidService covidService;
   
   @GetMapping("/home")
   public String getTodaysData(Model model) {
      LocalDate localDate = LocalDate.now();
      String todayDate = formatDate(localDate);
      System.out.println(todayDate);
      List<CovidData> currentData = covidService.fetchByDate(
            Long.parseLong(todayDate));
      
      model.addAttribute("allStateData", currentData);
      
      return "home";
   }
   
   @GetMapping("/populate")
   public String populateDB() {
      covidService.populate();
      return "home";
   }
   
   // helper function to format date
   private String formatDate(LocalDate localDate) {
      String formattedDate  = Integer.toString(localDate.getYear());
      
      if (localDate.getMonthValue() < 10) {
         formattedDate += "0" + Integer.toString(localDate.getMonthValue());
      } else {
         formattedDate += Integer.toString(localDate.getMonthValue());
      }
      
      if (localDate.getDayOfMonth() < 10) {
         formattedDate += "0" + Integer.toString(localDate.getDayOfMonth());
      } else {
         formattedDate += Integer.toString(localDate.getDayOfMonth());
      }
      
      return formattedDate;
   }
}
