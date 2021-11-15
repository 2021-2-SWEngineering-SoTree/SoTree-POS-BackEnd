package sogong.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.service.CommuteService;

import java.util.Map;

@RestController()
@Slf4j
public class CommuteController {

    @Autowired
    private CommuteService employeeService;


    @PostMapping("/recordCome")
    String recordCome(@RequestBody Map<String, String> param){
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String commingTime = param.get("time");
        Long branchId = Long.parseLong(param.get("branchId"));

        return employeeService.recordCome(employeeId, commingTime,branchId);
    }

    @PostMapping("/recordOut")
    String recordOut(@RequestBody Map<String, String> param){
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String commingTime = param.get("time");
        Long branchId = Long.parseLong(param.get("branchId"));

        return employeeService.recordOut(employeeId, commingTime,branchId);
    }

}
