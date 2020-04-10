package cst438.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
  
   @Id
   @GeneratedValue
   private long id;
   
   private String name;
   
   private String password;
   
   private long numberOfVisits;
   
   public User() {
      
   }

   public User(String name, String password, Integer numberOfVisits) {
      super();
      this.name = name;
      this.password = password;
      this.numberOfVisits = numberOfVisits;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public long getNumberOfVisits() {
      return numberOfVisits;
   }

   public void setNumberOfVisits(Integer numberOfVisits) {
      this.numberOfVisits = numberOfVisits;
   }
   
   
   
}
