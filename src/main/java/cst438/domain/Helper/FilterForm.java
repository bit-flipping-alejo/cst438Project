package cst438.domain.Helper;

public class FilterForm {
   private String state;
   private String daysBack;
   private String direction;
   
   public String getState() {
      return state;
   }
   public void setState(String state) {
      this.state = state;
   }
   public String getDaysBack() {
      return daysBack;
   }
   public void setDaysBack(String daysBack) {
      this.daysBack = daysBack;
   }
   
   public String getDirection() {
      return direction;
   }
   public void setDirection(String direction) {
      this.direction = direction;
   }
   public void printToConsole() {
      System.out.println(">>>>>>>><<<<<<<<<<<<<<");
      System.out.println("form data: " + this.state + " days: " + this.daysBack
            + "direction: " + this.direction);
      System.out.println(">>>>>>>><<<<<<<<<<<<<<");
   }
}
