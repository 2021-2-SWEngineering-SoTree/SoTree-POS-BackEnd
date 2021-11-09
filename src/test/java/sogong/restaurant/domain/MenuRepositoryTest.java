package sogong.restaurant.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.MenuRepository;
import sogong.restaurant.repository.UserRepository;

@SpringBootTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;
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
    void menuSaveTest(){
        Menu menu = new Menu();
        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");
        menu.setManager(managerRepository.findByStoreName("테스트가게").get());

        menuRepository.save(menu);

    }
}