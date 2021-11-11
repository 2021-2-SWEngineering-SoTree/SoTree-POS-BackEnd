package sogong.restaurant.service;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;
import sogong.restaurant.domain.TableOrder;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.OrderDetailRepository;
import sogong.restaurant.repository.OrderRepository;
import sogong.restaurant.repository.TableOrderRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class OrderService {

    OrderDetailRepository orderDetailRepository;
    OrderRepository orderRepository;
    TableOrderRepository tableOrderRepository;
    ManagerRepository managerRepository;

    @Autowired
    public OrderService(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, TableOrderRepository tableOrderRepository, ManagerRepository managerRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.tableOrderRepository = tableOrderRepository;
        this.managerRepository = managerRepository;
    }

    public orderVO getTableOrderByBranchIdAndSeatNumber(Long BranchId, int seatNumber){

        //나중에 현재 식사하고 있는 주문에 대한 정보 받아오는 것 로직 추가해야함.
        TableOrder tableOrder = new TableOrder();

        List<MenuOrder> menuOrderList = orderRepository.findMenuOrdersByManager(managerRepository.findById(BranchId).get());

        int idx = 0;

        for(int i=0;i<menuOrderList.size();i++){
            Optional<TableOrder> tableOrder1 = tableOrderRepository.findByMenuOrderAndSeatNumber(menuOrderList.get(i),seatNumber);
            if(tableOrder1.isPresent()){
                tableOrder= tableOrder1.get();
                idx=i;
            }
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findAllByMenuOrder(menuOrderList.get(idx));

        orderVO ret = new orderVO(tableOrder,orderDetails);

        return ret;
    }

}
