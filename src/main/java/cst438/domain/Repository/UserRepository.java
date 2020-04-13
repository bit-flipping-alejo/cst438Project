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
   
   String insertUser = "INSERT INTO user VALUES (null, :name, :numberOfVisits,"
         + " :password)";
   
   User findByNameAndPassword(String name, String password);
   
   @Modifying
   @Transactional
   @Query(value=insertUser, nativeQuery=true)
   void insertUser(
         @Param("name") String name, 
         @Param("numberOfVisits") long numberOfVisits, 
         @Param("password") String password );
}
