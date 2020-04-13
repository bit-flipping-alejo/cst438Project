package cst438.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cst438.domain.Model.Coctail;
import cst438.domain.Model.CovidData;
import cst438.domain.Helper.FilterForm;
import cst438.domain.Helper.NationalDisplayHelper;
import cst438.domain.Model.States;
import cst438.domain.Model.User;
import cst438.services.CoctailService;
import cst438.services.CovidService;
import cst438.services.StatesService;
import cst438.services.CovidAPIService;

@Controller
public class projectController {
   @Autowired 
   private CovidService covidService;
   @Autowired
   private CovidAPIService covidAPIService;
   @Autowired 
   private CoctailService coctailServ;
   @Autowired
   private StatesService statesService;
   
   
   @GetMapping("/")
   public String getCurrentData(Model model) {
      // Covid section
      List<CovidData> currentData = covidAPIService.pullCurrentStateData(); 
      NationalDisplayHelper nationalStats = 
            covidService.fetchCurrentNationalStats();
      
      model.addAttribute("allStateData", currentData);
      model.addAttribute("nationalStats", nationalStats);
      
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
      // explore into user state using tokens/cookies/whatever the fuck
      // for now, default to Cali
      // if (User.home_state) {
      //    model.addAttribute("stateSelected", User.home_state);
      // } else {
      //    model.addAttribute("stateSelected", "CA");
      // }
      model.addAttribute("stateSelected", "CA");
      
      FilterForm form = new FilterForm();
 
      // send state array to page
      List<States> states = statesService.fetchAll();
      
      model.addAttribute("states", states);
      model.addAttribute("form", form);
      
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
   
   @PostMapping("/user")
   public String filterQueryUpdate(
         @ModelAttribute FilterForm form,
         BindingResult result,
         Model model) {
      
      // using form data, query the DB with new search parameters
      List<CovidData> stateInfo = 
            covidService.fetchByStateAndDate(
                  form.getState(), form.getDaysBack(), form.getDirection());
      // updates first selection(default) with full state name
      model.addAttribute("stateSelected", 
            statesService.fetchByState(form.getState()));
      
      // send state array to page
      List<States> states = statesService.fetchAll();
      model.addAttribute("stateInfo", stateInfo);
      model.addAttribute("form", form);
      model.addAttribute("states", states);
      
      return "userHome";
   }
}
