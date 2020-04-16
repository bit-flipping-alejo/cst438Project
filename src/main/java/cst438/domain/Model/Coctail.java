package cst438.domain.Model;

import java.util.ArrayList;

public class Coctail {

   String name;
   String picUrl;
   String instructions;
   ArrayList<String> ingredients;
   
   public Coctail() {
      
   }

   
   public Coctail(
         String name, 
         String picUrl, 
         String instructions,
         ArrayList<String> ingredients) {
      super();
      this.name = name;
      this.picUrl = picUrl;
      this.instructions = instructions;
      this.ingredients = ingredients;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPicUrl() {
      return picUrl;
   }

   public void setPicUrl(String picUrl) {
      this.picUrl = picUrl;
   }

   public String getInstructions() {
      return instructions;
   }

   public void setInstructions(String instructions) {
      this.instructions = instructions;
   }


   public ArrayList<String> getIngredients() {
      return ingredients;
   }


   public void setIngredients(ArrayList<String> ingredients) {
      this.ingredients = ingredients;
   }
}
