package cst438.domain.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cst438.domain.Model.CovidNationalData;

@Repository
public interface CovidNationalRepository 
extends JpaRepository<CovidNationalData, Long>{

   String selectDate = "SELECT * FROM covid_national_data WHERE date=:date";
   @Query(value=selectDate, nativeQuery=true)
   CovidNationalData findByDate(@Param("date") long date);
   
   String selectOneByID = "SELECT * FROM covid_national_data WHERE id=:id";
   @Query(value=selectOneByID, nativeQuery=true)
   CovidNationalData findByID(@Param("id") long id);
   
   String selectAll = "SELECT * FROM covid_national_data";
   @Query(value=selectAll, nativeQuery=true)
   List<CovidNationalData> findAll();
   
   String selectRecentDate = "SELECT * FROM covid_national_data ORDER BY date"
         + " desc LIMIT 1";
   @Query(value=selectRecentDate, nativeQuery=true)
   CovidNationalData findByRecentDate();
   
   String insertHistorical = "INSERT INTO covid_national_data (id, date, "
         + "states_affected, tested_positive, tested_negative, "
         + "currently_hospitalized, total_hospitalized, currenticucount, "
         + "totalicucount, currently_on_ventilator, total_ventilated, "
         + "recovered, deaths) VALUES (default, :date, :states, :positive, "
         + ":negative, :currentHospital, :totalHospital, :currentICU, "
         + ":totalICU, :currentVent, :totalVent, :recovered, :deaths)";
   @Modifying
   @Transactional
   @Query(value=insertHistorical, nativeQuery=true)
   void insertHistoricalDataPoint(
         @Param("date") LocalDate date,
         @Param("states") long states,
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
