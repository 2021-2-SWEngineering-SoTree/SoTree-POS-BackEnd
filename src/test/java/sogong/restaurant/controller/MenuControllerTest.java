package sogong.restaurant.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.repository.MenuRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuControllerTest {

    @Autowired private MenuController menuController;
    @Autowired private MenuRepository menuRepository;

    @AfterEach
    public void afterEach(){
        menuRepository.deleteAll();
    }

    @Test
    void testisPresent(){

        Menu menu = new Menu();

        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");

        menuRepository.save(menu);

        Assertions.assertThat(menuController.validName(menu.getMenuName())).isEqualTo(false);

    }

}