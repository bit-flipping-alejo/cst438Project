package cst438.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import cst438.domain.Model.Coctail;
import cst438.domain.Model.CovidStateData;
import cst438.domain.Model.States;
import cst438.domain.Model.User;
import cst438.domain.Helper.FilterForm;
import cst438.domain.Helper.NationalDisplayHelper;
import cst438.services.CoctailService;
import cst438.services.CovidService;
import cst438.services.StatesService;
import cst438.services.UserService;

@Controller
public class projectController {
   @Autowired 
   private CovidService covidService;
   @Autowired 
   private CoctailService coctailServ;
   @Autowired
   private UserService userServ;   
   @Autowired
   private StatesService stateServ;

   
   
   /*//////////////////////////////*/
   /*Get Mappings*/
   /*//////////////////////////////*/
   
   @GetMapping("/")
   public String getCurrentData(Model model, RedirectAttributes redirectAttrs) {
      
      // Covid section
      List<CovidStateData> currentData = covidService.fetchCurrentStateStats(); 
      NationalDisplayHelper nationalStats = 
            covidService.fetchCurrentNationalStats();
      
      model.addAttribute("allStateData", currentData);
      model.addAttribute("nationalStats", nationalStats);
      model.addAttribute("nationalPosChange", 
            nationalStats.getPositiveChange());
      model.addAttribute("nationalDeadChange", 
            nationalStats.getDeadChange());
      
      // Coctail section
      Coctail thisCoctail = coctailServ.getARandomCoctail();
      model.addAttribute("coctail", thisCoctail);
      
      // redirected user input
      User redirectUser = (User) model.asMap().get("user");
      // redirectUser should not be null in the "real world"
      // keeping null check for compatibility with localhost:8080/
      if (redirectUser != null) {         
         model.addAttribute("user", redirectUser);
      }
      
      return "home";
   }
   
   @GetMapping("/login")
   public String login(Model model) {
      model.addAttribute("user", new User());
      return "login";
   }
   
   @GetMapping("/register")
   public String register(Model model) {
      model.addAttribute("user", new User());
      
      List<States> stateList = stateServ.fetchAll();
      model.addAttribute("stateList", stateList);
      
      return "register";
   }
   
   @GetMapping("/user")
   public String userLanding(
         Model model, 
         @ModelAttribute User user, 
         RedirectAttributes redirectAttrs) {

      // grab the logged in or just registered user passed from that route
      User redirectUser = (User) model.asMap().get("user");
      
      // let's grab the state info for our user
      // default to the recent 5 in descending order
      List<CovidStateData> stateInfo = 
            covidService.fetchByStateAndDate(
                  redirectUser.getState(), "5", "asc");
      
      // create our form object
      FilterForm form = new FilterForm();
 
      // create state array for the select list
      List<States> states = stateServ.fetchAll();
      States stateName = stateServ.fetchByState(redirectUser.getState());
      model.addAttribute("stateSelected", stateName.getState_code()
            );
      model.addAttribute("stateName", 
            stateName.getState());
      model.addAttribute("states", states);
      model.addAttribute("form", form);
      model.addAttribute("user", user);
      model.addAttribute("stateInfo", stateInfo);
      redirectAttrs.addFlashAttribute("user", user);
      
      // Coctail section
      Coctail thisCoctail = coctailServ.getARandomCoctail();
      model.addAttribute("coctail", thisCoctail);
      
      return "userHome";
   }
   

   
   /*//////////////////////////////*/
   /*Post Mappings*/
   /*//////////////////////////////*/
   
   @PostMapping("/login")
   public RedirectView loginWithUserData(
         @ModelAttribute User user, 
         RedirectAttributes redirectAttrs) {
      
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      User repoUser = userServ.findByNameAndPassword(
            user.getName(), user.getPassword());      
      
      if (repoUser == null) {
         redirView.setUrl("/login");
         
      } else {
         redirectAttrs.addFlashAttribute("user", repoUser);
         redirView.setUrl("/user");
      }
      
      return redirView;
   }
   
   @PostMapping("/register")
   public RedirectView registerWithUserData(
         @ModelAttribute User user, 
         RedirectAttributes redirectAttrs) {
      
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      user.printToConsole();
      
      User testIfExistsUser = userServ.findByNameAndPassword(
            user.getName(), user.getPassword());
      
      if (testIfExistsUser != null ) {
         // user exists, dont register
         redirView.setUrl("/login");
      } else {
         userServ.insertUser(user);
         redirectAttrs.addFlashAttribute("user", user);
         redirView.setUrl("/user");
      }
      
      return redirView;
   }
   
   @PostMapping("/user")
   public String filterQueryUpdate(
         @ModelAttribute FilterForm form,
         @ModelAttribute User user, 
         RedirectAttributes redirectAttrs,
         Model model) {
      
      // grab the logged in or just registered user passed from that route
      User redirectUser = (User) model.asMap().get("user");
      
      // using form data, query the DB with new search parameters
      List<CovidStateData> stateInfo = 
            covidService.fetchByStateAndDate(
                  form.getState(), form.getDaysBack(), form.getDirection());
      
      // updates first selection(default) with full state name
      States stateName = stateServ.fetchByState(redirectUser.getState());
      model.addAttribute("stateSelected", stateName.getState_code());
      model.addAttribute("stateName", stateName.getState());
      
      // send state array to page
      List<States> states = stateServ.fetchAll();
      model.addAttribute("stateInfo", stateInfo);
      model.addAttribute("form", form);
      model.addAttribute("states", states);
      model.addAttribute("user", user);
      redirectAttrs.addFlashAttribute("user", user);
      
      // Coctail section
      Coctail thisCoctail = coctailServ.getARandomCoctail();
      model.addAttribute("coctail", thisCoctail);
      
      return "userHome";
   }
}
