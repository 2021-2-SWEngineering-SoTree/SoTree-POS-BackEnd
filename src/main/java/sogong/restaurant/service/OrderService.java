package sogong.restaurant.service;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.VO.orderVO;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;
import sogong.restaurant.domain.TableOrder;
import sogong.restaurant.repository.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class OrderService {

    OrderDetailRepository orderDetailRepository;
    TableOrderRepository tableOrderRepository;
    TakeoutOrderRepository takeoutOrderRepository;
    ManagerRepository managerRepository;

    @Autowired
    public OrderService(sogong.restaurant.repository.OrderDetailRepository orderDetailRepository, sogong.restaurant.repository.TableOrderRepository tableOrderRepository, sogong.restaurant.repository.TakeoutOrderRepository takeoutOrderRepository, ManagerRepository managerRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.tableOrderRepository = tableOrderRepository;
        this.takeoutOrderRepository = takeoutOrderRepository;
        this.managerRepository = managerRepository;
    }

    public orderVO getTableOrderByBranchIdAndSeatNumber(Long BranchId, int seatNumber){

        //나중에 현재 식사하고 있는 주문에 대한 정보 받아오는 것 로직 추가해야함.
        TableOrder tableOrder = new TableOrder();

        List<TableOrder> tableOrderList = tableOrderRepository.findTableOrdersByManager(managerRepository.findById(BranchId).get());

        int idx = 0;

        for(int i=0;i<tableOrderList.size();i++){
            Optional<TableOrder> tableOrder1 = tableOrderRepository.findBySeatNumber(seatNumber);
            if(tableOrder1.isPresent()){
                tableOrder= tableOrder1.get();
                idx=i;
            }
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findAllByMenuOrder(tableOrderList.get(idx));

        orderVO ret = new orderVO(tableOrder,orderDetails);

        return ret;
    }

}
