package cst438.domain.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cst438.domain.Model.CovidStateData;

@Repository
public interface CovidRepository extends JpaRepository<CovidStateData, Long> {
   // Select by state
   String selectState = "SELECT * FROM covid_data WHERE state=:state";
   @Query(value=selectState, nativeQuery=true)
   List<CovidStateData> findByState(@Param("state") String state);
   
   
   // Select by state, ordered date desc
   String selectStateDesc = "SELECT * FROM covid_data WHERE state=:state"
         + " ORDER BY date DESC";
   @Query(value=selectStateDesc, nativeQuery=true)
   List<CovidStateData> findByStateDesc(@Param("state") String state);
   
   
   // Select all by date
   String selectDate = "SELECT * FROM covid_data WHERE date=:date";
   @Query(value=selectDate, nativeQuery=true)
   List<CovidStateData> findByDate(@Param("date") LocalDate date);
   
   
   // Select most current 
   String selectCurrent = "SELECT * FROM covid_data ORDER BY id, date desc "
         + "limit 56;";
   @Query(value=selectCurrent, nativeQuery=true)
   List<CovidStateData> findCurrent();
   
   // Select by state and date
   String selectStateDate = "SELECT * FROM covid_data WHERE state=:state "
         + "AND date >= :date ORDER BY date";
   @Query(value=selectStateDate, nativeQuery=true)
   List<CovidStateData> findByStateAndDate(
         @Param("state") String state,
         @Param("date") LocalDate date);
   
   
   // Select by state and date desc
   String selectStateDateDesc = "SELECT * FROM covid_data WHERE state=:state "
         + "AND date >= :date ORDER BY date desc";  
   @Query(value=selectStateDateDesc, nativeQuery=true)
   List<CovidStateData> findByStateAndDateDesc(
         @Param("state") String state,
         @Param("date") LocalDate date);
   
   
   // Select all
   String selectAll = "SELECT * FROM covid_data";
   @Query(value=selectAll, nativeQuery=true)
   List<CovidStateData> findAll();
   
   
   // Select one by ID
   String selectOneByID = "SELECT * FROM covid_data WHERE id=:id";
   @Query(value=selectOneByID, nativeQuery=true)
   CovidStateData findByID(@Param("id") long ID);
   
   
   // insert into db
   // maybe won't be used? (4/7 Ivan)
   String insert = "INSERT INTO covid_data VALUES (null, :state, :positive, "
         + ":negative, :currentHospital, :totalHospital, :currentICU, "
         + ":totalICU, :currentVent, :totalVent, :recovered, :deaths)";
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
   
   
   // insert into db with date
   String insertHistorical = "INSERT INTO covid_data (id, date, state, "
         + "tested_positive, tested_negative, currently_hospitalized, "
         + "total_hospitalized, currenticucount, totalicucount, "
         + "currently_on_ventilator, total_ventilated, recovered, deaths) VALUES "
         + "(default, :date, :state, :positive, :negative, :currentHospital, "
         + ":totalHospital, :currentICU, :totalICU, :currentVent, :totalVent, "
         + ":recovered, :deaths)";
   @Modifying
   @Transactional
   @Query(value=insertHistorical, nativeQuery=true)
   void insertHistoricalDataPoint(
         @Param("date") LocalDate date,
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
}
