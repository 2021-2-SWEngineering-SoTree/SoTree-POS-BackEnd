package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockVO {
    private String stockName;
    private int quantity;
    private Long managerId;
    private Long employeeId;
    List<StockdetailVO> stockDetailList;
}
