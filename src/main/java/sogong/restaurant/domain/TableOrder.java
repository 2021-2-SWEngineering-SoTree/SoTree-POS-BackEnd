package sogong.restaurant.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableOrder {

    @Id
    @Column(name = "TableOrderId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int seatNumber;

    @OneToOne
    @JoinColumn(name="OrderId")
    private MenuOrder menuOrder;

}
