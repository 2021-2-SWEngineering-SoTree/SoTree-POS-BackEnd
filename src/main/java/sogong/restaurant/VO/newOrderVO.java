package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.restaurant.domain.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class newOrderVO {

    private int totalPrice;
    private String startTime; // "yyyy-MM-dd HH:mm"
    private String endTime;   // "yyyy-MM-dd HH:mm"

    private MenuOrder.OrderType orderType;

    private int seatNumber;           // TableOrder
    private int takeoutTicketNumber;  // TakeoutOrder

    private List<OrderDetail> orderDetails;

    private Long employeeId;
    private Long managerId;

}
