package cst438.services;


import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import cst438.domain.Coctail;

@Service
public class CoctailAPIService {

   private RestTemplate restTemplate;

   private String randomCoctailUrl;

   public CoctailAPIService(
         @Value("${coctails.randomDrinkUrl}") final String randomCoctailUrl) {

      this.restTemplate = new RestTemplate();
      this.randomCoctailUrl = randomCoctailUrl;
   }

   public Coctail getARandomCoctail() {
      ResponseEntity<JsonNode> response = restTemplate.getForEntity(randomCoctailUrl ,JsonNode.class);
      JsonNode json = response.getBody();       
      
      // returned array is of size 1, so even though its an iterator it only goes thru once
      // which is why return is IN the for loop.
      for (Iterator<JsonNode> it = json.get("drinks").elements(); it.hasNext();) {
         JsonNode thisDrink = it.next();
         String name = thisDrink.get("strDrink").asText();            
         String url = thisDrink.get("strDrinkThumb").asText();
         String instr = thisDrink.get("strInstructions").asText();
         return new Coctail(name, url, instr);
      }

      // below return to make Eclipse error checking happy
      return null;
   }



}
