package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class orderVO {

    private Long orderId;
    private int seatNumber;
    private int totalPrice;
    private Map<String, Long> orderDetails;


}
