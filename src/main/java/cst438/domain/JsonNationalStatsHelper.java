package cst438.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonNationalStatsHelper {
   //private long date;
   //private String state;
   private long positive;
   private long negative;
   private long hospitalizedCurrently;
   private long hospitalizedCumulative;
   private long inIcuCurrently;
   private long inIcuCumulative;
   private long onVentilatorCurrently;
   private long onVentilatorCumulative;
   private long recovered;
   private long death;
   private long hospitalized;
   private long totalTestResults;
   
//   public long getDate() {
//      return date;
//   }
//   public void setDate(long date) {
//      this.date = date;
//   }
//   public String getState() {
//      return state;
//   }
//   public void setState(String state) {
//      this.state = state;
//   }
   public long getPositive() {
      return positive;
   }
   public void setPositive(long positive) {
      this.positive = positive;
   }
   public long getNegative() {
      return negative;
   }
   public void setNegative(long negative) {
      this.negative = negative;
   }
   public long getHospitalizedCurrently() {
      return hospitalizedCurrently;
   }
   public void setHospitalizedCurrently(long hospitalizedCurrently) {
      this.hospitalizedCurrently = hospitalizedCurrently;
   }
   public long getHospitalizedCumulative() {
      return hospitalizedCumulative;
   }
   public void setHospitalizedCumulative(long hospitalizedCumulative) {
      this.hospitalizedCumulative = hospitalizedCumulative;
   }
   public long getInIcuCurrently() {
      return inIcuCurrently;
   }
   public void setInIcuCurrently(long inIcuCurrently) {
      this.inIcuCurrently = inIcuCurrently;
   }
   public long getInIcuCumulative() {
      return inIcuCumulative;
   }
   public void setInIcuCumulative(long inIcuCumulative) {
      this.inIcuCumulative = inIcuCumulative;
   }
   public long getOnVentilatorCurrently() {
      return onVentilatorCurrently;
   }
   public void setOnVentilatorCurrently(long onVentilatorCurrently) {
      this.onVentilatorCurrently = onVentilatorCurrently;
   }
   public long getOnVentilatorCumulative() {
      return onVentilatorCumulative;
   }
   public void setOnVentilatorCumulative(long onVentilatorCumulative) {
      this.onVentilatorCumulative = onVentilatorCumulative;
   }
   public long getRecovered() {
      return recovered;
   }
   public void setRecovered(long recovered) {
      this.recovered = recovered;
   }
   public long getDeath() {
      return death;
   }
   public void setDeath(long death) {
      this.death = death;
   }
   public long getHospitalized() {
      return hospitalized;
   }
   public void setHospitalized(long hospitalized) {
      this.hospitalized = hospitalized;
   }
   public long getTotalTestResults() {
      return totalTestResults;
   }
   public void setTotalTestResults(long totalTestResults) {
      this.totalTestResults = totalTestResults;
   }
   
}