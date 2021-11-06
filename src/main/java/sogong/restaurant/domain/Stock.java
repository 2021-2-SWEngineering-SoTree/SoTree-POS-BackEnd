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
public class Stock {
    @Id
    @Column(name = "StockId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;
    private Long branchId;
    private int quantity;

    // 가게에 대한 key
    // private Long BranchId;
}
