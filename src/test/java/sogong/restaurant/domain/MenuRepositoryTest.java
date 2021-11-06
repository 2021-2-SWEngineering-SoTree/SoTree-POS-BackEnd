package sogong.restaurant.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.MenuRepository;

@SpringBootTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @AfterEach
    public void afterEach(){
        menuRepository.deleteAll();
    }

    @Test
    void menuSaveTest(){
        Menu menu = new Menu();
        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");

        menuRepository.save(menu);

    }
}