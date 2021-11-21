package sogong.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.*;

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

            ret.add(new orderVO(takeoutOrder.getId(), -1,
                    takeoutOrder.getTakeoutTicketNumber(), takeoutOrder.getTotalPrice(), orderDetailSummary));
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
                Menu menu = menuRepository.findMenuByMenuName(key).
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
                Menu menu = menuRepository.findMenuByMenuName(key).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

                // orderDetail의 메뉴와 비교해서 기존 orderdetail 찾기
                List<OrderDetail> orderDetailByMenuOrder = orderDetailService.getOrderDetailByMenuOrder(tableOrder);
                for (OrderDetail orderDetail : orderDetailByMenuOrder) {

                    // 기존 orderDetail 과 수량도 같으면 재고처리 안하고 다음 메뉴에 대한 주문으로 넘어감
                    if (orderDetail.getMenu().equals(menu)) {
                        if (orderDetailMap.get(key) == 0) {   // 수량이 0일 때 orderDetail 삭제
                            orderDetailService.deleteOrderDetail(orderDetail.getId());
                            continue outerloop;
                        }
                        if (orderDetail.getQuantity() != orderDetailMap.get(key)) {  // 수량 변화 있을 때
                            // addStockDetail 에서 재고 확인 후 이상 없으면 orderdetail 생성
                            orderDetail.setQuantity(orderDetailMap.get(key));
                            orderDetailService.addOrderDetail(orderDetail);

                        }
                        continue outerloop;
                    }
                }


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
     * TakeoutOrder 최초 추가
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class}) // exception 발생 시 rollback

    public Long addTakeoutOrder(TakeoutOrder takeoutOrder, List<Map<String, Integer>> orderDetailList) {
        takeoutOrderRepository.save(takeoutOrder);

        Employee employee = takeoutOrder.getEmployee();

        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            for (String key : orderDetailMap.keySet()) {

                Menu menu = menuRepository.findMenuByMenuName(key).
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
            for (String key : orderDetailMap.keySet()) {
                Menu menu = menuRepository.findMenuByMenuName(key).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

                // orderDetail의 메뉴와 비교해서 기존 orderdetail 찾기
                List<OrderDetail> orderDetailByMenuOrder = orderDetailService.getOrderDetailByMenuOrder(takeoutOrder);
                for (OrderDetail orderDetail : orderDetailByMenuOrder) {

                    // 기존 orderDetail 과 수량도 같으면 재고처리 안하고 다음 메뉴에 대한 주문으로 넘어감
                    if (orderDetail.getMenu().equals(menu)) {
                        if (orderDetailMap.get(key) == 0) {   // 수량이 0일 때 orderDetail 삭제
                            orderDetailService.deleteOrderDetail(orderDetail.getId());
                            continue outerloop;
                        }
                        if (orderDetail.getQuantity() != orderDetailMap.get(key)) {  // 수량 변화 있을 때
                            // addStockDetail 에서 재고 확인 후 이상 없으면 orderdetail 생성
                            orderDetail.setQuantity(orderDetailMap.get(key));
                            orderDetailService.addOrderDetail(orderDetail);

                        }
                        continue outerloop;
                    }
                }


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
                    // stockDetail.setFinalQuantity(stockdetailVO.getQuantityChanged()); // 처음 재고 설정이므로 변화 이후 양도 동일함
                    stockDetailService.addStockDetail(stock, stockDetail);
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


}
