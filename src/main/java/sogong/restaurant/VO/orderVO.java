package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.restaurant.domain.OrderDetail;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class orderVO {

    private Long orderId;
    private int seatNumber;
    private int totalPrice;
    private List<OrderDetail> orderDetails;

}
