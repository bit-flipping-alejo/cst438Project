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

import cst438.domain.Coctail;
import cst438.domain.CovidData;
import cst438.domain.CovidNationalData;
import cst438.domain.NationalDisplayHelper;
import cst438.domain.User;
import cst438.services.CoctailService;
import cst438.services.CovidService;
import cst438.services.UserService;
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
   private UserService userServ;
   
   /*//////////////////////////////*/
   /*Get Mappings*/
   /*//////////////////////////////*/
   
   @GetMapping("/")
   public String getCurrentData(Model model, RedirectAttributes redirectAttrs) {
      
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
   public RedirectView loginWithUserData(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
      
      RedirectView redirView = new RedirectView();
      redirView.setContextRelative(true);
      
      User repoUser = userServ.findByNameAndPassword(user.getName(), user.getPassword());      
      System.out.println("Hit the /login Post mapping");
      
      
      if (repoUser == null) {
         System.out.println("User DOESNT exist");
         //maybe alert the user?
         redirView.setUrl("/login");
         
      } else {
         System.out.println("User DOES exist");
         redirectAttrs.addFlashAttribute("user", repoUser);
         redirView.setUrl("/");
      }
      
      return redirView;
   }
   
   @PostMapping("/register")
   public String registerWithUserData(@ModelAttribute User user) {
      userServ.insertUser(user);      
      System.out.println("Hit the /register Post mapping");
      return "redirect:/";
   }
   
   
}
