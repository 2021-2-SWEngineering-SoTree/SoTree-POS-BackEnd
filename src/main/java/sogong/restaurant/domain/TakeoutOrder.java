package sogong.restaurant.domain;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DiscriminatorValue(MenuOrder.OrderType.Values.TAKEOUT_ORDER)
public class TakeoutOrder extends MenuOrder {

    private int takeoutTicketNumber;
}
