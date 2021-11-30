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
public class Stock {
    @Id
    @Column(name = "StockId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;
    private int quantity;

    @NotNull
    @Column(columnDefinition = "boolean default true")
    private Boolean active; // 현재 사용중인 재고인지 여부

    // 가게에 대한 key
    @ManyToOne
    @JoinColumn(name = "BranchId")
    private Manager manager;


}
