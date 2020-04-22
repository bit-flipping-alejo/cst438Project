package cst438.domain.Helper;

/**This class is the container for the web site data. Essentially it holds all
 * information needed for the web site to properly display. All fields are 
 * formatted and ready for insertion.
 * */

public class NationalDisplayHelper {
   private String positive;
   private String dead;
   private String positiveChange;
   private String deadChange;
   private boolean isPositiveIncrease;
   private boolean isDeadIncrease;
   private String date;
   
   public NationalDisplayHelper() {}
   
   public NationalDisplayHelper(
         String positive,
         String dead,
         String positiveChange,
         String deadChange,
         boolean isPositiveIncrease,
         boolean isDeadIncrease, 
         String date) {
      
      super();
      this.positive = positive;
      this.dead = dead;
      this.positiveChange = positiveChange;
      this.deadChange = deadChange;
      this.isPositiveIncrease = isPositiveIncrease;
      this.isDeadIncrease = isDeadIncrease;
      this.date = date;
   }

   public String getPositive() {
      return positive;
   }

   public void setPositive(String positive) {
      this.positive = positive;
   }

   public String getDead() {
      return dead;
   }

   public void setDead(String dead) {
      this.dead = dead;
   }

   public String getPositiveChange() {
      return positiveChange;
   }

   public void setPositiveChange(String positiveChange) {
      this.positiveChange = positiveChange;
   }

   public String getDeadChange() {
      return deadChange;
   }

   public void setDeadChange(String deadChange) {
      this.deadChange = deadChange;
   }

   public boolean isPositiveIncrease() {
      return isPositiveIncrease;
   }

   public void setPositiveIncrease(boolean isPositiveIncrease) {
      this.isPositiveIncrease = isPositiveIncrease;
   }

   public boolean isDeadIncrease() {
      return isDeadIncrease;
   }

   public void setDeadIncrease(boolean isDeadIncrease) {
      this.isDeadIncrease = isDeadIncrease;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }
}
