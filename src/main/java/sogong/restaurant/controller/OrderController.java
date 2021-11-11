package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.TableOrder;
import sogong.restaurant.service.OrderService;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/currentSeatOrder")
    public orderVO validName(@RequestBody Map<String, String> param) {
        //현재 좌석의 주문을 어떻게 구분할지? 이전의 좌석이랑?

        /*
        {
            "BranchId" : "1",
            "seatNumber" : "1"
        }
         */

        Long BranchId = Long.parseLong(param.get("BranchId"));
        int seatNumber = Integer.parseInt(param.get("seatNumber"));

        return orderService.getTableOrderByBranchIdAndSeatNumber(BranchId,seatNumber);
    }
}
