package cst438.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cst438.domain.Model.States;
import cst438.domain.Repository.StatesRepository;
import cst438.services.StatesService;

@SpringBootTest
public class StateServiceTests {
	
	//class to be tested
	private StatesService statesService;
	
	@Mock
	private StatesRepository statesRepo;
	
	@BeforeEach
	public void setUpEach() {
		MockitoAnnotations.initMocks(this);
		statesService = new StatesService(statesRepo);		
	}
	
	@Test
	public void testFetchByState() {
		States testState = new States(1, "California", "CA");
		
		given(statesRepo.findByStateCode("CA")).willReturn(testState);
		
		States actual = statesService.fetchByState("CA");
		
		assertThat(actual.getState()).isEqualTo("California");
		assertThat(actual.getState_code()).isEqualTo("CA");
		
	}
}
