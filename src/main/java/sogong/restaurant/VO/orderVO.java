package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;
import sogong.restaurant.domain.TableOrder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class orderVO {

    private Long orderId;
    private int seatNumber;
    private int totalPrice;
    private Map<String,Long> orderDetails;

}
