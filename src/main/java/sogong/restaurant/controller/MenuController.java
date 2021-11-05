package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.domain.MenuIngredientRepository;
import sogong.restaurant.domain.MenuRepository;
import sogong.restaurant.VO.menuVO;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {

    private MenuRepository menuRepository;
    private MenuIngredientRepository menuIngredientRepository;

    @Autowired
    public MenuController(MenuRepository menuRepository, MenuIngredientRepository menuIngredientRepository){
        this.menuIngredientRepository = menuIngredientRepository;
        this.menuRepository = menuRepository;
    }

    @PostMapping("/getAll")
    public List<Menu> getAllMenu(){
        return menuRepository.findAll();
    }

    @PostMapping("/add")
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

        Menu menu = new Menu();

        System.out.println("mvo.getMenuName() = " + mvo.getMenuName());

        menu.setMenuName(mvo.getMenuName());
        menu.setMenuCategory(mvo.getMenuCategory());
        menu.setPrice(mvo.getPrice());
        menuRepository.save(menu);

        for(MenuIngredient menuIngredient:mvo.getMenuIngredientLists()){
            menuIngredient.setMenu(menu);
            menuIngredientRepository.save(menuIngredient);
            System.out.println("menuIngredient = " + menuIngredient.getIngredientName());
        }

        return "OK";
    }



}
