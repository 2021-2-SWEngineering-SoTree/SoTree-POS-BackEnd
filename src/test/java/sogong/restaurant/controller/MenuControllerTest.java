package sogong.restaurant.controller;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.User;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.MenuRepository;
import sogong.restaurant.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuControllerTest {

    @Autowired private MenuController menuController;
    @Autowired private MenuRepository menuRepository;


    @Autowired private UserRepository userRepository;
    @Autowired private ManagerRepository managerRepository;

    @BeforeEach
    public void makeManager(){

        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("testAdmin");
        user.setPhoneNumber("010-9283-9712");
        userRepository.save(user);
        Manager manager = new Manager();
        manager.setUser(user);
        manager.setStoreName("테스트가게");
        manager.setBranchPhoneNumber("02-123-1234");
        managerRepository.save(manager);
    }

    @AfterEach
    public void afterEach(){
        menuRepository.deleteAll();

        managerRepository.deleteAll(); userRepository.deleteAll();
    }


    @Test
    void testgetMenu(){
        Menu menu = new Menu();

        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");
        menu.setManager(managerRepository.findByStoreName("테스트가게").get());

        menuRepository.save(menu);
        //asdfa


    }

    @Test
    void testisPresent(){


        Menu menu = new Menu();

        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");
        menu.setManager(managerRepository.findByStoreName("테스트가게").get());

        menuRepository.save(menu);

        Map<String,String> param = new HashMap<>();
        param.put("menuName",menu.getMenuName());
        param.put("managerId",String.valueOf(managerRepository.findByStoreName("테스트가게").get().getId()));

        Assertions.assertThat(menuController.validName(param)).isEqualTo(false);

    }

}