package sogong.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "EventId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private int eventDiscountValue; // 할인 가격

    @DecimalMin(value = "0.00", message = "할인 최소비율")
    @DecimalMin(value = "1.00", message = "할인 최대비율")
    private Double eventDiscountRate;  // 할인율

    //branch
    @ManyToOne
    @JoinColumn(name = "BranchId")
    private Manager manager;
}
