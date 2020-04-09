package cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cst438.domain.Coctail;
import cst438.services.CoctailService;

@RestController
public class projectRestController {

   @Autowired
   private CoctailService coctailServ;
   
   @GetMapping("/api/1/RandomCoctail")
   public Coctail getRandomCoctail() {    
      //try {
         return coctailServ.getARandomCoctail();   
      //} catch (Exception e) {
      //   throw new ResponseStatusException( HttpStatus.NOT_FOUND, "No luck ya booze hound" );
      //}
   }
}
