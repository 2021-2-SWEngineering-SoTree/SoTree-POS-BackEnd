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
    public String addMenu(Menu menu, @RequestBody MenuIngredient[] menuIngredientLists){

        /*
    [
        {
            "menuName": "김치찌개",
            "price":15000,
            "menuCategory":"식사"
        },
        {
            "ingredientName":"김치",
            "count":3
        },
        {
            "ingredientName":"두부",
            "count":7
        }
    ]
    */
        return "OK";
    }



}
