package sogong.restaurant.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.UserRepository;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findTest(){
        User user = userRepository.findByLoginId("test");

        String pw = "1234";

        Assertions.assertThat(user.getPassword().equals(pw));

    }

}