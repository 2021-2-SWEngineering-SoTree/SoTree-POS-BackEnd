package sogong.restaurant.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogong.restaurant.VO.menuVO;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.repository.MenuIngredientRepository;
import sogong.restaurant.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MenuServiceIntegrationTest {

    @Autowired private MenuRepository menuRepository;
    @Autowired private MenuService menuService;
    @Autowired private MenuIngredientRepository menuIngredientRepository;
    @Autowired private MenuIngredientService menuIngredientService;

    @AfterEach
    public void afterEach(){
        menuIngredientRepository.deleteAll();
        menuRepository.deleteAll();
    }

    @Test
    public void testAddingMenuAndIngredients() throws Exception {
        //Given
//        MenuIngredient mi1 = new MenuIngredient();
//        mi1.setIngredientName("김치");
//        mi1.setCount(3);
//
//        MenuIngredient mi2 = new MenuIngredient();
//        mi2.setIngredientName("두부");
//        mi2.setCount(7);
//
//        List<MenuIngredient> listMI= new ArrayList<>();
//        listMI.add(mi1);
//        listMI.add(mi2);
//
//        menuVO menuvo = menuVO.builder()
//                .menuName("테스트메뉴")
//                .price(10000)
//                .menuCategory("테스트 카테고리")
//                .menuIngredientLists(listMI).build();
//
//        Menu menu = new Menu();
//        menu.setMenuName(menuvo.getMenuName());
//        menu.setMenuCategory(menuvo.getMenuCategory());
//        menu.setPrice(menuvo.getPrice());
//
//        for(MenuIngredient menuIngredient:menuvo.getMenuIngredientLists()){
//            menuIngredient.setMenu(menu);
//            menuIngredientService.addMenuIngredient(menuIngredient);
//            System.out.println("menuIngredient = " + menuIngredient.getIngredientName());
//        }
        Menu menu = new Menu();
        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");

        //When
        Long saveId = menuService.addMenu(menu);

        //Then
        Menu findMenu = menuRepository.findById(saveId).get();

        assertThat(menu).isEqualTo(findMenu);
    }

    @Test
    public void testValidateMenu() throws Exception {
        //Given
        Menu menu = new Menu();
        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");

        Menu menu2 = new Menu();
        menu2.setMenuName("테스트메뉴");
        menu2.setPrice(10000);
        menu2.setMenuCategory("테스트 카테고리");

        //When
        menuService.addMenu(menu);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> menuService.addMenu(menu2));  //예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 메뉴입니다.");
    }

    @Test
    public void testUpdateMenu() throws Exception {
        //Given
        Menu menu = new Menu();
        menu.setMenuName("테스트메뉴");
        menu.setPrice(10000);
        menu.setMenuCategory("테스트 카테고리");
        menuService.addMenu(menu);

        //When
        menu.setMenuName("update 테스트");
        Long saveId = menuService.saveMenu(menu);

        //Then
        Menu findMenu = menuRepository.findById(saveId).get();
        assertThat(findMenu.getMenuName()).isEqualTo("update 테스트");
    }

    @Test
    public void testDeleteMenu() throws Exception {
        //Given
        MenuIngredient mi1 = new MenuIngredient();
        mi1.setIngredientName("김치");
        mi1.setCount(3);

        MenuIngredient mi2 = new MenuIngredient();
        mi2.setIngredientName("두부");
        mi2.setCount(7);

        List<MenuIngredient> listMI= new ArrayList<>();
        listMI.add(mi1);
        listMI.add(mi2);

        menuVO menuvo = menuVO.builder()
                        .menuName("테스트메뉴2")
                                .price(10000)
                                        .menuCategory("테스트 카테고리")
                                                .menuIngredientLists(listMI).build();


        Menu menu = new Menu();
        menu.setMenuName(menuvo.getMenuName());
        menu.setMenuCategory(menuvo.getMenuCategory());
        menu.setPrice(menuvo.getPrice());

        Long saveId = menuService.addMenu(menu);
        System.out.println(saveId);

        for(MenuIngredient menuIngredient:menuvo.getMenuIngredientLists()){
            menuIngredient.setMenu(menu);
            menuIngredientService.addMenuIngredient(menuIngredient);
            System.out.println("menuIngredient = " + menuIngredient.getIngredientName());
        }


        //When
        menuService.deleteMenu(saveId);

        //Then
        assertThat(menuRepository.findById(saveId).isPresent()).isEqualTo(false);
    }

    @Test
    void createMenuIngredientTest(){

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

    @Test
    void getAllMenuTest(){

        Menu menu = new Menu();

        menu.setMenuName("된장찌개");
        menu.setPrice(12000);
        menu.setMenuCategory("식사");

        menuService.saveMenu(menu);

        List<Menu> menuList = menuService.getAllMenu();

        assertThat(1).isEqualTo(menuList.size());

    }

}
