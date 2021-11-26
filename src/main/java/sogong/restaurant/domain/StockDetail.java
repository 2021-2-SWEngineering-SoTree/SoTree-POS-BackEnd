package sogong.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockDetail {

    @Id
    @Column(name = "StockDetailId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date time;
    private int quantityChanged; // 변화량

    private int finalQuantity; // 변화 이후 해당 재고 총량

    private String memo;  // 변동 사항에 대한 기록(비고)

    @ManyToOne
    @JoinColumn(name = "StockId")
    private Stock stock;

    //User user;
    @ManyToOne
    @JoinColumn(name = "EmployeeId")
    private Employee employee;

    @PrePersist
    protected void onCreate() {
        time = new Date();
    }

}
