package sogong.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    @Id
    @Column(name = "BranchId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String branchPhoneNumber;
    private String storeName;
    private int seatCnt;

    @OneToOne
    @JoinColumn(name="UserId")
    private User user;

}
