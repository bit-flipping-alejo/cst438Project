package domain;

import javax.persistence.*;

@Entity

public class covidData {
   @Id
   @GeneratedValue
   private long ID;

   private String State;
   
   private long TestedPositive;
   
   private long TestedNegative;
   
   private long AmountCurrentlyHospitalized;
   
   private long TotalHospitalized;
   
   private long CurrentICUCount;
   
   private long TotalICUCount;
   
   private long CurrentlyOnVentilator;
   
   private long TotalVentilated;
   
   private long Recovered;
   
   private long Deaths;
   
   public covidData() {
      
   }
   
   public covidData(
         long ID,
         String State,
         long TestedPositive,
         long TestedNegative,
         long AmountCurrentlyHospitalized,
         long TotalHospitalized,
         long CurrentICUCount,
         long TotalICUCount,
         long CurrentlyOnVentilator,
         long TotalVentilated,
         long Recovered,
         long Deaths) {
      
      super();
      this.ID = ID;
      this.State = State;
      this.TestedPositive = TestedPositive;
      this.AmountCurrentlyHospitalized = AmountCurrentlyHospitalized;
      this.TotalHospitalized = TotalHospitalized;
      this.CurrentICUCount = CurrentICUCount;
      this.TotalICUCount = TotalICUCount;
      this.CurrentlyOnVentilator = CurrentlyOnVentilator;
      this.TotalVentilated = TotalVentilated;
      this.Recovered = Recovered;
      this.Deaths = Deaths;
   }

   public long getID() {
      return ID;
   }

   public void setID(long iD) {
      this.ID = iD;
   }

   public String getState() {
      return State;
   }

   public void setState(String state) {
      this.State = state;
   }

   public long getTestedPositive() {
      return TestedPositive;
   }

   public void setTestedPositive(long testedPositive) {
      this.TestedPositive = testedPositive;
   }

   public long getTestedNegative() {
      return TestedNegative;
   }

   public void setTestedNegative(long testedNegative) {
      this.TestedNegative = testedNegative;
   }

   public long getAmountCurrentlyHospitalized() {
      return AmountCurrentlyHospitalized;
   }

   public void setAmountCurrentlyHospitalized(long amountCurrentlyHospitalized) {
      this.AmountCurrentlyHospitalized = amountCurrentlyHospitalized;
   }

   public long getTotalHospitalized() {
      return TotalHospitalized;
   }

   public void setTotalHospitalized(long totalHospitalized) {
      this.TotalHospitalized = totalHospitalized;
   }

   public long getCurrentICUCount() {
      return CurrentICUCount;
   }

   public void setCurrentICUCount(long currentICUCount) {
      this.CurrentICUCount = currentICUCount;
   }

   public long getTotalICUCount() {
      return TotalICUCount;
   }

   public void setTotalICUCount(long totalICUCount) {
      this.TotalICUCount = totalICUCount;
   }

   public long getCurrentlyOnVentilator() {
      return CurrentlyOnVentilator;
   }

   public void setCurrentlyOnVentilator(long currentlyOnVentilator) {
      this.CurrentlyOnVentilator = currentlyOnVentilator;
   }

   public long getTotalVentilated() {
      return TotalVentilated;
   }

   public void setTotalVentilated(long totalVentilated) {
      this.TotalVentilated = totalVentilated;
   }

   public long getRecovered() {
      return Recovered;
   }

   public void setRecovered(long recovered) {
      this.Recovered = recovered;
   }

   public long getDeaths() {
      return Deaths;
   }

   public void setDeaths(long deaths) {
      this.Deaths = deaths;
   }
}
