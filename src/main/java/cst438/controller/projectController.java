package cst438.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import cst438.domain.Model.Coctail;
import cst438.domain.Model.CovidData;
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
      List<CovidData> currentData = covidService.fetchCurrentStateStats(); 
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
   public String login( Model model) {
      model.addAttribute("user", new User());
      return "login";
   }
   
   @GetMapping("/register")
   public String register(Model model) {
      model.addAttribute("user", new User());
      
      List<States> stateList = stateServ.fetchAll();
      model.addAttribute("stateList", stateList);
      
      System.out.println("Hit the /register Get mapping");
      return "register";
   }
   
   @GetMapping("/user")
   public String userLanding(Model model, @ModelAttribute User user, RedirectAttributes redirectAttrs) {
      
      System.out.println("GET /user");

      // grab the logged in or just registered user passed from that route
      User redirectUser = (User) model.asMap().get("user");
      
      // let's grab the state info for our user
      // default to the recent 5 in descending order
      List<CovidData> stateInfo = 
            covidService.fetchByStateAndDate(
                  redirectUser.getState(), "5", "desc");
      
      // create our form object
      FilterForm form = new FilterForm();
 
      // create state array for the select list
      List<States> states = stateServ.fetchAll();
      
      model.addAttribute("stateSelected", redirectUser.getState());
      model.addAttribute("states", states);
      model.addAttribute("form", form);
      model.addAttribute("user", user);
      model.addAttribute("stateInfo", stateInfo);
      
      // Coctail section
      Coctail thisCoctail = coctailServ.getARandomCoctail();
      model.addAttribute("coctail", thisCoctail);
      
      return "userHome";
   }
   
   
   
   
   
   
   /*//////////////////////////////*/
   /*Post Mappings*/
   /*//////////////////////////////*/
   
   @PostMapping("/login")
   public RedirectView loginWithUserData(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
      
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      User repoUser = userServ.findByNameAndPassword(user.getName(), user.getPassword());      
      System.out.println("Hit the /login Post mapping");
      
      
      if (repoUser == null) {
         System.out.println("User DOESNT exist");
         //maybe alert the user?
         // IA: yes, make it so it redirects to the current page and uses
         // the error field in thymeleaf like the first week assignment
         redirView.setUrl("/login");
         
      } else {
         System.out.println("User DOES exist");
         redirectAttrs.addFlashAttribute("user", repoUser);
         redirView.setUrl("/user");
      }
      
      return redirView;
   }
   
   @PostMapping("/register")
   public RedirectView registerWithUserData(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
      
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      user.printToConsole();
      
      User testIfExistsUser = userServ.findByNameAndPassword(user.getName(), user.getPassword());
      
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
         BindingResult result,
         Model model) {
      
      // using form data, query the DB with new search parameters
      List<CovidData> stateInfo = 
            covidService.fetchByStateAndDate(
                  form.getState(), form.getDaysBack(), form.getDirection());
      // updates first selection(default) with full state name
      model.addAttribute("stateSelected", 
            stateServ.fetchByState(form.getState()));
      
      // send state array to page
      List<States> states = stateServ.fetchAll();
      model.addAttribute("stateInfo", stateInfo);
      model.addAttribute("form", form);
      model.addAttribute("states", states);
      
      return "userHome";
   }
}
