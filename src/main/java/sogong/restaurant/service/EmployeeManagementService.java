package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.CommuteRecordRepository;
import sogong.restaurant.repository.MenuOrderRepository;
import sogong.restaurant.repository.PaymentRepository;
import sogong.restaurant.repository.StockDetailRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EmployeeManagementService {

    private final CommuteRecordRepository commuteRecordRepository;
    private final PaymentRepository paymentRepository;
    private final MenuOrderRepository menuOrderRepository;
    private final StockDetailRepository stockDetailRepository;

    @Autowired
    public EmployeeManagementService(CommuteRecordRepository commuteRecordRepository, PaymentRepository paymentRepository, MenuOrderRepository menuOrderRepository, StockDetailRepository stockDetailRepository) {
        this.commuteRecordRepository = commuteRecordRepository;
        this.paymentRepository = paymentRepository;
        this.menuOrderRepository = menuOrderRepository;
        this.stockDetailRepository = stockDetailRepository;
    }

    public List<Map<String, String>> getAllEmployeeActivity(String criterion, String date, Long branchId) {
        List<Map<String, String>> ret = new ArrayList<>();

        String stdate, endate;
        if (date.length() == 7) {
            stdate = date + "-01 00:00";
            endate = date + "-31 23:59";
        } else {
            stdate = date + " 00:00";
            endate = date + " 23:59";
        }

        switch (criterion) {
            case "COME":
                //월


                List<CommuteRecord> all = commuteRecordRepository.findAllByEmployeeIdBetweenTime(branchId, stdate, endate, true);

                for (int i = 0; i < all.size(); i++) {
                    Map<String, String> comRec = new HashMap<>();
                    comRec.put("이름", all.get(i).getEmployee().getUser().getPersonName());
                    comRec.put("ID", all.get(i).getEmployee().getUser().getUsername());
                    comRec.put("출근 일자", all.get(i).getTime());
                    ret.add(comRec);
                }

                break;
            case "OUT":
                List<CommuteRecord> all1 = commuteRecordRepository.findAllByEmployeeIdBetweenTime(branchId, stdate, endate, false);

                for (int i = 0; i < all1.size(); i++) {
                    Map<String, String> comRec = new HashMap<>();
                    comRec.put("이름", all1.get(i).getEmployee().getUser().getPersonName());
                    comRec.put("ID", all1.get(i).getEmployee().getUser().getUsername());
                    comRec.put("출근 일자", all1.get(i).getTime());
                    ret.add(comRec);
                }
                break;
            case "PAY":
                List<Payment> all2 = paymentRepository.findByManagerAndDateBetween(branchId, stdate, endate);
                for (int i = 0; i < all2.size(); i++) {
                    Map<String, String> payRec = new HashMap<>();
                    if (all2.get(i).getEmployee() == null) {
                        payRec.put("이름", all2.get(i).getManager().getUser().getPersonName());
                    } else {
                        payRec.put("이름", all2.get(i).getEmployee().getUser().getPersonName());
                    }
                    payRec.put("결제 종류", all2.get(i).getMethod());
                    payRec.put("결제 일자", all2.get(i).getPayTime());
                    payRec.put("주문 번호", String.valueOf(all2.get(i).getMenuOrder().getId()));
                    payRec.put("결제 금액", String.valueOf(all2.get(i).getFinalPrice()));
                    MenuOrder.OrderType orderType = all2.get(i).getMenuOrder().getOrderType();
                    if (orderType.equals(MenuOrder.OrderType.TABLE_ORDER)) {
                        TableOrder tableOrder = (TableOrder) all2.get(i).getMenuOrder();
                        payRec.put("자리 번호", String.valueOf(tableOrder.getSeatNumber()));
                    } else {
                        payRec.put("자리 번호", "포장");
                    }
                    ret.add(payRec);
                }

                break;
            case "ORDER":
                List<MenuOrder> all3 = menuOrderRepository.findByManagerAndDateBetween(branchId, stdate, endate);

                for (int i = 0; i < all3.size(); i++) {
                    Map<String, String> orderRec = new HashMap<>();
                    if (all3.get(i).getEmployee() == null) {
                        orderRec.put("이름", all3.get(i).getManager().getUser().getPersonName());
                    } else {
                        orderRec.put("이름", all3.get(i).getEmployee().getUser().getPersonName());
                    }
                    orderRec.put("주문합계", String.valueOf(all3.get(i).getTotalPrice()));
                    orderRec.put("주문번호", String.valueOf(all3.get(i).getId()));
                    orderRec.put("주문일자", all3.get(i).getStartTime());
                    orderRec.put("주문금액", String.valueOf(all3.get(i).getTotalPrice()));
                    MenuOrder.OrderType orderType = all3.get(i).getOrderType();
                    if (orderType.equals(MenuOrder.OrderType.TABLE_ORDER)) {
                        TableOrder tableOrder = (TableOrder) all3.get(i);
                        orderRec.put("자리번호", String.valueOf(tableOrder.getSeatNumber()));
                        orderRec.put("주문 종류", "테이블");
                    } else {
                        orderRec.put("자리번호", "포장");
                        orderRec.put("주문 종류", "포장");
                    }
                    ret.add(orderRec);
                }

                break;
            case "STOCK":
                System.out.println(stdate);
                System.out.println(endate);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                Date stdateFormatted = Date.valueOf(LocalDate.parse(stdate, formatter));
                Date endateFormatted = Date.valueOf(LocalDate.parse(endate, formatter));
                System.out.println(stdateFormatted);
                System.out.println(endateFormatted);
//                java.sql.Date stSqlDate = java.sql.Date.valueOf(stdate);
//                java.sql.Date enSqlDate = java.sql.Date.valueOf(endate);
//                System.out.println(stSqlDate);
                    
                List<StockDetail> all4 = stockDetailRepository.findAllByDateAndBranchId(branchId, stdateFormatted, endateFormatted);

                for (int i = 0; i < all4.size(); i++) {
                    Map<String, String> stockRec = new HashMap<>();
                    Employee employee = all4.get(i).getEmployee();
                    if (employee == null) {
                        stockRec.put("이름", "NULL");
                    } else {
                        stockRec.put("이름", employee.getUser().getPersonName());
                    }
                    stockRec.put("재고번호", String.valueOf(all4.get(i).getStock().getId()));
                    stockRec.put("재고이름", all4.get(i).getStock().getStockName());
                    stockRec.put("변화량", String.valueOf(all4.get(i).getQuantityChanged()));
                    stockRec.put("변경후수량", String.valueOf(all4.get(i).getFinalQuantity()));
                    stockRec.put("수정일자", all4.get(i).getTime());
                    stockRec.put("메모", all4.get(i).getMemo());

                    ret.add(stockRec);
                }

                break;
        }


        return ret;
    }
}
