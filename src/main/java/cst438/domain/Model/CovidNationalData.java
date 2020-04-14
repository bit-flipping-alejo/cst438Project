package cst438.domain.Model;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
public class CovidNationalData {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long ID;
   private LocalDate date;
   private long States_affected;
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
   
   public CovidNationalData() {
      
   }
   
   public CovidNationalData(
         long ID,
         LocalDate date,
         long States_affected,
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
      this.date = date;
      this.States_affected = States_affected;
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
      ID = iD;
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }

   public long getStates_affected() {
      return States_affected;
   }

   public void setStates_affected(long states_affected) {
      States_affected = states_affected;
   }

   public long getTestedPositive() {
      return TestedPositive;
   }

   public void setTestedPositive(long testedPositive) {
      TestedPositive = testedPositive;
   }

   public long getTestedNegative() {
      return TestedNegative;
   }

   public void setTestedNegative(long testedNegative) {
      TestedNegative = testedNegative;
   }

   public long getCurrentlyHospitalized() {
      return CurrentlyHospitalized;
   }

   public void setCurrentlyHospitalized(long currentlyHospitalized) {
      CurrentlyHospitalized = currentlyHospitalized;
   }

   public long getTotalHospitalized() {
      return TotalHospitalized;
   }

   public void setTotalHospitalized(long totalHospitalized) {
      TotalHospitalized = totalHospitalized;
   }

   public long getCurrentICUCount() {
      return CurrentICUCount;
   }

   public void setCurrentICUCount(long currentICUCount) {
      CurrentICUCount = currentICUCount;
   }

   public long getTotalICUCount() {
      return TotalICUCount;
   }

   public void setTotalICUCount(long totalICUCount) {
      TotalICUCount = totalICUCount;
   }

   public long getCurrentlyOnVentilator() {
      return CurrentlyOnVentilator;
   }

   public void setCurrentlyOnVentilator(long currentlyOnVentilator) {
      CurrentlyOnVentilator = currentlyOnVentilator;
   }

   public long getTotalVentilated() {
      return TotalVentilated;
   }

   public void setTotalVentilated(long totalVentilated) {
      TotalVentilated = totalVentilated;
   }

   public long getRecovered() {
      return Recovered;
   }

   public void setRecovered(long recovered) {
      Recovered = recovered;
   }

   public long getDeaths() {
      return Deaths;
   }

   public void setDeaths(long deaths) {
      Deaths = deaths;
   }
}
