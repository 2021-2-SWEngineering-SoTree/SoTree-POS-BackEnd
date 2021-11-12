package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.repository.OrderDetailRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class OrderDetailService {
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public Long addOrderDetail(OrderDetail orderDetail){
        orderDetailRepository.save(orderDetail);
        return orderDetail.getId();
    }

//    // 같은 주문에 대해 같은 메뉴가 들어 왔을 때
//    private void validateDuplicateStock(OrderDetail orderDetail){
//        orderDetailRepository.findOrderDetailByMenuOrder(orderDetail.getMenuOrder())
//                .ifPresent(list -> list.stream().forEach(s->s.findOrderDetailByMenu())
//
//
//    }
    @Transactional
    public void deleteOrderDetail(Long id){   orderDetailRepository.deleteById(id);   }

    public List<OrderDetail> getOrderDetailByMenuOrder(MenuOrder menuOrder) {
        return orderDetailRepository.findOrderDetailByMenuOrder(menuOrder).get();
    }
}
