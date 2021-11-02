package sogong.restaurant.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MenuIngredientRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuIngredientRepository menuIngredientRepository;


    @Test
    void menuIngredientCreateTest(){

        Menu menu= new Menu();

        menu.setMenuName("된장찌개");
        menu.setPrice(12000);
        menu.setMenuCategory("식사");

        menuRepository.save(menu);

        Menu insertMenu = menuRepository.findMenuByMenuName("된장찌개");

        MenuIngredient menuIngredient = new MenuIngredient();
        menuIngredient.setMenu(insertMenu);
        menuIngredient.setIngredientName("된장");
        menuIngredient.setCount(12);

        menuIngredientRepository.save(menuIngredient);

    }

}