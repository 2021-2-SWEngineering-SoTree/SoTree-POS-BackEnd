package sogong.restaurant.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DiscriminatorValue(MenuOrder.OrderType.Values.TAKEOUT_ORDER)
public class TakeoutOrder extends MenuOrder{
//    @Id
//    @Column(name = "TakeoutOrderId")
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    //private Long id;
    private int takeoutTicketNumber;


}
