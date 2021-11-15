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
@DiscriminatorValue(value = MenuOrder.OrderType.Values.TABLE_ORDER)
public class TableOrder extends MenuOrder {

    private int seatNumber;
}
