package sogong.restaurant.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "orderType", discriminatorType = DiscriminatorType.STRING)
public abstract class MenuOrder {

    // Table & Takeout 구분을 위한 OrderType
    public enum OrderType {
        TABLE_ORDER("TABLE_ORDER"),
        TAKEOUT_ORDER("TAKEOUT_ORDER");
        private String value;

        OrderType(String val) {
            if (!this.name().equals(val))
                throw new IllegalArgumentException("Incorrect use of GroupType");
        }

        public static class Values {
            public static final String TABLE_ORDER = "TABLE_ORDER";
            public static final String TAKEOUT_ORDER = "TAKEOUT_ORDER";
        }

        public String getValue(){ return value; }
    }

    @Id
    @Column(name = "OrderId")
    @GeneratedValue(strategy= GenerationType.TABLE)
    private Long id;

    private int totalPrice;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private String startTime;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private String endTime;

    @Column(name="orderType", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType; //  TABLE_ORDER,TAKEOUT_ORDER

    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    //private String orderDate;

    //User user;
    @ManyToOne
    @JoinColumn(name="EmployeeId")
    private Employee employee;

    //branch
    @ManyToOne
    @JoinColumn(name="BranchId")
    private Manager manager;

    public MenuOrder(Long id, int totalPrice, String startTime, String endTime, OrderType orderType, Employee employee, Manager manager) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.orderType = orderType;
        this.employee = employee;
        this.manager = manager;
    }
}

