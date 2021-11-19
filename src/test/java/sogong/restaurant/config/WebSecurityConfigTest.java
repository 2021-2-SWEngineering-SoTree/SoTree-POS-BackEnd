package sogong.restaurant.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncode(){
        String raw="1234";

        System.out.println("encoded password : "+passwordEncoder.encode(raw));

        Assertions.assertThat(passwordEncoder.matches(raw,passwordEncoder.encode(raw))).isEqualTo(true);
    }

    @Test
    void passwordTest(){

        String raw = "asdf";
        System.out.println("encoded : "+passwordEncoder.encode(raw));

    }

}