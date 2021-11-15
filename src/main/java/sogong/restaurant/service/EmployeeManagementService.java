package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import sogong.restaurant.domain.CommuteRecord;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.Payment;
import sogong.restaurant.domain.TableOrder;
import sogong.restaurant.repository.CommuteRecordRepository;
import sogong.restaurant.repository.MenuOrderRepository;
import sogong.restaurant.repository.PaymentRepository;
import sogong.restaurant.repository.StockRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class EmployeeManagementService {

    private CommuteRecordRepository commuteRecordRepository;
    private PaymentRepository paymentRepository;
    private MenuOrderRepository menuOrderRepository;
    private StockRepository stockRepository;

    @Autowired
    public EmployeeManagementService(CommuteRecordRepository commuteRecordRepository, PaymentRepository paymentRepository, MenuOrderRepository menuOrderRepository, StockRepository stockRepository) {
        this.commuteRecordRepository = commuteRecordRepository;
        this.paymentRepository = paymentRepository;
        this.menuOrderRepository = menuOrderRepository;
        this.stockRepository = stockRepository;
    }

    public List<Map<String,String>> getAllEmployeeActivity(String criterion, String date, Long branchId){
        List<Map<String,String>> ret = new ArrayList<>();

        String stdate, endate;
        if(date.length()==7){
            stdate = date + "-01 00:00";
            endate = date + "-31 23:59";
        }
        else {
            stdate = date + " 00:00";
            endate = date + " 23:59";
        }

            switch (criterion){
            case "COME" :
                //월


                List<CommuteRecord> all = commuteRecordRepository.findAllByEmployeeIdBetweenTime(branchId, stdate, endate, true);

                for(int i=0;i<all.size();i++){
                    Map<String,String> comRec = new HashMap<>();
                    comRec.put("이름",all.get(i).getEmployee().getUser().getPersonName());
                    comRec.put("ID",all.get(i).getEmployee().getUser().getUsername());
                    comRec.put("출근 일자",all.get(i).getTime());
                    ret.add(comRec);
                }

                break;
            case "OUT" :
                List<CommuteRecord> all1 = commuteRecordRepository.findAllByEmployeeIdBetweenTime(branchId, stdate, endate, false);

                for(int i=0;i<all1.size();i++){
                    Map<String,String> comRec = new HashMap<>();
                    comRec.put("이름",all1.get(i).getEmployee().getUser().getPersonName());
                    comRec.put("ID",all1.get(i).getEmployee().getUser().getUsername());
                    comRec.put("출근 일자",all1.get(i).getTime());
                    ret.add(comRec);
                }
                break;
            case "PAY" :
                List<Payment> all2 = paymentRepository.findByManagerAndDateBetween(branchId, stdate, endate);
                for(int i=0;i<all2.size();i++) {
                    Map<String, String> payRec = new HashMap<>();
                    payRec.put("이름",all2.get(i).getEmployee().getUser().getPersonName());
                    payRec.put("결제 종류",all2.get(i).getMethod());
                    payRec.put("결제 일자",all2.get(i).getPayTime());
                    payRec.put("주문 번호",String.valueOf(all2.get(i).getMenuOrder().getId()));
                    MenuOrder.OrderType orderType = all2.get(i).getMenuOrder().getOrderType();
                    if(orderType.equals(MenuOrder.OrderType.TABLE_ORDER)){
                        TableOrder tableOrder = (TableOrder) all2.get(i).getMenuOrder();
                        payRec.put("자리 번호",String.valueOf(tableOrder.getSeatNumber()));
                    }
                    else{
                        payRec.put("자리 번호","포장");
                    }
                    ret.add(payRec);
                }

                break;
            case "ORDER" :
                List<MenuOrder> all3 = menuOrderRepository.findByManagerAndDateBetween(branchId, stdate, endate);

                for(int i=0;i<all3.size();i++){
                    Map<String, String> orderRec = new HashMap<>();
                    orderRec.put("이름",all3.get(i).getEmployee().getUser().getPersonName());
                    orderRec.put("주문합계",String.valueOf(all3.get(i).getTotalPrice()));
                    orderRec.put("주문번호",String.valueOf(all3.get(i).getId()));
                    MenuOrder.OrderType orderType = all3.get(i).getOrderType();
                    if(orderType.equals(MenuOrder.OrderType.TABLE_ORDER)){
                        TableOrder tableOrder = (TableOrder) all3.get(i);
                                orderRec.put("자리번호",String.valueOf(tableOrder.getSeatNumber()));
                                orderRec.put("주문 종류","테이블");
                    }
                    else{
                        orderRec.put("자리번호","포장");
                        orderRec.put("주문 종류","포장");
                    }
                    ret.add(orderRec);
                }

                break;
            case "STOCK":


                break;
        }


        return ret;
    }
}
