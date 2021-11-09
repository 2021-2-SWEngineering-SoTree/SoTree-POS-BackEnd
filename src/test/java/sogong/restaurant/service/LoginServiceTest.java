package sogong.restaurant.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.User;
import sogong.restaurant.repository.EmployeeRepository;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired private LoginService loginService;
    @Autowired private UserRepository userRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private ManagerRepository managerRepository;

    @AfterEach
    public void afterEach(){
        managerRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
    }

    /*
    @Test
    void loginTest(){
        //Given
        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("test");
        user.setPhoneNumber("010-9283-9712");

        //When
        loginService.addUser(user);

        User findUser = loginService.login("test","1234");

        assertThat(findUser.getLoginId()).isEqualTo(user.getLoginId());
        assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void validTest(){
        //Given
        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("test");
        user.setPhoneNumber("010-9283-9712");


        loginService.addUser(user);

        assertThat(loginService.isPresent(user.getLoginId())).isEqualTo(true);

    }
    */

    @Test
    void loadTest(){


        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("test");
        user.setPhoneNumber("010-9283-9712");
        user.setRoles(Collections.singletonList("ROLE_USER"));

        userRepository.save(user);

        Manager manager = new Manager();
        manager.setBranchPhoneNumber("02-123-1234");
        manager.setStoreName("테스트가게");
        managerRepository.save(manager);

        UserDetails users = loginService.loadUserByUsername("test");

        //System.out.println("user.getRoles().get(0) = " + user.getRoles().get(0));

        assertThat(users.getUsername()).isEqualTo("test");

    }
}