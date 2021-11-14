package sogong.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @Column(name = "MenuId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;
    private int price;
    private String menuCategory;

    // 가게에 대한 key
    @ManyToOne
    //@JsonManagedReference // 순환참조 방지
    @JoinColumn(name = "BranchId")
    private Manager manager;

    // orderdetailList -> Menu 단방향
    //@OneToMany(mappedBy = "menu")
    //@JsonBackReference
    //private List<OrderDetail> orderDetailList = new ArrayList<>();


}
