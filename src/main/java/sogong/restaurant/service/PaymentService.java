package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.*;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MenuOrderRepository menuOrderRepository;
    @Autowired
    private ManagerRepository managerRepository;


    public int getFinalPrice(int totalPrice){
        return totalPrice;
    }

    public Long makeMenu(Long orderId, Long employeeId,String payTime,String method,Long managerId){
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        Optional<MenuOrder> menuOrder=menuOrderRepository.findById(orderId);
        if(menuOrder.isEmpty()) throw new NoSuchElementException("존재하지 않는 주문정보입니다.");

        Optional<Employee> employee = employeeRepository.findEmployeeByIdAndManager(employeeId,managerId);
        if(employee.isEmpty()) throw new NoSuchElementException("존재하지 않는 직원입니다.");

        Payment payment = new Payment();
        payment.setEmployee(employee.get());
        payment.setManager(optionalManager.get());
        payment.setMenuOrder(menuOrder.get());
        payment.setPayTime(payTime);
        payment.setMethod(method);
        payment.setFinalPrice(getFinalPrice(menuOrder.get().getTotalPrice()));

        paymentRepository.save(payment);

        return payment.getId();
    }

    public String sendToCompany(Long paymentId,Long branchId){
        //어떤 방식으로 어느정도 해야하는지...
        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new
                NoSuchElementException("해당하는 가게가 없습니다.");

        Optional<Payment> payment = paymentRepository.findByIdAndManager(paymentId,branchId);

        if(payment.isEmpty()) throw new
                NoSuchElementException("해당하는 결제 정보가 없습니다.");

        //여기서 통계에 관한 처리를 해줘야 할듯

        MenuOrder menuOrder = payment.get().getMenuOrder();
        if(menuOrder.getOrderType().equals(MenuOrder.OrderType.TABLE_ORDER.getValue())){
            TableOrder tableOrder = (TableOrder) menuOrder;
            tableOrder.setIsSeated(false);
            menuOrderRepository.save(tableOrder);
        }


        return "OK";
    }

}
