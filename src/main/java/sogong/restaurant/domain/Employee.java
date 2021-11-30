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
public class Employee {


    @Id
    @Column(name = "EmployeeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    boolean commuteState;

    private boolean isActive;  // 현재 직원인지 여부

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;
    String workSchedule;

    @ManyToOne
    @JoinColumn(name = "BranchId")
    private Manager manager;

}
