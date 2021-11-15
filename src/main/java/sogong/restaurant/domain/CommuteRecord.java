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
public class CommuteRecord {

    @Id
    @Column(name = "CommuteRecordId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    @JoinColumn(name="EmployeeId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "ManagerId")
    private Manager manager;

    Boolean isComing;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    String time;

}
