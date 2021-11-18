package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.VO.menuVO;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.service.ManagerService;
import sogong.restaurant.service.MenuIngredientService;
import sogong.restaurant.service.MenuService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;
    private final MenuIngredientService menuIngredientService;
    private final ManagerService managerService;

    @Autowired
    public MenuController(MenuService menuService, MenuIngredientService menuIngredientService, ManagerService managerService) {
        this.menuService = menuService;
        this.menuIngredientService = menuIngredientService;
        this.managerService = managerService;
    }


    @PostMapping("/add")
    public String addMenu(@RequestBody menuVO mvo) {

        /*
    {

        "menuName": "김치찌개232",
        "price":15000,
        "menuCategory":"식사",
        "managerId":1,
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
        System.out.println("mvo.getPrice() = " + mvo.getPrice());

        if (mvo.getMenuName() == null || mvo.getMenuCategory() == null || mvo.getPrice() == 0) {
            System.out.println("blank!");
            return "null input";
        }
        System.out.println("menu" + mvo.getManagerId());

        Manager manager = managerService.getOneManager(mvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        menu.setMenuName(mvo.getMenuName());
        menu.setMenuCategory(mvo.getMenuCategory());
        menu.setPrice(mvo.getPrice());
        menu.setManager(manager);
        menuService.saveMenu(menu);

        for (MenuIngredient menuIngredient : mvo.getMenuIngredientLists()) {
            menuIngredient.setMenu(menu);
            menuIngredientService.addMenuIngredient(menuIngredient);
            System.out.println("menuIngredient = " + menuIngredient.getIngredientName());
        }

        return "OK";
    }


    @PutMapping("/{id}")
    public String updateMenu(@RequestBody menuVO mvo) {
        Manager manager = managerService.getOneManager(mvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        Menu menu = menuService.getOneMenu(mvo.getMenuName(), manager)
                .orElseThrow(() -> new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

        // 메뉴 예외 처리
        if (mvo.getMenuName() == null || mvo.getMenuCategory() == null || mvo.getPrice() == 0) {
            System.out.println("blank!");
            return "null input";
        }

        menu.setMenuCategory(mvo.getMenuCategory());
        menu.setPrice(mvo.getPrice());

        // 이름 변경 안 됐을 때, 중복검증 없음
        if (mvo.getMenuName().equals(menu.getMenuName())) {
            menuService.updateMenuWithoutName(menu);
        } else {
            menuService.saveMenu(menu);
        }

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@RequestBody menuVO mvo) {
        // MenuIngredient 먼저 삭제
        for (MenuIngredient menuIngredient : mvo.getMenuIngredientLists()) {
            menuIngredientService.deleteMenuIngredient(menuIngredient.getId());
        }


        Manager manager = managerService.getOneManager(mvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        Menu menu = menuService.getOneMenu(mvo.getMenuName(), manager)
                .orElseThrow(() -> new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

        menuService.deleteMenu(menu.getId());
        return "redirect:/";
    }

    @PostMapping("/getAll")
    public List<Menu> getAllMenu(@RequestBody String managerId) {
        //managerId 숫자만 body에 넣어서 요청하면 된다.

        System.out.println("managerId = " + managerId);

        Manager manager = managerService.getOneManager(Long.parseLong(managerId))
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        return menuService.getAllMenu(manager);
    }

//    @PostMapping("/isPresent")
//    public boolean validName(@RequestBody Map<String, String> param) {
//
//        String menuName = param.get("menuName");
//        Long managerId = Long.parseLong(param.get("managerId"));
//
//        System.out.println("menuName = " + menuName);
//
//        Optional<Manager> manager = managerRepository.findById(managerId);
//        if (manager.isEmpty()) {
//            return false;
//        }
//
//        Optional<Menu> menu = menuService.getOneMenu(menuName, manager.get());
//
//        //이름이 이미 존재하면 false 값을 리턴한다.
//        if (menu.isEmpty()) {
//            return true;
//        } else {
//            System.out.println("menu = " + menu.get().getMenuName());
//            return false;
//        }
//    }

    @PostMapping("/getByName")
    public List<Map<String, String>> getByName(@RequestBody Map<String, String> params) {

        String menuName = params.get("menuName");
        Long managerId = Long.parseLong(params.get("managerId"));
        Manager manager = managerService.getOneManager(managerId)
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        Menu menu = menuService.getOneMenu(menuName, manager)
                .orElseThrow(() -> new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));


        System.out.println("menu.get().getMenuName() = " + menu.getMenuName());


        List<MenuIngredient> menuIngredients = menuIngredientService.getMenuIngredientByMenu(menu);

        List<Map<String, String>> r = new ArrayList<>();

        Map<String, String> paramss = new HashMap<>();
        paramss.put("menuName", menu.getMenuName());
        paramss.put("price", String.valueOf(menu.getPrice()));
        paramss.put("menuCategory", menu.getMenuCategory());

        //r.add(params); id, ingredientName, count만 사용.

        for (MenuIngredient menuIngredient : menuIngredients) {
            Map<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(menuIngredient.getId()));
            param.put("ingredientName", menuIngredient.getIngredientName());
            param.put("count", String.valueOf(menuIngredient.getCount()));

            r.add(param);
        }

        //menuVO ret = new menuVO(menu.get().getMenuName(),menu.get().getPrice(),menu.get().getMenuCategory(),menuIngredients,menu.get().getManager().getId());

        return r;
    }

}
