package cst438.domain;

import javax.persistence.*;

@Entity
public class CovidData {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long ID;
   private long Date;
   private String State;
   private long TestedPositive;
   private long TestedNegative;
   private long CurrentlyHospitalized;
   private long TotalHospitalized;
   private long CurrentICUCount;
   private long TotalICUCount;
   private long CurrentlyOnVentilator;
   private long TotalVentilated;
   private long Recovered;
   private long Deaths;
   
   public CovidData() {
      
   }
   
   public CovidData(
         long ID,
         String State,
         long TestedPositive,
         long TestedNegative,
         long CurrentlyHospitalized,
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
      this.CurrentlyHospitalized = CurrentlyHospitalized;
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

   public long getDate() {
      return Date;
   }

   public void setDate(long date) {
      Date = date;
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

   public long getCurrentlyHospitalized() {
      return CurrentlyHospitalized;
   }

   public void setCurrentlyHospitalized(long CurrentlyHospitalized) {
      this.CurrentlyHospitalized = CurrentlyHospitalized;
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