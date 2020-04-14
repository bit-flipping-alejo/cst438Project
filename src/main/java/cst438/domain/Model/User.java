package cst438.domain.Model;

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
   
   private String state;
   
   public User() {
      
   }

   public User(String name, String password, Integer numberOfVisits, String state) {
      super();
      this.name = name;
      this.password = password;
      this.numberOfVisits = numberOfVisits;
      this.state = state;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   
   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public void setNumberOfVisits(long numberOfVisits) {
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
   
   public void printToConsole() {
      System.out.println(">>>>>>>><<<<<<<<<<<<<<");
      System.out.println("You created a user with name: " + this.name + " and pw: " + this.password);
      System.out.println(">>>>>>>><<<<<<<<<<<<<<");
   }
   
   
}
