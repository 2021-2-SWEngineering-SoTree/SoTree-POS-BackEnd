package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.*;
import sogong.restaurant.summary.*;

import javax.transaction.Transactional;
import java.util.*;

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
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuStatisticRepository menuStatisticRepository;
    @Autowired
    private PayStatisticRepository payStatisticRepository;

    public int getFinalPrice(int totalPrice){
        return totalPrice;
    }

    public Long makeMenu(Long orderId, Long employeeId,String payTime,String method,Long managerId){
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        Optional<MenuOrder> menuOrder=menuOrderRepository.findById(orderId);
        if(menuOrder.isEmpty()) throw new NoSuchElementException("존재하지 않는 주문정보입니다.");

        Payment payment = new Payment();
        if(employeeId==-1){
            payment.setEmployee(null);
        }
        else{
            Optional<Employee> employee = employeeRepository.findEmployeeByIdAndManager(employeeId,managerId);
            if(employee.isEmpty()) throw new NoSuchElementException("존재하지 않는 직원입니다.");

            payment.setEmployee(employee.get());
        }

        payment.setManager(optionalManager.get());
        payment.setMenuOrder(menuOrder.get());
        payment.setPayTime(payTime);
        payment.setMethod(method);
        payment.setFinalPrice(getFinalPrice(menuOrder.get().getTotalPrice()));

        paymentRepository.save(payment);

        if(!method.equals("복합")){
            combinePay(payTime,method,menuOrder.get().getTotalPrice(),managerId);
        }

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

        MenuOrder menuOrder = payment.get().getMenuOrder();
        //여기서 통계에 관한 처리를 해줘야 할듯
        String stdate = payment.get().getPayTime().substring(0,10);
        System.out.println("stdate = " + stdate);

        for(OrderDetail orderDetail : menuOrder.getOrderDetailList()){
            Menu menu = orderDetail.getMenu();
            Optional<MenuStatistic> menuStatistic = menuStatisticRepository.findByMenuIdAndBranchIdAndDate(branchId,menu.getId(),stdate);
            //현재 날짜의 메뉴 통계가 없다면 새로 만들어야 함.
            if(menuStatistic.isEmpty()) {
                MenuStatistic menuStatistic1 = new MenuStatistic();
                menuStatistic1.setMenu(menu);
                menuStatistic1.setQuantity(orderDetail.getQuantity());
                menuStatistic1.setManager(optionalManager.get());
                menuStatistic1.setDate(stdate);
                menuStatisticRepository.save(menuStatistic1);
            }
            //있으면 업데이트해야함.
            else{
                menuStatistic.get().setQuantity(menuStatistic.get().getQuantity() + orderDetail.getQuantity());
                menuStatisticRepository.save(menuStatistic.get());
            }
        }



        //테이블오더 자리 비활성화
        if(menuOrder.getOrderType().equals(MenuOrder.OrderType.TABLE_ORDER)){
            TableOrder tableOrder = (TableOrder) menuOrder;
            tableOrder.setIsSeated(false);
            menuOrderRepository.save(tableOrder);
        }


        return "OK";
    }

    public List<PaymentSummary> getAllByManagerId(Long branchId) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findAllByManager(branchId);
    }

    public List<PaymentDaySummary> getSortedBYDAYFORTOTAL(Long branchId) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findAllByManagerAndPayTimeFROMALLBYDAY(branchId);
    }

    public List<PaymentWeekSummary> getSortedBYWEEK(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findAllByManagerAndPayTimeFROMWEEK(branchId, start, end);
    }

    public List<PaymentMonthSummary> getSortedByMonth(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findAllByManagerAndPayTimeFromMonth(branchId, start, end);
    }

    public PayMentTodaySummary getTodaySummarySale(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerToday(branchId, start, end);
    }

    public List<PaymentWeeklySummary> getWeeklySaleSummary(Long branchId) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerAAndPayTimeFromWeekly(branchId);
    }

    public List<PaymentWeeklySummary> getRecent7DaysSaleSummary(Long branchId) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerAndPayTimeFROMRecent7Days(branchId);
    }

    public String combinePay(String payTime, String method, int price, Long branchId){

        if(managerRepository.findById(branchId).isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        PayStatistic payStatistic = new PayStatistic();

        payStatistic.setPayTime(payTime);
        payStatistic.setPrice(price);
        payStatistic.setManager(managerRepository.findById(branchId).get());
        payStatistic.setMethod(method);

        payStatisticRepository.save(payStatistic);
        return "OK";
    }

    public List<PaymentDaySummary> getSortedByDayOfWeekSaleSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findAllByManagerAndPayTimeSortedByDayOfWeekBetween(branchId, start, end);
    }

    public List<PaymentDaySummary> getDaySaleSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findALlByManagerAndPayTimeSortedByDayBetween(branchId, start, end);
    }

    public List<PaymentHourSummary> getHourSaleSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findALLByManagerAndPayTimeSortedByHourBetween(branchId, start, end);
    }
    public List<PaymentDateSummary> getSortedByDateSaleSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerAndPayTimeSortedByDateBetweenInput(branchId, start, end);
    }
    public List<PaymentSumSummary> getSumSaleSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerAndPayTimeSumSummaryBetweenInput(branchId, start, end);
    }

    public List<PaymentMonthOrderTypeSummary> getMonthOrderTypeSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByMangerANDOrderIdANDPayTimeANDOrderTypeMonthSummary(branchId, start, end);
    }

    public List<PaymentTodayOrderTypeSummary> getTodayOrderTypeSummary(Long branchId) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByMangerANDOrderIdANDPayTimeANDOrderTypeTodaySummary(branchId);
    }

    public List<PaymentTodayOrderTypeSummary> getWeekOrderTypeSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerANDOrderIdANDPayTimeANDOrderTypeWeekSummary(branchId, start, end);
    }

    public List<PaymentTodayOrderTypeSummary> getDayOrderTypeSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerANDOrderIdANDPayTimeANDOrderTypeDaySummary(branchId, start, end);
    }

    public List<PaymentTodayOrderTypeSummary> getBetweenInputOrderTypeSumSummary(Long branchId, String start, String end) {

        Optional<Manager> optionalManager = Optional.ofNullable(managerRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 가게입니다.")));

        return paymentRepository.findByManagerAndOrderIdAndPayTimeAndOrderTypeBetweenInputSumSummary(branchId, start, end);
    }

    public List<Map<String,String>>getReceipt(Long branchId, Long orderId, Long paymentId){
        List<Map<String,String>> ret= new ArrayList<>();
        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        Manager manager = optionalManager.get();

        Optional<MenuOrder> optionalMenu = menuOrderRepository.findByIdAndAndManager(orderId, manager);
        if(optionalMenu.isEmpty()) throw new NoSuchElementException("존재하지 않는 주문정보입니다.");
        MenuOrder menuOrder = optionalMenu.get();

        Optional<Payment> optionalPayment = paymentRepository.findByIdAndManager(paymentId, branchId);
        if(optionalPayment.isEmpty()) throw new NoSuchElementException("존재하지 않는 결제정보입니다.");
        Payment payment = optionalPayment.get();

        Map<String,String> info = new HashMap<>();

        info.put("결제일시",payment.getPayTime());
        info.put("결제 금액",String.valueOf(payment.getFinalPrice()));
        if(menuOrder.getEmployee()==null)
            info.put("담당자","null");
        else
            info.put("담당자",menuOrder.getEmployee().getUser().getPersonName());
        info.put("결제 방법",payment.getMethod());
        info.put("매장 전화번호",manager.getBranchPhoneNumber());
        info.put("매장 이름",manager.getStoreName());
        info.put("대표",manager.getUser().getPersonName());
        ret.add(info);

        for(OrderDetail orderDetail : menuOrder.getOrderDetailList()){
            Map<String,String> one = new HashMap<>();

            one.put("메뉴 이름",orderDetail.getMenu().getMenuName());
            one.put("수량",String.valueOf(orderDetail.getQuantity()));
            one.put("금액",String.valueOf(orderDetail.getQuantity() * orderDetail.getMenu().getPrice()));

            ret.add(one);
        }

        return ret;
    }

}
