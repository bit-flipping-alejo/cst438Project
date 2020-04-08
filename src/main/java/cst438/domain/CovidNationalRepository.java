package cst438.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CovidNationalRepository 
extends JpaRepository<CovidNationalData, Long>{
   // insert into db with date
   String insertHistorical = "INSERT INTO covid_national_data (id, date, "
         + "tested_positive, tested_negative, currently_hospitalized, "
         + "total_hospitalized, currenticucount, totalicucount, "
         + "currently_on_ventilator, total_ventilated, recovered, deaths) VALUES "
         + "(default, :date, :positive, :negative, :currentHospital, "
         + ":totalHospital, :currentICU, :totalICU, :currentVent, :totalVent, "
         + ":recovered, :deaths)";

   // Select by date
   String selectDate = "SELECT * FROM covid_national_data WHERE date=:date";
   
   // Select all data points
   String selectAll = "SELECT * FROM covid_national_data";
   
   @Query(value=selectDate, nativeQuery=true)
   CovidNationalData findByDate(@Param("date") long date);
   
   @Query(value=selectAll, nativeQuery=true)
   List<CovidNationalData> findAll();
   
   @Modifying
   @Transactional
   @Query(value=insertHistorical, nativeQuery=true)
   void insertHistoricalDataPoint(
         @Param("date") long date,
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
