package cst438.domain.Helper;

/**This is the standard form object container similar to the homework assignments
 * Its purpose is to hold the form data from the user home page. 
 * */

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
}
