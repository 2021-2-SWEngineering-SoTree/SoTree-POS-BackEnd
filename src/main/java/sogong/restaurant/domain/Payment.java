package sogong.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Payment {
    @Id
    @Column(name = "PaymentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private String payTime;
    private int finalPrice;

    //User user;
    @ManyToOne
    @JoinColumn(name="EmployeeId")
    private Employee employee;

    //branch
    @ManyToOne
    @JoinColumn(name="BranchId")
    private Manager manager;

    @OneToOne
    @JoinColumn(name="orderId")
    private MenuOrder menuOrder;

}
