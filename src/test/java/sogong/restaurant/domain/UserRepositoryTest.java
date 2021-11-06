package sogong.restaurant.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.UserRepository;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    void findTest(){
        // Given
        String pw = "1234";

        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword(pw);
        user.setLoginId("test");
        user.setPhoneNumber("010-9283-9712");

        //When
        userRepository.save(user);
        User findUser  = userRepository.findByLoginId("test").get();

        //Then
        Assertions.assertThat(findUser.getPassword().equals(pw));
    }

}