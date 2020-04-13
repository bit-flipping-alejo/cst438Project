package cst438.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepository extends JpaRepository<States, Long>{
   // select state by code
   String selectState = "SELECT * FROM states WHERE state_code = :code";
   
   // select all
   String selectAll = "SELECT * FROM states";
   
   @Query(value=selectState, nativeQuery=true)
   States findByStateCode(@Param("code") String code);
   
   @Query(value=selectAll, nativeQuery=true)
   List<States> findAll();
}
