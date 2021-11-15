package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.domain.CommuteRecord;
import sogong.restaurant.service.EmployeeManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/manage")
public class EmployeeManagementController {

    @Autowired
    private EmployeeManagementService employeeManagementService;

    @PostMapping("/EmployeeActivity")
    public List<Map<String,String>> getAllEmployeeActivity(@RequestBody Map<String,String> param){
        String criterion = param.get("criterion");
        //2021-10
        //2021-10-11 두가지 경우
        String date = param.get("date");
        Long branchId = Long.parseLong(param.get("BranchId"));

        return employeeManagementService.getAllEmployeeActivity(criterion,date,branchId);
    }


}
