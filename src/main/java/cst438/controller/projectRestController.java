package cst438.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cst438.domain.Model.Coctail;
import cst438.domain.Model.CovidData;
import cst438.domain.Model.CovidNationalData;
import cst438.services.CoctailService;
import cst438.services.CovidService;

@RestController
public class projectRestController {

   @Autowired
   private CoctailService coctailServ;
   @Autowired
   private CovidService covidServ;
   
   @GetMapping("/api/1/RandomCoctail")
   public Coctail getRandomCoctail() {    
      try {
         return coctailServ.getARandomCoctail();   
      } catch (Exception e) {
        throw new ResponseStatusException( HttpStatus.NOT_FOUND, "No luck ya booze hound" );
      }
   }
   
   // returns all current state data
   @GetMapping("/api/1/covid/state")
   public List<CovidData> getAllStateInfo() {
      return covidServ.fetchCurrentStateStats();
   }
   
   // returns all data pertaining to that state
   @GetMapping("/api/1/covid/state={code}")
   public List<CovidData> getStateInfo(@PathVariable("code") String state) {
      try {
      return covidServ.fetchByState(state);
      } catch (Exception e) {
         throw new ResponseStatusException( HttpStatus.NOT_FOUND, "State not found" );
      }
   }
   
   // returns current national data
   @GetMapping("/api/1/covid/national")
   public CovidNationalData getNationalStats() {
      return covidServ.fetchCurrentNationalData();
   }
   
   // returns all national data
   @GetMapping("/api/covid/allNational")
   public List<CovidNationalData> getAllNationalStats() {
      return covidServ.fetchAllNationalData();
   }
}
