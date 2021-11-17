package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.VO.newOrderVO;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.EmployeeRepository;
import sogong.restaurant.service.ManagerService;
import sogong.restaurant.service.OrderDetailService;
import sogong.restaurant.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static sogong.restaurant.domain.MenuOrder.OrderType;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;
    @Autowired
    private final OrderDetailService orderDetailService;
    @Autowired
    private final ManagerService managerService;
    @Autowired
    private final EmployeeRepository employeeRepository;

    // @PostMapping("/currentSeatOrder")
//    public orderVO validName(@RequestBody Map<String, String> param) {
//        //현재 좌석의 주문을 어떻게 구분할지? 이전의 좌석이랑?
//
//        /*
//        {
//            "BranchId" : "1",
//            "seatNumber" : "1"
//        }
//         */
//
//        Long BranchId = Long.parseLong(param.get("BranchId"));
//        int seatNumber = Integer.parseInt(param.get("seatNumber"));
//
//        return orderService.getTableOrderByBranchIdAndSeatNumber(BranchId, seatNumber).orElse(null);
//    }

    @PostMapping("/addTableOrder")
    public String addTableOrder(@RequestBody newOrderVO oVO) {

        // Manager(branchId) & 주문 받은 직원
        Manager manager = managerService.getOneManager(oVO.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));
        Employee employee = employeeRepository.findById(oVO.getEmployeeId())
                .orElseThrow(() -> new NoSuchElementException("해당 직원이 존재하지 않습니다."));

        // 예외 처리
        if (OrderType.valueOf(oVO.getOrderType()) != OrderType.TABLE_ORDER) {
            System.out.println(oVO.getOrderType());
            return "Wrong Order Type";
        }

        if (oVO.getOrderDetails().isEmpty()) {
            System.out.println("Wrong Order!");
            return "null Order Details";
        }

        TableOrder order = new TableOrder();

        List<Map<String, Integer>> orderDetails = oVO.getOrderDetails();

        order.setOrderType(MenuOrder.OrderType.TABLE_ORDER);
        order.setSeatNumber(oVO.getSeatNumber());
        //order.setIsSeated(oVO.getIsSeated());
        order.setIsSeated(Boolean.TRUE);

        // branchId로 find manager
        order.setTotalPrice(oVO.getTotalPrice());
        // order.setOrderDate(oVO.get);
        order.setStartTime(oVO.getStartTime());
        order.setEndTime(oVO.getEndTime());
        order.setEmployee(employee);
        order.setManager(manager);

        orderService.addTableOrder(order, orderDetails);

        return Long.toString(order.getId());
    }

    @PostMapping("/addTakeoutOrder")
    public String addTakeoutOrder(@RequestBody newOrderVO oVO) {

        MenuOrder.OrderType orderType = MenuOrder.OrderType.valueOf(oVO.getOrderType());

        // Manager(branchId) & 주문 받은 직원
        Manager manager = managerService.getOneManager(oVO.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));
        Employee employee = employeeRepository.findById(oVO.getEmployeeId())
                .orElseThrow(() -> new NoSuchElementException("해당 직원이 존재하지 않습니다."));

        // 예외 처리
        if (OrderType.valueOf(oVO.getOrderType()) != OrderType.TAKEOUT_ORDER) {
            System.out.println(oVO.getOrderType());
            return "Wrong Order Type";
        }

        if (oVO.getOrderDetails().isEmpty()) {
            System.out.println("Wrong Order!");
            return "null Order Details";
        }

        TakeoutOrder order = new TakeoutOrder();
        order.setOrderType(MenuOrder.OrderType.TAKEOUT_ORDER);
        order.setTakeoutTicketNumber(oVO.getTakeoutTicketNumber());

        List<Map<String, Integer>> orderDetails = oVO.getOrderDetails();

        // branchId로 find manager
        order.setTotalPrice(oVO.getTotalPrice());
        // order.setOrderDate(oVO.get);
        order.setStartTime(oVO.getStartTime());
        order.setEndTime(oVO.getEndTime());
        order.setEmployee(employee);
        order.setManager(manager);
        order.setIsSeated(Boolean.TRUE);

        orderService.addTakeoutOrder(order, orderDetails);

        return "OK";
    }

    @PostMapping("/getTableOrder/{branchId}/{totalTable}")
    public List<orderVO> getAllTableOrder(@PathVariable(value = "branchId") Long branchId, @PathVariable(value = "totalTable") int totalTable) {
        List<orderVO> orderDetailList = new ArrayList<>();

        if (totalTable < 1) {
            throw new IllegalStateException("전체 좌석의 번호는 1보다 커야합니다.");
        }

        // new orderVO(-1l,-1,-1, Map.of()) : default 값 (order 존재 하지 않음)
        for (int seatNumber = 1; seatNumber <= totalTable; seatNumber++) {
            orderVO orderVOOptional = orderService.getTableOrderByBranchIdAndSeatNumber(branchId, seatNumber)
                    .orElse(new orderVO(-1L, -1, -1, -1, new ArrayList<>()));
            orderDetailList.add(orderVOOptional);
        }
        System.out.println(orderDetailList);
        return orderDetailList;
    }

    @PostMapping("/getOneTableInfo/{branchId}/{seatNumber}")
    public orderVO getOneTableOrder(@PathVariable(value = "branchId") Long branchId, @PathVariable(value = "seatNumber") int seatNumber) {

        // new orderVO(-1l,-1,-1, Map.of()) : default 값 (order 존재 하지 않음)
        return orderService.getTableOrderByBranchIdAndSeatNumber(branchId, seatNumber)
                .orElse(new orderVO(-1L, -1, -1, -1, new ArrayList<>()));
        //if(oVO.isEmpty()) throw new NoSuchElementException("현재 좌석에 주문이 없습니다.");

    }

    @PostMapping("/getTakeoutOrder/{branchId}")
    public List<orderVO> getAllTakeoutOrder(@PathVariable(value = "branchId") Long branchId) {

        // new orderVO(-1l,-1,-1, Map.of()) : default 값 (order 존재 하지 않음)
        return orderService.getTakeoutOrderByBranchIdAndSeatNumber(branchId);
    }

//    @PostMapping("/addOrderDetail")
//    public Long addOrderDetail(@RequestBody newOrderVO){
//
//    }

}

