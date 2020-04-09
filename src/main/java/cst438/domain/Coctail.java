package cst438.domain;

public class Coctail {

   String name;
   String picUrl;
   String instructions;
   
   public Coctail() {
      
   }

   
   public Coctail(String name, String picUrl, String instructions) {
      super();
      this.name = name;
      this.picUrl = picUrl;
      this.instructions = instructions;
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
   
   
   
}
