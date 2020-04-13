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
import cst438.domain.NationalDisplayHelper;
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
   
   
   @GetMapping("/")
   public String getCurrentData(Model model) {
      // Covid section
      List<CovidData> currentData = covidAPIService.pullCurrentStateData(); 
      NationalDisplayHelper nationalStats = 
            covidService.fetchCurrentNationalStats();
      
      model.addAttribute("allStateData", currentData);
      model.addAttribute("nationalPositive", nationalStats.getPositive());
      model.addAttribute("nationalDead", nationalStats.getDead());
      model.addAttribute("nationalPosChange", 
            nationalStats.getPositiveChange());
      model.addAttribute("nationalDeadChange", 
            nationalStats.getDeadChange());
      model.addAttribute("nationalPosInd", nationalStats.isPositiveIncrease());
      model.addAttribute("nationalDeadInd", nationalStats.isDeadIncrease());
      model.addAttribute("nationalHistDate", nationalStats.getDate());
      
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
   
   @GetMapping("/user")
   public String userLanding(Model model) {
      return "userHome";
   }
   
   /*//////////////////////////////*/
   /*Post Mappings*/
   /*//////////////////////////////*/
   
   @PostMapping("/login")
   public String loginWithUserData(@ModelAttribute User user) {
      // let's actually make this work. Use the empty UserService class for logic
      user.printToConsole();
      
      System.out.println("Hit the /register Post mapping");
      return "redirect:/home";
   }
   
   @PostMapping("/register")
   public String registerWithUserData(@ModelAttribute User user) {
      // let's actually make this work. Use the empty UserService class for logic
      user.printToConsole();
      System.out.println("Hit the /register Post mapping");
      return "redirect:/home";
   }
   
   
}
