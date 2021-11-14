package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.VO.newOrderVO;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.EmployeeRepository;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.service.OrderDetailService;
import sogong.restaurant.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static sogong.restaurant.domain.MenuOrder.OrderType;


@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public OrderController(OrderService orderService, OrderDetailService orderDetailService, ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
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

        return orderService.getTableOrderByBranchIdAndSeatNumber(BranchId, seatNumber);
    }

    @PostMapping("/addTableOrder")
    public String addTableOrder(@RequestBody newOrderVO oVO) {

        // Manager(branchId) & 주문 받은 직원
        Optional<Manager> manager = managerRepository.findById(oVO.getManagerId());
        Optional<Employee> employee = employeeRepository.findById(oVO.getEmployeeId());

        // 예외 처리
        switch (OrderType.valueOf(oVO.getOrderType())) {
            case TABLE_ORDER:
                break;
            default:
                System.out.println(oVO.getOrderType());
                return "Wrong Order Type";
        }

        if (oVO.getOrderDetails().isEmpty()) {
            System.out.println("Wrong Order!");
            return "null Order Details";
        }

        if (manager.isEmpty()) {
            System.out.println("blank manager");
            return "null manager";
        }

        TableOrder order = new TableOrder();

        List<Map<String, Integer>> orderDetails = oVO.getOrderDetails();

        order.setOrderType(MenuOrder.OrderType.TABLE_ORDER);
        order.setSeatNumber(oVO.getSeatNumber());

        // branchId로 find manager
        order.setTotalPrice(oVO.getTotalPrice());
        // order.setOrderDate(oVO.get);
        order.setStartTime(oVO.getStartTime());
        order.setEndTime(oVO.getEndTime());
        order.setEmployee(employee.get());
        order.setManager(manager.get());

        orderService.addTableOrder(order, orderDetails);

        return Long.toString(order.getId());
    }

    @PostMapping("/addTakeoutOrder")
    public String addTakeoutOrder(@RequestBody newOrderVO oVO) {

        MenuOrder.OrderType orderType = MenuOrder.OrderType.valueOf(oVO.getOrderType());

        // Manager(branchId) & 주문 받은 직원
        Optional<Manager> manager = managerRepository.findById(oVO.getManagerId());
        Optional<Employee> employee = employeeRepository.findById(oVO.getEmployeeId());

        // 예외 처리
        switch (OrderType.valueOf(oVO.getOrderType())) {
            case TAKEOUT_ORDER:
                break;
            default:
                System.out.println(oVO.getOrderType());
                return "Wrong Order Type";
        }

        if (oVO.getOrderDetails().isEmpty()) {
            System.out.println("Wrong Order!");
            return "null Order Details";
        }

        if (manager.isEmpty()) {
            System.out.println("blank manager");
            return "null manager";
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
        order.setEmployee(employee.get());
        order.setManager(manager.get());


        orderService.addTakeoutOrder(order, orderDetails);

        return "OK";
    }
}
