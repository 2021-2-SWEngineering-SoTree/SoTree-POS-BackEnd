package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.restaurant.summary.OrderDetailSummary;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class orderVO {
    public orderVO(Long orderId, int seatNumber, int takeoutTicketNumber, int totalPrice, List<OrderDetailSummary> orderDetailSummaries) {
        this.orderId = orderId;
        this.seatNumber = seatNumber;
        this.takeoutTicketNumber = takeoutTicketNumber;
        this.totalPrice = totalPrice;
        this.orderDetailSummaries = orderDetailSummaries;
    }

    private Long orderId;
    private int seatNumber = -1;
    private int takeoutTicketNumber = -1;
    private int totalPrice;
    private List<OrderDetailSummary> orderDetailSummaries;


    private int finalPrice; // takeoutorder 결제 이후 가격
}
