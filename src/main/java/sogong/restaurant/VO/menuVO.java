package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.restaurant.domain.MenuIngredient;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class menuVO {

    private String menuName;
    private int price;
    private String menuCategory;
    private Long managerId;

    List<MenuIngredient> menuIngredientLists;

}
