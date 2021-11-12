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

import java.util.Map;
import java.util.Optional;

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

        return orderService.getTableOrderByBranchIdAndSeatNumber(BranchId,seatNumber);
    }

    /*
        포장 주문의 경우
        {
            totalPrice : "10000",
            startTime : "2021-11-11 12:00",
            endTime : "2021-11-11 13:00",
            orderType : "TABLE_ORDER",
            seatNumber : 2,
            orderDetails : [],
            employeeId : 5,
            managerId: 3
        }

        매장 주문의 경우
        {
            totalPrice : "10000",
            startTime : "2021-11-11 12:00",
            endTime : "2021-11-11 13:00",
            orderType : "TAKEOUT_ORDER",
            seatNumber : 2,
            orderDetails : [],
            employeeId : 5,
            managerId: 3
        }

     */
    @PostMapping("/add/tableOrder")
    public String addTableOrder(@RequestBody newOrderVO oVO){


        MenuOrder.OrderType orderType = oVO.getOrderType();

        // Manager(branchId) & 주문 받은 직원
        Optional<Manager> manager = managerRepository.findById(oVO.getManagerId());
        Optional<Employee> employee = employeeRepository.findById(oVO.getEmployeeId());

        // 예외 처리 
        if(oVO.getOrderDetails().isEmpty()){
            System.out.println("Wrong Order!");
            return "null Order Details";
        }

        if((orderType.getValue() !="TABLE_ORDER")){
            System.out.println("Wrong Type");
            return "Wrong Order Type";
        }
        
        if(manager.isEmpty()){
            System.out.println("blank manager");
            return "null manager";
        }


        TableOrder order = new TableOrder();
        order.setOrderType(MenuOrder.OrderType.TABLE_ORDER);
        order.setSeatNumber(oVO.getSeatNumber());

        // branchId로 find manager
        order.setTotalPrice(oVO.getTotalPrice());
        // order.setOrderDate(oVO.get);
        order.setStartTime(oVO.getStartTime());
        order.setEndTime(oVO.getEndTime());
        order.setEmployee(employee.get());
        order.setManager(manager.get());


        for(OrderDetail orderDetail:oVO.getOrderDetails()){
            orderDetail.setMenuOrder(order);
            orderDetailService.addOrderDetail(orderDetail);
            System.out.println("orderDetail");
            System.out.println("Menu Name ="+ orderDetail.getMenu().getMenuName());
        }
        return "OK";
    }

    @PostMapping("/add/takeoutOrder")
    public String addTakeoutOrder(@RequestBody newOrderVO oVO){


        MenuOrder.OrderType orderType = oVO.getOrderType();

        // Manager(branchId) & 주문 받은 직원
        Optional<Manager> manager = managerRepository.findById(oVO.getManagerId());
        Optional<Employee> employee = employeeRepository.findById(oVO.getEmployeeId());

        // 예외 처리
        if(oVO.getOrderDetails().isEmpty()){
            System.out.println("Wrong Order!");
            return "null Order Details";
        }

        if((orderType.getValue() !="TAKEOUT_ORDER")){
            System.out.println("Wrong Type");
            return "Wrong Order Type";
        }

        if(manager.isEmpty()){
            System.out.println("blank manager");
            return "null manager";
        }


        TakeoutOrder order = new TakeoutOrder();
        order.setOrderType(MenuOrder.OrderType.TAKEOUT_ORDER);
        order.setTakeoutTicketNumber(oVO.getTakeoutTicketNumber());

        // branchId로 find manager
        order.setTotalPrice(oVO.getTotalPrice());
        // order.setOrderDate(oVO.get);
        order.setStartTime(oVO.getStartTime());
        order.setEndTime(oVO.getEndTime());
        order.setEmployee(employee.get());
        order.setManager(manager.get());


        for(OrderDetail orderDetail:oVO.getOrderDetails()){
            orderDetail.setMenuOrder(order);
            orderDetailService.addOrderDetail(orderDetail);
            System.out.println("orderDetail");
            System.out.println("Menu Name ="+ orderDetail.getMenu().getMenuName());
        }
        return "OK";
    }
}
