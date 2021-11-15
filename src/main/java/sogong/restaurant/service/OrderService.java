package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.OrderDetail;
import sogong.restaurant.domain.TableOrder;
import sogong.restaurant.domain.TakeoutOrder;
import sogong.restaurant.repository.*;

import javax.transaction.Transactional;
import java.util.*;


@Transactional
@Service
public class OrderService {

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    TableOrderRepository tableOrderRepository;
    @Autowired
    TakeoutOrderRepository takeoutOrderRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    MenuRepository menuRepository;

    public List<OrderDetail> getTableOrderByBranchIdAndSeatNumber(Long BranchId, int seatNumber) {

        //나중에 현재 식사하고 있는 주문에 대한 정보 받아오는 것 로직 추가해야함.
        // --> validDuplicateTableOrder
        List<OrderDetail> ret = new ArrayList<>();
        // Map<String,Long> zipOrderDetail = new HashMap<>();

        List<TableOrder> tableOrderList = tableOrderRepository
                .findAllByManager(managerRepository.findById(BranchId)
                        .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다.")));


        // list 중 파라미터의 seatnumber와 동일한 좌석 번호를 가진 order 찾기
        // 없으면 error
        Optional<TableOrder> tableOrder = tableOrderList.stream()
                .filter(s -> s.getSeatNumber() == seatNumber)
                .filter(s -> s.getIsSeated() == Boolean.TRUE)
                .findAny();

        if (tableOrder.isPresent()) {
            // orderDetails
            ret = orderDetailRepository.findAllByMenuOrder(tableOrder.get()).
                    orElseGet(ArrayList::new);

//            for (OrderDetail s : orderDetails) {
//                zipOrderDetail.put(s.getMenu().getMenuName(), Long.valueOf(s.getQuantity()));
//            }
//
//            ret = Optional.of(new orderVO(tableOrder.get().getId(), seatNumber, tableOrder.get().getTotalPrice(), zipOrderDetail));
        }
        return ret;
    }

    public Long addTableOrder(TableOrder tableOrder, List<Map<String, Integer>> orderDetailList) {
        validateDuplicateTableOrder(tableOrder);

        tableOrderRepository.save(tableOrder);
        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            for (String key : orderDetailMap.keySet()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMenuOrder(tableOrder);
                orderDetail.setMenu(menuRepository.findMenuByMenuName(key).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다.")));
                orderDetail.setQuantity(orderDetailMap.get(key));
                orderDetailService.addOrderDetail(orderDetail);
                System.out.println("orderDetail");
                System.out.println("Menu Name =" + orderDetail.getMenu().getMenuName());
            }
        }
        return tableOrder.getId();

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

    public Long addTakeoutOrder(TakeoutOrder takeoutOrder, List<Map<String, Integer>> orderDetailList) {
        takeoutOrderRepository.save(takeoutOrder);
        for (Map<String, Integer> orderDetailMap : orderDetailList) {

            for (String key : orderDetailMap.keySet()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setMenuOrder(takeoutOrder);
                orderDetail.setMenu(menuRepository.findMenuByMenuName(key).
                        orElseThrow(() ->
                                new NoSuchElementException("해당 메뉴가 존재하지 않습니다.")));
                orderDetail.setQuantity(orderDetailMap.get(key));
                orderDetailService.addOrderDetail(orderDetail);
                System.out.println("orderDetail");
                System.out.println("Menu Name =" + orderDetail.getMenu().getMenuName());
            }
        }
        return takeoutOrder.getId();

    }


}
