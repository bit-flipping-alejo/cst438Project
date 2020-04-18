package cst438.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.ArrayList;

import cst438.domain.Model.Coctail;
import cst438.services.CoctailAPIService;
import cst438.services.CoctailService;


@SpringBootTest
public class CoctailServiceTests {
   
   // class to be tested
   private CoctailService coctailServ;
   
   @Mock
   public CoctailAPIService coctailAPIServ;
   
   
   @BeforeEach
   public void setUpEach() {
      MockitoAnnotations.initMocks(this);
      coctailServ = new CoctailService(coctailAPIServ);
   }
   
   /*Coctail API Service is a wrapper for API calls
    * not much to test here*/
   @Test
   public void testCoctailService() {
      
      ArrayList<String> testIngredients = new ArrayList<String>();
      testIngredients.add("Awesome sauce");
      testIngredients.add("Shaken bacon");
      testIngredients.add("Cool beans");
      
      Coctail testCoctail = new Coctail("boozy drink", "www.boozypic.location", "Shaken not stirred", testIngredients );
      
      given(coctailAPIServ.getARandomCoctail()).willReturn(testCoctail);
      
      // since its an API call, this is a feed thru
      Coctail actualCoctail = coctailServ.getARandomCoctail();
      
      assertThat(actualCoctail).isExactlyInstanceOf(Coctail.class);
      assertThat(actualCoctail.getName()).isEqualTo("boozy drink");
      assertThat(actualCoctail.getPicUrl()).isEqualTo("www.boozypic.location");
      assertThat(actualCoctail.getInstructions()).isEqualTo("Shaken not stirred");
      
      assertThat(actualCoctail.getIngredients()).isEqualTo(testIngredients);
      
   }
   
   
   
   
}











