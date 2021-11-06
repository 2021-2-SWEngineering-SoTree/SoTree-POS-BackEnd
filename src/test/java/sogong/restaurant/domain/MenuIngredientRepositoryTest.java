package sogong.restaurant.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.repository.MenuIngredientRepository;
import sogong.restaurant.repository.MenuRepository;

@SpringBootTest
class MenuIngredientRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuIngredientRepository menuIngredientRepository;

    @AfterEach
    public void afterEach(){
        menuIngredientRepository.deleteAll();
        menuRepository.deleteAll();
    }

    @Test
    void menuIngredientCreateTest(){

        Menu menu= new Menu();

        menu.setMenuName("된장찌개");
        menu.setPrice(12000);
        menu.setMenuCategory("식사");

        menuRepository.save(menu);

        Menu insertMenu = menuRepository.findMenuByMenuName("된장찌개").get();

        MenuIngredient menuIngredient = new MenuIngredient();
        menuIngredient.setMenu(insertMenu);
        menuIngredient.setIngredientName("된장");
        menuIngredient.setCount(12);

        menuIngredientRepository.save(menuIngredient);

    }

}