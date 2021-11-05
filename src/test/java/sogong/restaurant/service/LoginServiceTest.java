package sogong.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.domain.User;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Test
    void loginTest(){

        User user = loginService.login("test","1234");

        System.out.println("user.getUserName() = " + user.getUserName());

    }

}