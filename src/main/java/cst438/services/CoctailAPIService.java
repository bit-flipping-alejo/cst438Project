package cst438.services;


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

      String name = json.get("drinks").get("0").get("strDrink").asText();            
      String url = json.get("drinks").get("0").get("strDrinkThumb").asText();
      String instr = json.get("drinks").get("0").get("strInstructions").asText();

      System.out.println(">>>>>>>>>>>>><<<<<<<<<<<<<<");
      System.out.println(name);
      System.out.println(">>>>>>>>>>>>><<<<<<<<<<<<<<");
      return new Coctail(name, url, instr);
   }



}
