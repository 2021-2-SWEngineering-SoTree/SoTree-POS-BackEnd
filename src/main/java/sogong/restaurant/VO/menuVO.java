package sogong.restaurant.VO;

import lombok.Builder;
import sogong.restaurant.domain.MenuIngredient;

import java.util.List;

public class menuVO {

    private String menuName;
    private int price;
    private String menuCategory;

    @Builder
    public menuVO(String menuName, int price, String menuCategory, List<MenuIngredient> menuIngredientLists) {
        this.menuName = menuName;
        this.price = price;
        this.menuCategory = menuCategory;
        this.menuIngredientLists = menuIngredientLists;
    }

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
