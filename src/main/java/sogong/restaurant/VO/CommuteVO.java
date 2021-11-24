package sogong.restaurant.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommuteVO {

    private Long employeeId;
    private Long managerId;

    private Boolean isComing; // True : 출근 기록 , False : 퇴근 기록
    String time;

}
