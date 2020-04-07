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
      covidService.populate();
      LocalDate localDate = LocalDate.now();
      String todayDate = Integer.toString(localDate.getYear()) + 
            Integer.toString(localDate.getMonthValue()) + 
            Integer.toString(localDate.getDayOfMonth());
      System.out.println(todayDate);
      List<CovidData> currentData = covidService.fetchByDate(
            Long.parseLong(todayDate));
      
      model.addAllAttributes(currentData);
      
      return "home";
   }
}
