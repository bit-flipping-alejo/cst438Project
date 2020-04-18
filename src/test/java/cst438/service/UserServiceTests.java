package cst438.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cst438.domain.Model.User;
import cst438.domain.Repository.UserRepository;
import cst438.services.UserService;

@SpringBootTest
public class UserServiceTests {
	
	//class to be tested
	private UserService userService;
	
	@Mock
	private UserRepository userRepo;
	
	@BeforeEach
	public void setUpEach() {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(userRepo);
	}
	
	@Test
	public void testUser() {
		
		User testUser = new User();
		testUser.setName("john");
		testUser.setPassword("pass");
		testUser.setState("CA");
		testUser.setNumberOfVisits(2);
		
		given(userRepo.findByNameAndPassword("john", "pass")).willReturn(testUser);
		
		User actual = userService.findByNameAndPassword("john", "pass");
		
		assertThat(actual.getName()).isEqualTo("john");
		assertThat(actual.getState()).isEqualTo("CA");
		assertThat(actual.getPassword()).isEqualTo("pass");
		assertThat(actual.getNumberOfVisits()).isEqualTo(2);
		
	}

}
