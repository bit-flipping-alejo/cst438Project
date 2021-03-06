package cst438.domain.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cst438.domain.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
   
   String insertUser = "INSERT INTO user VALUES (null, :name, :numberOfVisits, :password, :state)";
   
   User findByNameAndPassword(String name, String password);
   User findByName(String name);
   
   @Modifying
   @Transactional
   @Query(value=insertUser, nativeQuery=true)
   void insertUser(@Param("name") String name
         , @Param("numberOfVisits") long numberOfVisits
         , @Param("password") String password 
         , @Param("state") String state );

   
   // Return user model
   String getUser = "SELECT * FROM user WHERE name = :name";
   @Query(value=getUser, nativeQuery=true)
   User findUser(@Param("name") String name);
   
}
