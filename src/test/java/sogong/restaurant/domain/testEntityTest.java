package sogong.restaurant.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class testEntityTest {

    @Autowired
    private sogong.restaurant.repository.testEntityRepository testEntityRepository;

    @Test
    void create(){

        testEntity t= new testEntity();
        t.setName("test");
        t.setPw("ass");
        testEntityRepository.save(t);
    }

}