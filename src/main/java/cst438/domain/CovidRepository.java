package cst438.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CovidRepository extends JpaRepository<CovidData, Long> {
   // insert into db
   // maybe won't be used? (4/7 Ivan)
   String insert = "INSERT INTO covid_data VALUES (null, :state, :positive, "
         + ":negative, :currentHospital, :totalHospital, :currentICU, "
         + ":totalICU, :currentVent, :totalVent, :recovered, :deaths)";
   
   // insert into db with date
   String insertHistorical = "INSERT INTO covid_data (id, date, state, "
         + "tested_positive, tested_negative, currently_hospitalized, "
         + "total_hospitalized, currenticucount, totalicucount, "
         + "currently_on_ventilator, total_ventilated, recovered, deaths) VALUES "
         + "(default, :date, :state, :positive, :negative, :currentHospital, "
         + ":totalHospital, :currentICU, :totalICU, :currentVent, :totalVent, "
         + ":recovered, :deaths)";
   
   // Select by state
   String selectState = "SELECT * FROM covid_data WHERE state=:state";
   
   // Select all by date
   String selectDate = "SELECT * FROM covid_data WHERE date=:date";
   
   // Select all
   String selectAll = "SELECT * FROM covid_data";
   
   @Query(value=selectState, nativeQuery=true)
   CovidData findByState(@Param("state") String state);
   
   @Modifying
   @Transactional
   @Query(value=insert, nativeQuery=true)
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
         );
   
   @Modifying
   @Transactional
   @Query(value=insertHistorical, nativeQuery=true)
   void insertHistoricalDataPoint(
         @Param("date") long date,
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
         );
   
   @Query(value=selectDate, nativeQuery=true)
   List<CovidData> findByDate(@Param("date") long date);
   
   @Query(value=selectAll, nativeQuery=true)
   List<CovidData> findAll();
   
   
}
