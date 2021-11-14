package sogong.restaurant.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DiscriminatorValue(value = MenuOrder.OrderType.Values.TABLE_ORDER)
public class TableOrder extends MenuOrder{

//    @Id
//    @Column(name = "TableOrderId")
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    private Long id;
    private int seatNumber;
    private Boolean isSeated;


}
