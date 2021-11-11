package sogong.restaurant.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrder {

    @Id
    @Column(name = "OrderId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int totalPrice;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private String startTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private String endTime;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private String orderDate;

    //User user;
    @ManyToOne
    @JoinColumn(name="EmployeeId")
    private Employee employee;

    //branch
    @ManyToOne
    @JoinColumn(name="BranchId")
    private Manager manager;

}
