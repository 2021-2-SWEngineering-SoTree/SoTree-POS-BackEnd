package sogong.restaurant.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Test
    void testCOmmute(){
        String date = "2021-11-15 07:50";

        System.out.println(date.substring(0,10));


    }
    @Test
    void testMinus() throws Exception{

        String startTime = "2021-11-11 12:00";
        String endTime = "2021-11-11 14:15";

        Date format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
        Date format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);

        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
        long diffMin = (format2.getTime() - format1.getTime()) / 60000;

        System.out.println("diffMin = " + diffMin);
    }

}