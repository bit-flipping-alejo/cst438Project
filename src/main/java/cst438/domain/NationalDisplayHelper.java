package cst438.domain;

import java.time.LocalDate;

public class NationalDisplayHelper {
   private String positive;
   private String dead;
   private String positiveChange;
   private String deadChange;
   private boolean isPositiveIncrease;
   private boolean isDeadIncrease;
   private LocalDate date;
   
   public NationalDisplayHelper() {}
   
   public NationalDisplayHelper(
         String positive,
         String dead,
         String positiveChange,
         String deadChange,
         boolean isPositiveIncrease,
         boolean isDeadIncrease, 
         LocalDate date) {
      
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

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }
}
