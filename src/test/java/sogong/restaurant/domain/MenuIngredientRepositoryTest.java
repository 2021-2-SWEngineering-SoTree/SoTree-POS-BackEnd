package sogong.restaurant.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.MenuIngredientRepository;
import sogong.restaurant.repository.MenuRepository;
import sogong.restaurant.repository.UserRepository;

@SpringBootTest
class MenuIngredientRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuIngredientRepository menuIngredientRepository;

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
        menuIngredientRepository.deleteAll();
        menuRepository.deleteAll();
        managerRepository.deleteAll(); userRepository.deleteAll();
    }

    @Test
    void menuIngredientCreateTest(){

        Menu menu= new Menu();

        menu.setMenuName("된장찌개");
        menu.setPrice(12000);
        menu.setMenuCategory("식사");
        menu.setManager(managerRepository.findByStoreName("테스트가게").get());


        menuRepository.save(menu);

        Menu insertMenu = menuRepository.findMenuByMenuName("된장찌개").get();

        MenuIngredient menuIngredient = new MenuIngredient();
        menuIngredient.setMenu(insertMenu);
        menuIngredient.setIngredientName("된장");
        menuIngredient.setCount(12);

        menuIngredientRepository.save(menuIngredient);

    }

}