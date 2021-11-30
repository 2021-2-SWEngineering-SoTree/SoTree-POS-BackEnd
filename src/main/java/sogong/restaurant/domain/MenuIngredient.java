package sogong.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuIngredient {

    @Id
    @Column(name = "MenuIngredientId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String ingredientName;
    private int count;

    @ManyToOne
    @JoinColumn(name="MenuId")
    private Menu menu;


}
