package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.repository.*;
import sogong.restaurant.service.MenuStatisticService;
import sogong.restaurant.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MenuStatisticService menuStatisticService;

    @PostMapping("/makePayment")
    public Long makePayment(@RequestBody Map<String,String> param){

        Long orderId = Long.parseLong(param.get("orderId"));
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String payTime = param.get("payTime");
        String method = param.get("method");
        Long managerId = Long.parseLong(param.get("branchId"));

        return paymentService.makeMenu(orderId,employeeId,payTime,method,managerId);
    }


    @PostMapping("/sendToCompany")
    public String sendToCompany(@RequestBody Map<String,String> param){

        Long paymentId = Long.parseLong(param.get("paymentId"));
        Long branchId = Long.parseLong(param.get("branchId"));

        return paymentService.sendToCompany(paymentId,branchId);
    }

    @PostMapping("/getALLPaymentResults")
    public List<PaymentSummary> getALLPaymentResults(@RequestBody String branchId){
        return paymentService.getAllByManagerId(Long.parseLong(branchId));
    }

    @PostMapping("/getALLSortedBYDAYFORTOTAL")
    public List<PaymentDaySummary> getALLSortedBYDAYFORTOTAL(@RequestBody String branchId){

        return paymentService.getSortedBYDAYFORTOTAL(Long.parseLong(branchId));
    }

    @PostMapping("/getALLSortedBYWEEK")
    public List<PaymentWeekSummary> getALLSortedBYWEEK(@RequestBody Map<String,String> param){

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");
        return paymentService.getSortedBYWEEK(Long.parseLong(branchId), start, end);
    }

    @PostMapping("/getALLSortedByMonth")
    public List<PaymentMonthSummary> getALLSortedByMonth(@RequestBody Map<String,String> param){

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");
        return paymentService.getSortedByMonth(Long.parseLong(branchId), start, end);
    }

    @PostMapping("/getTodaySummarySale")
    public Map<String, Object>  getTodaySummarySale(@RequestBody Map<String,String> param){

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");

        Map<String, Object> response = new HashMap<>();

        response.put("saleSummary", paymentService.getTodaySummarySale(Long.parseLong(branchId), start, end));
        response.put("recentSevenDays", paymentService.getRecent7DaysSaleSummary(Long.parseLong(branchId)));
        response.put("DaySummary", paymentService.getSortedBYDAYFORTOTAL(Long.parseLong(branchId)));
        response.put("TopFiveMenu", menuStatisticService.getWeeklyTopFiveMenu(Long.parseLong(branchId)));

        return response;
    }

    @PostMapping("/getWeeklySaleInfo")
    public List<PaymentWeeklySummary> getWeeklySaleInfo(@RequestBody String branchId){
        return paymentService.getWeeklySaleSummary(Long.parseLong(branchId));
    }

    @PostMapping("/getRecent7DaysSaleInfo")
    public List<PaymentWeeklySummary> getRecent7DaysSaleInfo(@RequestBody String branchId){
        return paymentService.getRecent7DaysSaleSummary(Long.parseLong(branchId));
    }

    @PostMapping("/combinePay")
    public String combinePay(@RequestBody Map<String,String> param){

        //여기서 실제로 카드회사와의 결제가 진행되어야 함. 지금은 그냥 다 되었다고 가정

        String payTime = param.get("payTime");
        String method = param.get("method");
        String price = param.get("price");
        Long branchId = Long.parseLong(param.get("branchId"));



        return paymentService.combinePay(payTime,method,Integer.parseInt(price),branchId);
    }

}
