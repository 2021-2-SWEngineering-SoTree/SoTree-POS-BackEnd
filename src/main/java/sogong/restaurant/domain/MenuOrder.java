package sogong.restaurant.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "orderType", discriminatorType = DiscriminatorType.STRING)
public abstract class MenuOrder {

    // Table & Takeout 구분을 위한 OrderType
    public enum OrderType {
        TABLE_ORDER("TABLE_ORDER"),
        TAKEOUT_ORDER("TAKEOUT_ORDER");
        private String value;

        OrderType(String val) {
            if (!name().equals(val)) {
                throw new IllegalArgumentException("Incorrect use of GroupType");
            }
        }

        public static class Values {
            public static final String TABLE_ORDER = "TABLE_ORDER";
            public static final String TAKEOUT_ORDER = "TAKEOUT_ORDER";
        }

        public String getValue() {
            return value;
        }
    }

    @Id
    @Column(name = "OrderId")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private int totalPrice;
    private Boolean isSeated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private String startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private String endTime;

    @Column(name = "orderType", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType; //  TABLE_ORDER,TAKEOUT_ORDER

    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    //private String orderDate;

    //User user;
    @ManyToOne
    @JoinColumn(name = "EmployeeId")
    private Employee employee;

    //branch
    @ManyToOne
    @JoinColumn(name = "BranchId")
    private Manager manager;

    @OneToMany(mappedBy = "menuOrder")
    private List<OrderDetail> orderDetailList = new ArrayList<>();

}

