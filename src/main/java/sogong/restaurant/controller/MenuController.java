package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.repository.MenuIngredientRepository;
import sogong.restaurant.VO.menuVO;
import sogong.restaurant.service.MenuIngredientService;
import sogong.restaurant.service.MenuService;

@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;
    private MenuIngredientService menuIngredientService;

    @Autowired
    public MenuController(MenuService menuService, MenuIngredientService menuIngredientService){
        this.menuService = menuService;
        this.menuIngredientService = menuIngredientService;
    }


    @PostMapping("/menu/add")
    public String addMenu(@RequestBody menuVO mvo){

        /*
    {

        "menuName": "김치찌개232",
        "price":15000,
        "menuCategory":"식사",
        "menuIngredientLists":[{
            "ingredientName":"김치",
            "count":3
        },
        {
            "ingredientName":"두부",
            "count":7
        }]
    }
    */
        //System.out.println("menu = " + menu.getMenuName());

        // Menu Refactoring
        Menu menu = new Menu();

        System.out.println("mvo.getMenuName() = " + mvo.getMenuName());

        menu.setMenuName(mvo.getMenuName());
        menu.setMenuCategory(mvo.getMenuCategory());
        menu.setPrice(mvo.getPrice());
        menuService.addMenu(menu);


        for(MenuIngredient menuIngredient:mvo.getMenuIngredientLists()){
            menuIngredient.setMenu(menu);
            menuIngredientService.addMenuIngredient(menuIngredient);
            System.out.println("menuIngredient = " + menuIngredient.getIngredientName());
        }

        return "OK";
    }


    @PutMapping("/menu/{id}")
    public String updateMenu(@RequestBody menuVO mvo){
        Menu menu = menuService.getOneMenu(mvo.getMenuName()).get();

        menu.setMenuName(mvo.getMenuName());
        menu.setMenuCategory(mvo.getMenuCategory());
        menu.setPrice(mvo.getPrice());

        menuService.saveMenu(menu);
        return "redirect:/";
    }

    @DeleteMapping("/menu/{id}")
    public String delete(@RequestBody menuVO mvo) {
        for(MenuIngredient menuIngredient:mvo.getMenuIngredientLists()){
            menuIngredientService.deleteMenuIngredient(menuIngredient.getId());
        }
        Menu menu = menuService.getOneMenu(mvo.getMenuName()).get();
        menuService.deleteMenu(menu.getId());
        return "redirect:/";
    }

}
