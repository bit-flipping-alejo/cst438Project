package cst438.domain.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cst438.domain.Model.States;

@Repository
public interface StatesRepository extends JpaRepository<States, Long>{

   String selectState = "SELECT * FROM states WHERE state_code = :code";
   @Query(value=selectState, nativeQuery=true)
   States findByStateCode(@Param("code") String code);
}
