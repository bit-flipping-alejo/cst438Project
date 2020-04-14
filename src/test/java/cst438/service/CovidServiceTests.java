package cst438.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.context.SpringBootTest;

import cst438.domain.Model.CovidData;
import cst438.domain.Repository.CovidNationalRepository;
import cst438.domain.Repository.CovidRepository;
import cst438.services.CovidService;
import cst438.services.CovidAPIService;

// TODO: one final test in this file IA 4/13
@SpringBootTest
public class CovidServiceTests {
   // class to be tested
   private CovidService covidService;
   
   @Mock
   private CovidAPIService covidAPIService;
   @Mock
   private CovidRepository covidRepo;
   @Mock
   private CovidNationalRepository covidNationalRepo;
   
   @BeforeEach
   public void setUpEach() {
      MockitoAnnotations.initMocks(this);
      covidService = new CovidService(covidRepo, covidAPIService);
   }
   
   @Test
   public void getStateAndDateTest() {
      List<CovidData> testCovidData = Arrays.asList(new CovidData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByStateAndDate(
            "CA", LocalDate.now().minusDays(2))).willReturn(testCovidData);
      
      List<CovidData> actual;
      
      actual = covidService.fetchByStateAndDate("CA", "2", "asc");
      
      assertThat(actual.get(0).getState()).isEqualTo("CA");
      assertThat(actual.get(0).getTestedPositive()).isEqualTo(1234);
      assertThat(actual.get(0).getTestedNegative()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyHospitalized()).isEqualTo(333);
      assertThat(actual.get(0).getTotalHospitalized()).isEqualTo(222);
      assertThat(actual.get(0).getCurrentICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getTotalICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyOnVentilator()).isEqualTo(12345);
      assertThat(actual.get(0).getTotalVentilated()).isEqualTo(123);
      assertThat(actual.get(0).getRecovered()).isEqualTo(22);
      assertThat(actual.get(0).getDeaths()).isEqualTo(5);
   }
   
   @Test 
   public void getCurrentDayStates() {
      List<CovidData> testCovidData = Arrays.asList(new CovidData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findCurrent()).willReturn(testCovidData);
      
      List<CovidData> actual;
      
      actual = covidService.fetchCurrentDayStates();
      
      assertThat(actual.get(0).getState()).isEqualTo("CA");
      assertThat(actual.get(0).getTestedPositive()).isEqualTo(1234);
      assertThat(actual.get(0).getTestedNegative()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyHospitalized()).isEqualTo(333);
      assertThat(actual.get(0).getTotalHospitalized()).isEqualTo(222);
      assertThat(actual.get(0).getCurrentICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getTotalICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyOnVentilator()).isEqualTo(12345);
      assertThat(actual.get(0).getTotalVentilated()).isEqualTo(123);
      assertThat(actual.get(0).getRecovered()).isEqualTo(22);
      assertThat(actual.get(0).getDeaths()).isEqualTo(5);
   }
   
   @Test
   public void getCurrentNationalStats() {
      
   }
   
   @Test
   public void getDate() {
      List<CovidData> testCovidData = Arrays.asList(new CovidData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByDate(
            LocalDate.now().minusDays(2))).willReturn(testCovidData);
      
      List<CovidData> actual;
      
      actual = covidService.fetchByDate(LocalDate.now().minusDays(2));
      
      assertThat(actual.get(0).getState()).isEqualTo("CA");
      assertThat(actual.get(0).getTestedPositive()).isEqualTo(1234);
      assertThat(actual.get(0).getTestedNegative()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyHospitalized()).isEqualTo(333);
      assertThat(actual.get(0).getTotalHospitalized()).isEqualTo(222);
      assertThat(actual.get(0).getCurrentICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getTotalICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyOnVentilator()).isEqualTo(12345);
      assertThat(actual.get(0).getTotalVentilated()).isEqualTo(123);
      assertThat(actual.get(0).getRecovered()).isEqualTo(22);
      assertThat(actual.get(0).getDeaths()).isEqualTo(5);
   }
   
   @Test
   public void getAll() {
      List<CovidData> testCovidData = Arrays.asList(new CovidData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findAll()).willReturn(testCovidData);
      
      List<CovidData> actual;
      
      actual = covidService.fetchAll();
      
      assertThat(actual.get(0).getState()).isEqualTo("CA");
      assertThat(actual.get(0).getTestedPositive()).isEqualTo(1234);
      assertThat(actual.get(0).getTestedNegative()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyHospitalized()).isEqualTo(333);
      assertThat(actual.get(0).getTotalHospitalized()).isEqualTo(222);
      assertThat(actual.get(0).getCurrentICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getTotalICUCount()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyOnVentilator()).isEqualTo(12345);
      assertThat(actual.get(0).getTotalVentilated()).isEqualTo(123);
      assertThat(actual.get(0).getRecovered()).isEqualTo(22);
      assertThat(actual.get(0).getDeaths()).isEqualTo(5);
   }
}
