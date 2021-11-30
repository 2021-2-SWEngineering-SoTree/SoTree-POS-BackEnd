package sogong.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.*;
import sogong.restaurant.summary.OrderDetailSummary;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final TableOrderRepository tableOrderRepository;
    private final TakeoutOrderRepository takeoutOrderRepository;
    private final ManagerRepository managerRepository;
    private final OrderDetailService orderDetailService;
    private final MenuRepository menuRepository;
    private final MenuIngredientService menuIngredientService;
    private final StockService stockService;
    private final StockDetailService stockDetailService;
    private final MenuOrderRepository menuOrderRepository;
    private final PaymentService paymentService;

    /***
     * BranchId & SeatNumber로 TableOrder 받기
     */
    public Optional<orderVO> getTableOrderByBranchIdAndSeatNumber(Long BranchId, int seatNumber) {

        //나중에 현재 식사하고 있는 주문에 대한 정보 받아오는 것 로직 추가해야함.
        // --> validDuplicateTableOrder
        Optional<orderVO> ret = Optional.empty();

        List<TableOrder> tableOrderList = tableOrderRepository
                .findAllByManager(managerRepository.findById(BranchId)
                        .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다.")));
        System.out.println(tableOrderList);

        // list 중 파라미터의 seatnumber와 동일한 좌석 번호를 가진 order 찾기
        // 없으면 error
        Optional<TableOrder> tableOrder = tableOrderList.stream()
                .filter(s -> s.getSeatNumber() == seatNumber)
                .filter(s -> s.getIsSeated() == Boolean.TRUE)
                .findAny();

        if (tableOrder.isPresent()) {
            List<OrderDetailSummary> orderDetailSummary = orderDetailRepository.findAllByMenuOrder(tableOrder.get());

            ret = Optional.of(new orderVO(tableOrder.get().getId(), seatNumber, -1,
                    tableOrder.get().getTotalPrice(), orderDetailSummary));
        }
        return ret;
    }

    /**
     * BranchId로 전체 TakeoutOrder 찾기
     */
    public List<orderVO> getTakeoutOrderByBranchId(Long BranchId) {

        //나중에 현재 식사하고 있는 주문에 대한 정보 받아오는 것 로직 추가해야함.
        // --> validDuplicateTableOrder
        List<orderVO> ret = new ArrayList<>();

        List<TakeoutOrder> takeoutOrderList = takeoutOrderRepository
                .findAllByManager(managerRepository.findById(BranchId)
                        .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다.")));

        // list 중 파라미터의 seatnumber와 동일한 좌석 번호를 가진 order 찾기
        // 없으면 error
        List<TakeoutOrder> takeoutOrders = takeoutOrderList.stream()
                .filter(s -> s.getIsSeated() == Boolean.TRUE)
                .collect(Collectors.toList());

        for (TakeoutOrder takeoutOrder : takeoutOrders) {
            List<OrderDetailSummary> orderDetailSummary = orderDetailRepository.findAllByMenuOrder(takeoutOrder);
            Optional<Payment> paymentByOrder = paymentService.getPaymentByOrder(takeoutOrder);

            if (paymentByOrder.isPresent()) {  // 결제 내역 있으면 finalPrice도 보냄
                ret.add(new orderVO(takeoutOrder.getId(), -1,
                        takeoutOrder.getTakeoutTicketNumber(), takeoutOrder.getTotalPrice() -paymentByOrder.get().getFinalPrice(), orderDetailSummary ));
            } else {
                ret.add(new orderVO(takeoutOrder.getId(), -1,
                        takeoutOrder.getTakeoutTicketNumber(), takeoutOrder.getTotalPrice(), orderDetailSummary));
            }

        }

        return ret;
    }

    /**
     * TableOrder 최초 추가
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class}) // exception 발생 시 rollback
    public Long addTableOrder(TableOrder tableOrder, List<Map<String, Integer>> orderDetailList) {
        validateDuplicateTableOrder(tableOrder);

        tableOrderRepository.save(tableOrder);


        Employee employee = tableOrder.getEmployee();

        // 주어진 모든 orderdetail 순회
        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            // orderdetail 안에 여러 메뉴들 순회
            for (String key : orderDetailMap.keySet()) {

                Menu menu = menuRepository.findByManagerAndMenuName(tableOrder.getManager(), key).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

                // 주문 들어온 메뉴의 재료랑 재고 비교
                List<MenuIngredient> menuIngredientList = menuIngredientService.getMenuIngredientByMenu(menu);

                // 메뉴 재료와 같은 이름을 가진 재고에 대한 수정
                // stockdetail 직원은 주문 받은 직원
                for (MenuIngredient menuIngredient : menuIngredientList) {
                    Stock stock = stockService.getOneStock(tableOrder.getManager(), menuIngredient.getIngredientName())
                            .orElseThrow(() ->
                                    new NoSuchElementException("해당 재고가 존재하지 않습니다."));

                    StockDetail stockDetail = new StockDetail();
                    stockDetail.setStock(stock);
                    stockDetail.setEmployee(employee);
                    stockDetail.setQuantityChanged(menuIngredient.getCount() * (-1));
                    stockDetail.setMemo("주문");
                    stockDetail.setTime(tableOrder.getStartTime());
                    // stockDetail.setFinalQuantity(stockdetailVO.getQuantityChanged()); // 처음 재고 설정이므로 변화 이후 양도 동일함
                    stockDetailService.addStockDetail(stock, stockDetail);
                }

                // addStockDetail 에서 재고 확인 후 이상 없으면 orderdetail 생성
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMenuOrder(tableOrder);
                orderDetail.setMenu(menu);
                orderDetail.setQuantity(orderDetailMap.get(key));
                orderDetailService.addOrderDetail(orderDetail);
                System.out.println("orderDetail");
                System.out.println("Menu Name =" + orderDetail.getMenu().getMenuName());
            }
        }

        return tableOrder.getId();

    }

    /**
     * TableOrder 수정
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public Long updateTableOrder(TableOrder tableOrder, List<Map<String, Integer>> orderDetailList) {
        tableOrderRepository.save(tableOrder);

        Employee employee = tableOrder.getEmployee();

        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            // orderdetail 안에 여러 메뉴들 순회
            outerloop:
            for (String key : orderDetailMap.keySet()) {
                // 현재 양
                int currentQuantity = orderDetailMap.get(key);


                Menu menu = menuRepository.findMenuByMenuNameAndManager(key, tableOrder.getManager()).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

                // orderDetail의 메뉴와 비교해서 기존 orderdetail 찾기
                List<OrderDetail> orderDetailByMenuOrder = orderDetailService.getOrderDetailByMenuOrder(tableOrder);
                for (OrderDetail orderDetail : orderDetailByMenuOrder) {
                    int prevQuantity = orderDetail.getQuantity();

                    // 기존 orderDetail 과 수량도 같으면 재고처리 안하고 다음 메뉴에 대한 주문으로 넘어감
                    if (orderDetail.getMenu().equals(menu)) {
                        if (currentQuantity == 0) {   // 수량이 0일 때 orderDetail 삭제
                            orderDetailService.deleteOrderDetail(orderDetail.getId());

                        } else {
                            orderDetail.setQuantity(orderDetailMap.get(key));
                            orderDetailService.addOrderDetail(orderDetail);

                            if (currentQuantity > prevQuantity) {  // 주문 수량이 증가할 때
                                updateStockDetailWithOrder(menu, tableOrder, employee, currentQuantity - prevQuantity);
                            } else { // 주문 수량이 감소할 때
                                deleteStockDetailWithOrder(menu, tableOrder, employee, currentQuantity - prevQuantity);
                            }
                        }
                        continue outerloop;
                    }
                }


                // addStockDetail 에서 재고 확인 후 이상 없으면 orderdetail 생성
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMenuOrder(tableOrder);
                orderDetail.setMenu(menu);
                orderDetail.setQuantity(orderDetailMap.get(key));
                orderDetailService.addOrderDetail(orderDetail);
                System.out.println("orderDetail");
                System.out.println("Menu Name =" + orderDetail.getMenu().getMenuName());

            }
        }

        return tableOrder.getId();

    }

    /**
     * TakeoutOrder 최초 추가
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class}) // exception 발생 시 rollback

    public Long addTakeoutOrder(TakeoutOrder takeoutOrder, List<Map<String, Integer>> orderDetailList) {
        takeoutOrderRepository.save(takeoutOrder);

        Employee employee = takeoutOrder.getEmployee();

        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            for (String key : orderDetailMap.keySet()) {

                Menu menu = menuRepository.findMenuByMenuNameAndManager(key, takeoutOrder.getManager()).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

                // 주문 들어온 메뉴의 재료랑 재고 비교
                List<MenuIngredient> menuIngredientList = menuIngredientService.getMenuIngredientByMenu(menu);

                // 메뉴 재료와 같은 이름을 가진 재고에 대한 수정
                // stockdetail 직원은 주문 받은 직원
                for (MenuIngredient menuIngredient : menuIngredientList) {
                    Stock stock = stockService.getOneStock(takeoutOrder.getManager(), menuIngredient.getIngredientName())
                            .orElseThrow(() ->
                                    new NoSuchElementException("해당 재고가 존재하지 않습니다."));

                    StockDetail stockDetail = new StockDetail();
                    stockDetail.setStock(stock);
                    stockDetail.setEmployee(employee);
                    stockDetail.setQuantityChanged(menuIngredient.getCount() * (-1));
                    stockDetail.setMemo("주문");
                    stockDetail.setTime(takeoutOrder.getStartTime());

                    // stockDetail.setFinalQuantity(stockdetailVO.getQuantityChanged()); // 처음 재고 설정이므로 변화 이후 양도 동일함
                    stockDetailService.addStockDetail(stock, stockDetail);
                }

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMenuOrder(takeoutOrder);
                orderDetail.setMenu(menu);
                orderDetail.setQuantity(orderDetailMap.get(key));
                orderDetailService.addOrderDetail(orderDetail);
                System.out.println("orderDetail");
                System.out.println("Menu Name =" + orderDetail.getMenu().getMenuName());
            }
        }

        return takeoutOrder.getId();

    }

    /**
     * TakeoutOrder 수정
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class}) // exception 발생 시 rollback

    public Long updateTakeoutOrder(TakeoutOrder takeoutOrder, List<Map<String, Integer>> orderDetailList) {
        takeoutOrderRepository.save(takeoutOrder);

        Employee employee = takeoutOrder.getEmployee();

        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            // orderdetail 안에 여러 메뉴들 순회
            outerloop:
            // orderdetail 안에 여러 메뉴들 순회
            for (String key : orderDetailMap.keySet()) {
                // 현재 양
                int currentQuantity = orderDetailMap.get(key);


                Menu menu = menuRepository.findMenuByMenuNameAndManager(key, takeoutOrder.getManager()).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

                // orderDetail의 메뉴와 비교해서 기존 orderdetail 찾기
                List<OrderDetail> orderDetailByMenuOrder = orderDetailService.getOrderDetailByMenuOrder(takeoutOrder);
                for (OrderDetail orderDetail : orderDetailByMenuOrder) {
                    int prevQuantity = orderDetail.getQuantity();

                    // 기존 orderDetail 과 수량도 같으면 재고처리 안하고 다음 메뉴에 대한 주문으로 넘어감
                    if (orderDetail.getMenu().equals(menu)) {
                        if (currentQuantity == 0) {   // 수량이 0일 때 orderDetail 삭제
                            orderDetailService.deleteOrderDetail(orderDetail.getId());

                        } else {
                            orderDetail.setQuantity(orderDetailMap.get(key));
                            orderDetailService.addOrderDetail(orderDetail);

                            if (currentQuantity > prevQuantity) {  // 주문 수량이 증가할 때
                                updateStockDetailWithOrder(menu, takeoutOrder, employee, currentQuantity - prevQuantity);
                            } else { // 주문 수량이 감소할 때
                                deleteStockDetailWithOrder(menu, takeoutOrder, employee, currentQuantity - prevQuantity);
                            }
                        }
                        continue outerloop;
                    }
                }

                // addStockDetail 에서 재고 확인 후 이상 없으면 orderdetail 생성
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMenuOrder(takeoutOrder);
                orderDetail.setMenu(menu);
                orderDetail.setQuantity(orderDetailMap.get(key));
                orderDetailService.addOrderDetail(orderDetail);
                System.out.println("orderDetail");
                System.out.println("Menu Name =" + orderDetail.getMenu().getMenuName());

            }
        }

        return takeoutOrder.getId();
    }

    public void deleteTableOrder(Long id) {
        tableOrderRepository.deleteById(id);
    }

    public void deleteTakeoutOrder(Long id) {
        takeoutOrderRepository.deleteById(id);
    }


    public TableOrder getOneTableOrder(Manager manager, Long tableOrderId) {

        return tableOrderRepository.findTableOrderByManagerAndId(manager, tableOrderId)
                .orElseThrow(() -> new NoSuchElementException("해당 주문이 존재하지 않습니다"));

    }

    public TakeoutOrder getOneTakeoutOrder(Manager manager, Long takeoutOrderId) {

        return takeoutOrderRepository.findTakeoutOrderByManagerAndId(manager, takeoutOrderId)
                .orElseThrow(() -> new NoSuchElementException("해당 주문이 존재하지 않습니다"));

    }

    // 현 시각에 가게 및 좌석이 동일한 주문 있는지 확인
    private void validateDuplicateTableOrder(TableOrder tableOrder) {
        System.out.println("tableOrder = " + tableOrder.getIsSeated());
        tableOrderRepository.findAllByManager(tableOrder.getManager())
                .stream() // 한 가게의 모든 TableOrder 중
                .filter(s -> s.getSeatNumber() == tableOrder.getSeatNumber())// 매장 좌석 동일한거 찾기
                .findAny()
                .ifPresent(m -> {
                    System.out.println("m.getSeatNumber() = " + m.getSeatNumber());
                    System.out.println("m.getIsSeated() = " + m.getIsSeated().booleanValue());
                    if (m.getIsSeated()) {
                        throw new IllegalStateException("해당 좌석에 다른 주문이 존재합니다.");
                    }
                });
    }

    public List<Map<String, String>> getTakeOutTicketInfo(Long branchId, Long orderId) {
        List<Map<String, String>> ret = new ArrayList<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Manager manager = optionalManager.get();

        Optional<TakeoutOrder> optionalTakeoutOrder = takeoutOrderRepository.findTakeoutOrderByManagerAndId(manager, orderId);
        if (optionalTakeoutOrder.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 주문정보입니다.");
        }

        TakeoutOrder takeoutOrder = optionalTakeoutOrder.get();

        Map<String, String> info = new HashMap<>();
        info.put("startTime", takeoutOrder.getStartTime());
        info.put("totalPrice", String.valueOf(takeoutOrder.getTotalPrice()));
        info.put("takeoutTicketNumber", String.valueOf(takeoutOrder.getTakeoutTicketNumber()));
        info.put("branchName", manager.getUser().getPersonName());
        ret.add(info);

        return ret;
    }

    public String finishAlarm(Long branchId, Long orderId, String finishTime) throws Exception {

        System.out.println("OrderService.finishAlarm");


        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Manager manager = optionalManager.get();

        Optional<MenuOrder> optionalMenuOrder = menuOrderRepository.findByIdAndAndManager(orderId, manager);
        if (optionalMenuOrder.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 주문정보입니다.");
        }

        MenuOrder menuOrder = optionalMenuOrder.get();

        if (menuOrder.getEndTime() != null) {
            throw new IllegalStateException("이미 기록되었습니다.");
        }

        menuOrder.setEndTime(finishTime);
        //menuOrderRepository.save(menuOrder);
        if (menuOrder.getOrderType().equals(MenuOrder.OrderType.TAKEOUT_ORDER)) {
            menuOrder.setIsSeated(false);
        }

        String startTime = menuOrder.getStartTime();
        String endTime = menuOrder.getEndTime();

        Date format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
        Date format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);

        long diffMin = (format2.getTime() - format1.getTime()) / 60000;

        //System.out.println("diffMin = " + diffMin);

        for (OrderDetail orderDetail : menuOrder.getOrderDetailList()) {
            int prevQuantity = orderDetail.getMenu().getTotalQuantity();
            int curQuantity = orderDetail.getQuantity();
            int prevTime = orderDetail.getMenu().getTotalTime();
            orderDetail.getMenu().setTotalQuantity(prevQuantity + curQuantity);
            if (curQuantity == 1) {
                orderDetail.getMenu().setTotalTime(prevTime + (int) diffMin);
            } else {
                orderDetail.getMenu().setTotalTime(prevTime + (int) (diffMin * (int) (curQuantity / 2)));
            }
        }

        return "OK";
    }


    // 주문 추가 시 호출
    // quantityChanged : 늘어난 양 (e.g. 2->3 이면 1)
    private void updateStockDetailWithOrder(Menu menu, MenuOrder order, Employee employee, int quantityChanged) {

        // 주문 들어온 메뉴의 재료랑 재고 비교
        List<MenuIngredient> menuIngredientList = menuIngredientService.getMenuIngredientByMenu(menu);

        // 메뉴 재료와 같은 이름을 가진 재고에 대한 수정
        // stockdetail 직원은 주문 받은 직원
        for (MenuIngredient menuIngredient : menuIngredientList) {
            Stock stock = stockService.getOneStock(order.getManager(), menuIngredient.getIngredientName())
                    .orElseThrow(() ->
                            new NoSuchElementException("해당 재고가 존재하지 않습니다."));

            StockDetail stockDetail = new StockDetail();
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetail.setQuantityChanged(menuIngredient.getCount() * (-1) * quantityChanged);
            stockDetail.setMemo("주문");
            stockDetail.setTime(order.getStartTime());
            System.out.println("주문");

            stockDetailService.addStockDetail(stock, stockDetail);
        }
    }

    // 주문 삭제 시 호출
    // quantityChanged : 줄어든 양 (e.g. 이전 3-> 현재 2 이면 1)
    private void deleteStockDetailWithOrder(Menu menu, MenuOrder order, Employee employee, int quantityChanged) {

        // 주문 들어온 메뉴의 재료랑 재고 비교
        List<MenuIngredient> menuIngredientList = menuIngredientService.getMenuIngredientByMenu(menu);

        // 메뉴 재료와 같은 이름을 가진 재고에 대한 수정
        // stockdetail 직원은 주문 받은 직원
        for (MenuIngredient menuIngredient : menuIngredientList) {
            Stock stock = stockService.getOneStock(order.getManager(), menuIngredient.getIngredientName())
                    .orElseThrow(() ->
                            new NoSuchElementException("해당 재고가 존재하지 않습니다."));

            StockDetail stockDetail = new StockDetail();
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetail.setQuantityChanged(menuIngredient.getCount() * (-1) * quantityChanged);
            stockDetail.setMemo("주문 취소");
            stockDetail.setTime(order.getStartTime());
            System.out.println("주문 취소");
            stockDetailService.addStockDetail(stock, stockDetail);
        }
    }

}
