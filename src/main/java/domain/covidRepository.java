package domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface covidRepository extends JpaRepository<CovidData, Long> {
   // insert into db
   String insert = "INSERT INTO covid_data VALUES (null, :state, :positive, "
         + ":negative, :currentHospital, :totalHospital, :currentICU, "
         + ":totalICU, :currentVent, :totalVent, :recovered, :deaths)";
   
   // Select by state
   String selectState = "SELECT * FROM covid_data WHERE state=:state";
   
   // Select all
   String selectAll = "SELECT * FROM covid_data";
   
   @Query(value=selectState, nativeQuery=true)
   CovidData findByState(@Param("state") String state);
   
   @Query(value=insert, nativeQuery=true)
   static
   void insertDataPoint(
         @Param("state") String name,
         @Param("positive") long positive,
         @Param("negative") long negative,
         @Param("currentHospital") long currentlyHospitalized,
         @Param("totalHospital") long totalHospitalized,
         @Param("currentICU") long currentICU,
         @Param("totalICU") long totalICU,
         @Param("currentVent") long currentVentilated,
         @Param("totalVent") long totalVentilated,
         @Param("recovered") long recovered,
         @Param("deaths") long deaths
         ) {
   }
   
   @Query(value=selectAll, nativeQuery=true)
   List<CovidData> findAll();
}
