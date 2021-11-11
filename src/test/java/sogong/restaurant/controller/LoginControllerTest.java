package sogong.restaurant.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.User;
import sogong.restaurant.repository.EmployeeRepository;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.UserRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginControllerTest {

    @Autowired
    LoginController loginController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    EmployeeRepository employeeRepository;


    @AfterEach public void afterEach(){
        managerRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void isValidTest(){

        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("test");
        user.setPhoneNumber("010-9283-9712");
        user.setRoles(Collections.singletonList("ROLE_USER"));
        userRepository.save(user);

        Manager manager= new Manager();
        manager.setBranchPhoneNumber("02-123-1234");
        manager.setStoreName("테스트가게");
        manager.setUser(user);

        managerRepository.save(manager);

        Assertions.assertThat(loginController.validName(user.getUsername())).isEqualTo(false);

    }

}