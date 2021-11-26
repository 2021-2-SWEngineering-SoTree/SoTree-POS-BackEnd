package sogong.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @NotNull
    @Column(columnDefinition = "boolean default true")
    private Boolean active; // 현재 사용중인 재고인지 여부
    private int totalQuantity;
    private int totalTime;

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