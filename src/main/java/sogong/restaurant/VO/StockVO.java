package sogong.restaurant.VO;

import lombok.*;
import sogong.restaurant.domain.StockDetail;

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
    List<StockDetail> stockDetailList;
}
