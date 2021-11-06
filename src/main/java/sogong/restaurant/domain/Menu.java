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
public class Menu {
    @Id
    @Column(name = "MenuId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String menuName;
    private int price;
    private String menuCategory;

    // 가게에 대한 key
    // private Long BranchId;
}
