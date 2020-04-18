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

import cst438.domain.Helper.NationalDisplayHelper;
import cst438.domain.Model.CovidStateData;
import cst438.domain.Model.CovidNationalData;
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
      covidService = new CovidService(
            covidRepo, 
            covidAPIService,
            covidNationalRepo);
   }
   
   @Test
   public void testStateAndDateTestAscWithDate() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByStateAndDate(
            "CA", LocalDate.now().minusDays(2))).willReturn(
                  testCovidData);
      
       List<CovidStateData> actual = 
             covidService.fetchByStateAndDate("CA", "2", "asc");
      
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
   public void testStateAndDateTestDescWithDate() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByStateAndDateDesc(
            "CA", LocalDate.now().minusDays(2))).willReturn(
                  testCovidData);
      
       List<CovidStateData> actual = 
             covidService.fetchByStateAndDate("CA", "2", "desc");
      
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
   public void testStateAndDateTestAscAll() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByState("CA")).willReturn(testCovidData);
      
       List<CovidStateData> actual = 
             covidService.fetchByStateAndDate("CA", "all", "asc");
      
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
   public void testStateAndDateTestDescAll() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByStateDesc("CA")).willReturn(testCovidData);
      
       List<CovidStateData> actual = 
             covidService.fetchByStateAndDate("CA", "all", "desc");
      
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
   public void testCurrentDayStates() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidAPIService.pullCurrentStateData()).willReturn(
            testCovidData);
      
      List<CovidStateData> actual;
      
      actual = covidService.fetchCurrentStateStats();
      
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
   public void testState() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByState("CA")).willReturn(testCovidData);  
      
      List<CovidStateData> actual = 
            covidService.fetchByState("CA");
     
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
   public void testCurrentNationalStatsPosDeadInc() {
      CovidNationalData testAPIData = new CovidNationalData(
            1, LocalDate.parse("2020-04-02"), 50,
            1234, 40, 333, 222, 0, 0, 12345, 123, 22, 5);
      
      given(covidAPIService.pullCurrentNationalStats()).willReturn(
            testAPIData);
      
      given(covidNationalRepo.findByRecentDate()).willReturn(
            new CovidNationalData(
                  10, LocalDate.parse("2020-04-02"), 50, 
                  1000, 20, 10, 10, 10, 10, 5, 5, 50, 30));
      
      NationalDisplayHelper actual = 
            covidService.fetchCurrentNationalStats();
      
      assertThat(actual.getDate()).isEqualTo("April 2, 2020");
      assertThat(actual.getPositive()).isEqualTo("1,234");
      assertThat(actual.getDead()).isEqualTo("5");
      assertThat(actual.getPositiveChange()).isEqualTo("+234");
      assertThat(actual.getDeadChange()).isEqualTo("-25");
      assertThat(actual.isPositiveIncrease()).isEqualTo(true);
      assertThat(actual.isDeadIncrease()).isEqualTo(false);
   }
   
   @Test
   public void testCurrentNationalStatsPosDec() {
      CovidNationalData testAPIData = new CovidNationalData(
            1, LocalDate.parse("2020-04-02"), 50,
            1000, 40, 333, 222, 0, 0, 12345, 123, 22, 5);
      
      given(covidAPIService.pullCurrentNationalStats()).willReturn(
            testAPIData);
      
      given(covidNationalRepo.findByRecentDate()).willReturn(
            new CovidNationalData(
                  10, LocalDate.parse("2020-04-02"), 50, 
                  1234, 20, 10, 10, 10, 10, 5, 5, 50, 30));
      
      NationalDisplayHelper actual = 
            covidService.fetchCurrentNationalStats();
      
      assertThat(actual.getDate()).isEqualTo("April 2, 2020");
      assertThat(actual.getPositive()).isEqualTo("1,000");
      assertThat(actual.getDead()).isEqualTo("5");
      assertThat(actual.getPositiveChange()).isEqualTo("-234");
      assertThat(actual.getDeadChange()).isEqualTo("-25");
      assertThat(actual.isPositiveIncrease()).isEqualTo(false);
      assertThat(actual.isDeadIncrease()).isEqualTo(false);
   }
   
   @Test
   public void testCurrentNationalStatsPosDeadEqual() {
      CovidNationalData testAPIData = new CovidNationalData(
            1, LocalDate.parse("2020-04-02"), 50,
            1000, 40, 333, 222, 0, 0, 12345, 123, 22, 5);
      
      given(covidAPIService.pullCurrentNationalStats()).willReturn(
            testAPIData);
      
      given(covidNationalRepo.findByRecentDate()).willReturn(
            new CovidNationalData(
                  10, LocalDate.parse("2020-04-02"), 50, 
                  1000, 20, 10, 10, 10, 10, 5, 5, 50, 5));
      
      NationalDisplayHelper actual = 
            covidService.fetchCurrentNationalStats();
      
      assertThat(actual.getDate()).isEqualTo("April 2, 2020");
      assertThat(actual.getPositive()).isEqualTo("1,000");
      assertThat(actual.getDead()).isEqualTo("5");
      assertThat(actual.getPositiveChange()).isEqualTo(null);
      assertThat(actual.getDeadChange()).isEqualTo(null);
      assertThat(actual.isPositiveIncrease()).isEqualTo(false);
      assertThat(actual.isDeadIncrease()).isEqualTo(false);
   }
   
   @Test
   public void testCurrentNationalStatsDeadDec() {
      CovidNationalData testAPIData = new CovidNationalData(
            1, LocalDate.parse("2020-04-02"), 50,
            1000, 40, 333, 222, 0, 0, 12345, 123, 22, 50);
      
      given(covidAPIService.pullCurrentNationalStats()).willReturn(
            testAPIData);
      
      given(covidNationalRepo.findByRecentDate()).willReturn(
            new CovidNationalData(
                  10, LocalDate.parse("2020-04-02"), 50, 
                  1000, 20, 10, 10, 10, 10, 5, 5, 50, 10));
      
      NationalDisplayHelper actual = 
            covidService.fetchCurrentNationalStats();
      
      assertThat(actual.getDate()).isEqualTo("April 2, 2020");
      assertThat(actual.getPositive()).isEqualTo("1,000");
      assertThat(actual.getDead()).isEqualTo("50");
      assertThat(actual.getPositiveChange()).isEqualTo(null);
      assertThat(actual.getDeadChange()).isEqualTo("+40");
      assertThat(actual.isPositiveIncrease()).isEqualTo(false);
      assertThat(actual.isDeadIncrease()).isEqualTo(true);
   }
   
   @Test
   public void testCurrentNationalDataRaw() {
      given(covidNationalRepo.findByRecentDate()).willReturn(
            new CovidNationalData(
                  10, LocalDate.parse("2020-04-02"), 50, 
                  1000, 20, 10, 10, 10, 10, 5, 5, 50, 10));
      
      CovidNationalData actual = 
            covidService.fetchCurrentNationalData();
      
      assertThat(actual.getStates_affected()).isEqualTo(50);
      assertThat(actual.getTestedPositive()).isEqualTo(1000);
      assertThat(actual.getTestedNegative()).isEqualTo(0);
      assertThat(actual.getCurrentlyHospitalized()).isEqualTo(10);
      assertThat(actual.getTotalHospitalized()).isEqualTo(10);
      assertThat(actual.getCurrentICUCount()).isEqualTo(10);
      assertThat(actual.getTotalICUCount()).isEqualTo(10);
      assertThat(actual.getCurrentlyOnVentilator()).isEqualTo(5);
      assertThat(actual.getTotalVentilated()).isEqualTo(5);
      assertThat(actual.getRecovered()).isEqualTo(50);
      assertThat(actual.getDeaths()).isEqualTo(10);
   }
   
   @Test
   public void testAllNationalDataRaw() {
      given(covidNationalRepo.findAll()).willReturn(
            Arrays.asList(new CovidNationalData(
                  10, LocalDate.parse("2020-04-02"), 50, 
                  1000, 20, 10, 10, 10, 10, 5, 5, 50, 10)));
      
      List<CovidNationalData> actual = 
            covidService.fetchAllNationalData();
      
      assertThat(actual.get(0).getStates_affected()).isEqualTo(50);
      assertThat(actual.get(0).getTestedPositive()).isEqualTo(1000);
      assertThat(actual.get(0).getTestedNegative()).isEqualTo(0);
      assertThat(actual.get(0).getCurrentlyHospitalized()).isEqualTo(10);
      assertThat(actual.get(0).getTotalHospitalized()).isEqualTo(10);
      assertThat(actual.get(0).getCurrentICUCount()).isEqualTo(10);
      assertThat(actual.get(0).getTotalICUCount()).isEqualTo(10);
      assertThat(actual.get(0).getCurrentlyOnVentilator()).isEqualTo(5);
      assertThat(actual.get(0).getTotalVentilated()).isEqualTo(5);
      assertThat(actual.get(0).getRecovered()).isEqualTo(50);
      assertThat(actual.get(0).getDeaths()).isEqualTo(10);
   }
   
   @Test
   public void testDate() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findByDate(
            LocalDate.now().minusDays(2))).willReturn(testCovidData);
      
      List<CovidStateData> actual;
      
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
   public void testAll() {
      List<CovidStateData> testCovidData = Arrays.asList(new CovidStateData(
            1, "CA", LocalDate.now().minusDays(2),
            1234, 0, 333, 222, 0, 0, 12345, 123, 22, 5));
      
      given(covidRepo.findAll()).willReturn(testCovidData);
      
      List<CovidStateData> actual;
      
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
