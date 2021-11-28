package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.service.MenuStatisticService;
import sogong.restaurant.service.PaymentService;
import sogong.restaurant.summary.*;

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
    public Long makePayment(@RequestBody Map<String, String> param) {

        Long orderId = Long.parseLong(param.get("orderId"));
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String payTime = param.get("payTime");
        String method = param.get("method");
        Long managerId = Long.parseLong(param.get("branchId"));
        int finalPrice = Integer.parseInt(param.get("finalPrice"));

        return paymentService.makeMenu(orderId, employeeId, payTime, method, managerId, finalPrice);
    }


    @PostMapping("/sendToCompany")
    public String sendToCompany(@RequestBody Map<String, String> param) {

        Long paymentId = Long.parseLong(param.get("paymentId"));
        Long branchId = Long.parseLong(param.get("branchId"));

        return paymentService.sendToCompany(paymentId, branchId);
    }

    @PostMapping("/getALLPaymentResults")
    public List<PaymentSummary> getALLPaymentResults(@RequestBody String branchId) {
        return paymentService.getAllByManagerId(Long.parseLong(branchId));
    }

    @PostMapping("/getALLSortedBYDAYFORTOTAL")
    public List<PaymentDaySummary> getALLSortedBYDAYFORTOTAL(@RequestBody String branchId) {

        return paymentService.getSortedBYDAYFORTOTAL(Long.parseLong(branchId));
    }

    @PostMapping("/getALLSortedBYWEEK")
    public List<PaymentWeekSummary> getALLSortedBYWEEK(@RequestBody Map<String, String> param) {

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");

        /*Map<String, Object> response = new HashMap<>();
        response.put("saleSummary", paymentService.getSortedBYWEEK(Long.parseLong(branchId), start, end));
        response.put("orderTypeSummary", paymentService.getWeekOrderTypeSummary(Long.parseLong(branchId), start, end));*/

        return paymentService.getSortedBYWEEK(Long.parseLong(branchId), start, end);
    }

    @PostMapping("/getALLSortedByMonth")
    public List<PaymentMonthSummary> getALLSortedByMonth(@RequestBody Map<String, String> param) {

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");

        /*Map<String, Object> response = new HashMap<>();
        response.put("saleSummary", paymentService.getSortedByMonth(Long.parseLong(branchId), start, end));
        response.put("orderTypeSummary", paymentService.getMonthOrderTypeSummary(Long.parseLong(branchId), start, end));*/

        return paymentService.getSortedByMonth(Long.parseLong(branchId), start, end);
    }

    @PostMapping("/getTodaySummarySale")
    public Map<String, Object> getTodaySummarySale(@RequestBody Map<String, String> param) {

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");

        Map<String, Object> response = new HashMap<>();

        response.put("saleSummary", paymentService.getTodaySummarySale(Long.parseLong(branchId), start, end));
        response.put("recentSevenDays", paymentService.getRecent7DaysSaleSummary(Long.parseLong(branchId)));
        response.put("DaySummary", paymentService.getSortedBYDAYFORTOTAL(Long.parseLong(branchId)));
        response.put("TopFiveMenu", menuStatisticService.getWeeklyTopFiveMenu(Long.parseLong(branchId)));
        response.put("OrderTypeSummary", paymentService.getTodayOrderTypeSummary(Long.parseLong(branchId)));

        return response;
    }

    @PostMapping("/getWeeklySaleInfo")
    public List<PaymentWeeklySummary> getWeeklySaleInfo(@RequestBody String branchId) {
        return paymentService.getWeeklySaleSummary(Long.parseLong(branchId));
    }

    @PostMapping("/getRecent7DaysSaleInfo")
    public List<PaymentWeeklySummary> getRecent7DaysSaleInfo(@RequestBody String branchId) {
        return paymentService.getRecent7DaysSaleSummary(Long.parseLong(branchId));
    }

    @PostMapping("/combinePay")
    public String combinePay(@RequestBody Map<String, String> param) {

        //여기서 실제로 카드회사와의 결제가 진행되어야 함. 지금은 그냥 다 되었다고 가정

        String payTime = param.get("payTime");
        String method = param.get("method");
        String price = param.get("price");
        Long branchId = Long.parseLong(param.get("branchId"));


        return paymentService.combinePay(payTime, method, Integer.parseInt(price), branchId);
    }

    @PostMapping("/getDaySaleInfo")
    public Map<String, Object> getDaySaleInfo(@RequestBody Map<String, String> param) {

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");

        Map<String, Object> response = new HashMap<>();

        response.put("dayOfWeekSaleSummary", paymentService.getSortedByDayOfWeekSaleSummary(Long.parseLong(branchId), start, end));
        response.put("daySaleSummary", paymentService.getDaySaleSummary(Long.parseLong(branchId), start, end));
        response.put("orderTypeSummary", paymentService.getDayOrderTypeSummary(Long.parseLong(branchId), start, end));
        response.put("sumSummary", paymentService.getSumSaleSummary(Long.parseLong(branchId), start, end));

        return response;
    }

    @PostMapping("/getSaleInfoBetween")
    public Map<String, Object> getSaleInfoBetween(@RequestBody Map<String, String> param) {

        String start = param.get("start");
        String end = param.get("end");
        String branchId = param.get("branchId");

        Map<String, Object> response = new HashMap<>();

        response.put("hourSummary", paymentService.getHourSaleSummary(Long.parseLong(branchId), start, end));
        response.put("dateSaleSummary", paymentService.getSortedByDateSaleSummary(Long.parseLong(branchId), start, end));
        response.put("sumSummary", paymentService.getSumSaleSummary(Long.parseLong(branchId), start, end));
        response.put("orderTypeSumSummary", paymentService.getBetweenInputOrderTypeSumSummary(Long.parseLong(branchId), start, end));
        return response;
    }

    @PostMapping("/getReceipt")
    List<Map<String, String>> getReceipt(@RequestBody Map<String, String> param) {
        Long branchId = Long.parseLong(param.get("branchId"));
        Long orderId = Long.parseLong(param.get("orderId"));
        Long paymentId = Long.parseLong(param.get("paymentId"));

        return paymentService.getReceipt(branchId, orderId, paymentId);
    }

    @PostMapping("/getCustomerAvgTime")
    public Map<String, Object>  getTodaySummarySale(@RequestBody String branchId){

        Map<String, Object> response = new HashMap<>();

        response.put("allDay", paymentService.getCustomerAvgTimeALLTime(Long.parseLong(branchId)));
        response.put("weekend", paymentService.getCustomerAvgTimeWeekend(Long.parseLong(branchId)));
        response.put("weekday", paymentService.getCustomerAvgTimeWeekday(Long.parseLong(branchId)));
        response.put("dayAllTime", paymentService.getSortedByDayAllTime(Long.parseLong(branchId)));
        response.put("dayDinner", paymentService.getSortedByDayDinner(Long.parseLong(branchId)));
        response.put("dayLunch", paymentService.getSortedByDayLunch(Long.parseLong(branchId)));

        return response;
    }
}
