package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventVO {
    private Long id;

    private String eventName;

    private Integer eventDiscountValue; // 할인 가격 (정수)
    private Double eventDiscountRate; // 할인 비율 (0~1 사이)

    private Long managerId;

}
