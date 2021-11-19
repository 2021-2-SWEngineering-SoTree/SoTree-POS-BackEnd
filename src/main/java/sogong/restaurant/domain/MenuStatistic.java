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
public class MenuStatistic {
    @Id
    @Column(name = "MenuStatisticId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private String date;

    @ManyToOne
    //@JsonManagedReference // 순환참조 방지
    @JoinColumn(name = "BranchId")
    private Manager manager;

    @OneToOne
    @JoinColumn(name="MenuId")
    private Menu menu;

}
