package sogong.restaurant.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.UserRepository;

@SpringBootTest
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }
    @Test
    void makeTestUser(){
        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("test");
        user.setPhoneNumber("010-9283-9712");

        userRepository.save(user);
    }

}