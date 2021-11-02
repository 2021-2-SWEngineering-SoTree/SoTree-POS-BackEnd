package sogong.restaurant.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class testEntityTest {

    @Autowired
    private testEntityRepository testEntityRepository;

    @Test
    void create(){

        testEntity t= new testEntity();
        t.setName("test");
        t.setPw("ass");
        testEntityRepository.save(t);
    }

}