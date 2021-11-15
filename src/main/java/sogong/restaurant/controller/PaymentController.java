package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.service.PaymentService;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

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


}
