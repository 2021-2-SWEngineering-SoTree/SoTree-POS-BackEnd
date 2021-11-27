package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockdetailVO {

    private String time;
    private int quantityChanged; // 변화량
    private Long employeeId;
    private String memo;
}
