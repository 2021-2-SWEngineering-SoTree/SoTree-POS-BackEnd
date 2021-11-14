package sogong.restaurant.VO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.restaurant.domain.MenuIngredient;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class menuVO {

    private String menuName;
    private int price;
    private String menuCategory;
    private Long managerId;

    List<MenuIngredient> menuIngredientLists;

    @Builder
    public menuVO(String menuName, int price, String menuCategory, List<MenuIngredient> menuIngredientLists, Long managerId) {
        this.menuName = menuName;
        this.price = price;
        this.menuCategory = menuCategory;
        this.menuIngredientLists = menuIngredientLists;
        this.managerId = managerId;
    }


}
