package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class newOrderVO {

    private int totalPrice;
    private String startTime; // "yyyy-MM-dd HH:mm"
    private String endTime;   // "yyyy-MM-dd HH:mm"

    private String orderType;

    private int seatNumber;           // TableOrder
    private Boolean isSeated;

    private int takeoutTicketNumber;  // TakeoutOrder

    private List<Map<String, Integer>> orderDetails;

    private Long orderId;

    private Long employeeId;
    private Long managerId;

}
