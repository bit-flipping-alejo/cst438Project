package cst438.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cst438.domain.Coctail;
import cst438.domain.CovidData;
import cst438.domain.CovidNationalData;
import cst438.domain.User;
import cst438.services.CoctailService;
import cst438.services.CovidService;
import cst438.services.CovidAPIService;

@Controller
public class projectController {
   @Autowired 
   private CovidService covidService;
   @Autowired
   private CovidAPIService covidAPIService;
   
   @Autowired 
   private CoctailService coctailServ;
   
   
   @GetMapping("/home")
   public String getCurrentData(Model model) {
      // Covid section
      List<CovidData> currentData = covidAPIService.pullCurrentStateData(); 
      CovidNationalData currentNationalStats = 
            covidAPIService.pullCurrentNationalStats();    
      String positive = String.format("%,d", 
            currentNationalStats.getTestedPositive());      
      String dead = String.format("%,d", 
            currentNationalStats.getDeaths());
      
      model.addAttribute("allStateData", currentData);
      model.addAttribute("nationalPositive", positive);
      model.addAttribute("nationalDead", dead);
      
      // Coctail section
      Coctail thisCoctail = coctailServ.getARandomCoctail();
      model.addAttribute("coctail", thisCoctail);

      return "home";
   }
   
   /*//////////////////////////////*/
   /*Get Mappings*/
   /*//////////////////////////////*/
   
   @GetMapping("/login")
   public String login( Model model) {
      model.addAttribute("user", new User());
      return "login";
   }
   
   @GetMapping("/register")
   public String register(Model model) {
      model.addAttribute("user", new User());
      System.out.println("Hit the /register Get mapping");
      return "register";
   }
   
   @GetMapping("/populate")
   public String populateDB() {
      covidService.populate();
      return "home";
   }
   
   /*//////////////////////////////*/
   /*Post Mappings*/
   /*//////////////////////////////*/
   
   @PostMapping("/login")
   public String loginWithUserData(@ModelAttribute User user) {
      
      user.printToConsole();
      
      System.out.println("Hit the /register Post mapping");
      return "redirect:/home";
   }
   
   @PostMapping("/register")
   public String registerWithUserData(@ModelAttribute User user) {
      
      user.printToConsole();
      System.out.println("Hit the /register Post mapping");
      return "redirect:/home";
   }
   
   
}
