package cst438.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.ObjectFactory;
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
   
   @Autowired
   ObjectFactory<HttpSession> httpSessionFactory;
   
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
      
      return "home";
   }
   
   @GetMapping("/login")
   public String login( Model model) {
	  // Exception handling when a logged in user tries to log in again
	  HttpSession session = httpSessionFactory.getObject();
	  
	  String username = (String) session.getAttribute("user");
      
      if (username != null) {
    	  session.removeAttribute("user");
    	  return "logout";
          
      }
      
      // Proceed as normal
      model.addAttribute("user", new User());
      return "login";
   }
   
   @GetMapping("/register")
   public String register(Model model) {
	  // Exception handling when a logged in user tries to register
	  HttpSession session = httpSessionFactory.getObject();
	  String username = (String) session.getAttribute("user");
	  
	  if (username != null) {
		  session.removeAttribute("user");
		  return "logout";
	      
	  }
	  
	  // Proceed as normal if not logged in
      model.addAttribute("user", new User());
      
      List<States> stateList = stateServ.fetchAll();
      model.addAttribute("stateList", stateList);
      
      System.out.println("Hit the /register Get mapping");
      return "register";
   }
   
   @GetMapping("/user")
   public String userLanding(
         Model model, 
         @ModelAttribute User user, 
         RedirectAttributes redirectAttrs) {
      
	  HttpSession session = httpSessionFactory.getObject();
      System.out.println("GET /user");

      // Check if user is logged in yet
      String username = (String) session.getAttribute("user");
      
      if (username == null) {
    	  model.addAttribute("error", "Please log in first to access Dashboard!");
    	  return "login";
          
      }
      // Get User object from our service
      User redirectUser = userServ.findUser(username);
      
      // let's grab the state info for our user
      // default to the recent 5 in descending order
      List<CovidData> stateInfo = 
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
   
   @GetMapping("/logout")
   public String logout() {
	   HttpSession session = httpSessionFactory.getObject();
	   
	   session.removeAttribute("user");
	   return "logout";
   }
   
   /*//////////////////////////////*/
   /*Post Mappings*/
   /*//////////////////////////////*/
   
   @PostMapping("/login")
   public RedirectView loginWithUserData(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
      
	  HttpSession session = httpSessionFactory.getObject();
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      User repoUser = userServ.findByNameAndPassword(user.getName(), user.getPassword());      
      System.out.println("Hit the /login Post mapping");
      
      if (repoUser == null) {
         System.out.println("User DOESNT exist");
         redirectAttrs.addFlashAttribute("error", "Invalid user/password combination!");
         redirView.setUrl("/login");
         
      } else {
         System.out.println("User DOES exist");
         redirectAttrs.addFlashAttribute("user", repoUser);
         session.setAttribute("user", user.getName());
         redirView.setUrl("/user");
      }
      
      return redirView;
   }
   
   @PostMapping("/register")
   public RedirectView registerWithUserData(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
      
	  HttpSession session = httpSessionFactory.getObject();
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      user.printToConsole();
      
      User testIfExistsUser = userServ.findByName(user.getName());
      
      if (testIfExistsUser != null ) {
         redirectAttrs.addFlashAttribute("error", "User already exists!");
         redirView.setUrl("/register");
      } else {
         userServ.insertUser(user);
         redirectAttrs.addFlashAttribute("user", user);
         session.setAttribute("user", user.getName());
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
      List<CovidData> stateInfo = 
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
