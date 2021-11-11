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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long Id;
    boolean commuteState;
    @OneToOne
    @JoinColumn(name="UserId")
    private User user;

    @ManyToOne
    @JoinColumn(name="BranchId")
    private Manager manager;

}
