package cst438.domain.Model;

import javax.persistence.*;

@Entity
public class States {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long ID;
   private String state;
   private String state_code;
   
   public States() {
      
   }
   
   public States(
         long ID,
         String state,
         String state_code) {
      
      super();
      this.state = state;
      this.state_code = state_code;
   }

   public long getID() {
      return ID;
   }

   public void setID(long iD) {
      ID = iD;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getState_code() {
      return state_code;
   }

   public void setState_code(String state_code) {
      this.state_code = state_code;
   }
}
