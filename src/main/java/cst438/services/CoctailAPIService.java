package cst438.services;

import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import cst438.domain.Model.Coctail;

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
      Coctail retCoctail = new Coctail();
      
      // returned array is of size 1, so even though its an iterator it only goes thru once
      // so it doesnt set the members of retCoctail more than once even though its in a 
      // for loop
      for (Iterator<JsonNode> it = json.get("drinks").elements(); it.hasNext();) {
         JsonNode thisDrink = it.next();
         retCoctail.setName( thisDrink.get("strDrink").asText() );         
         retCoctail.setPicUrl( thisDrink.get("strDrinkThumb").asText() );
         retCoctail.setInstructions( thisDrink.get("strInstructions").asText() );      
         
         // ingredients are received as multiple key value pairs.
         ArrayList<String> ingredients = new ArrayList<String>();
         String quantity, ingredient;
         for (int i = 1; i < 16; i++) {
            if (!thisDrink.get("strIngredient" + i).isNull()) {
               ingredient = thisDrink.get("strIngredient" + i).asText();
               if (!thisDrink.get("strMeasure" + i).isNull()) {
                  quantity = thisDrink.get("strMeasure" + i).asText();
                  ingredients.add(quantity + ingredient);
               } else {
                  ingredients.add(ingredient);
               }
            }
         }
         retCoctail.setIngredients(ingredients);
      }
      
      return retCoctail;
   }
}
