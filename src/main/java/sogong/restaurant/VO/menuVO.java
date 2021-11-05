package sogong.restaurant.VO;

import sogong.restaurant.domain.MenuIngredient;

import java.util.List;

public class menuVO {

    private String menuName;
    private int price;
    private String menuCategory;

    List<MenuIngredient> menuIngredientLists;

    public String getMenuName(){
        return menuName;
    }

    public String getMenuCategory(){
        return menuCategory;
    }

    public int getPrice(){
        return price;
    }

    public List<MenuIngredient> getMenuIngredientLists() {
        return menuIngredientLists;
    }
}
