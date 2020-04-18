package cst438.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.Model.User;
import cst438.domain.Repository.UserRepository;

@Service
public class UserService {

   @Autowired
   private UserRepository userRepo;
   
   public UserService() {
      
   }

   public UserService(UserRepository userRepo) {
      super();
      this.userRepo = userRepo;
   }
   
   public User findByNameAndPassword(String name, String password) {
     return userRepo.findByNameAndPassword(name, password) ;
   }
   
   public User findByName(String name) {
	   return userRepo.findByName(name);
   }
   
   public void insertUser(User user) {
      userRepo.insertUser(user.getName(), user.getNumberOfVisits(), user.getPassword(), user.getState());
   }
   
   public void insertUser(String name, long numberOfVisits, String password , String state ) {
      userRepo.insertUser(name, numberOfVisits, password, state);
   }
   
}
