package sogong.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @Column(name = "EventId")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String eventName;

    @ColumnDefault(value = "-1")
    private Integer eventDiscountValue; // 할인 가격 (정수)
    @ColumnDefault(value = "-1")
    private Double eventDiscountRate; // 할인 비율 (0~1 사이)

    private int criticalPoint; // 이벤트 시작 임계점

    @ManyToOne
    @JoinColumn(name = "BranchId")
    private Manager manager;

}
