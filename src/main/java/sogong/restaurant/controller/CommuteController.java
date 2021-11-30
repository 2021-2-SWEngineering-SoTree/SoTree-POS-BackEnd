package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.VO.CommuteVO;
import sogong.restaurant.domain.CommuteRecord;
import sogong.restaurant.service.CommuteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController()
@Slf4j
@RequestMapping("/commute")
public class CommuteController {

    @Autowired
    private CommuteService commuteService;


    @PostMapping("/recordCome")
    String recordCome(@RequestBody Map<String, String> param) {
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String commingTime = param.get("time");
        Long branchId = Long.parseLong(param.get("branchId"));

        return commuteService.recordCome(employeeId, commingTime, branchId);
    }

    @PostMapping("/recordOut")
    String recordOut(@RequestBody Map<String, String> param) {
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String commingTime = param.get("time");
        Long branchId = Long.parseLong(param.get("branchId"));

        return commuteService.recordOut(employeeId, commingTime, branchId);
    }

    @PostMapping("/getOneEmployeeCommuteRecord/{branchId}/{employeeId}")
    List<CommuteVO> getOneEmployeeCommuteRecord(@PathVariable(value = "branchId") Long branchId, @PathVariable(value = "employeeId") Long employeeId) {
        List<CommuteVO> commuteVOList = new ArrayList<>();

        List<CommuteRecord> commuteRecordList = commuteService.getOneEmployeeRecords(employeeId, branchId);

        for (CommuteRecord commuteRecord : commuteRecordList) {
            CommuteVO commuteVO = new CommuteVO();

            commuteVO.setIsComing(commuteRecord.getIsComing());
            commuteVO.setTime(commuteRecord.getTime());

            commuteVOList.add(commuteVO);
        }

        return commuteVOList;
    }

    @PostMapping("/getAllCommuteDay")
    public List<Map<String,String>> getAllCommuteDay(@RequestBody Map<String,String> param) throws Exception{
        Long branchId = Long.valueOf(param.get("branchId"));
        String month = param.get("month");


        return commuteService.getAllCommuteDay(branchId,month);
    }

    @PostMapping("/findCommuteByEmployee")
    public Map<String,String> findCommuteByManager(@RequestBody Long branchId){
        return commuteService.findCommuteByManager(branchId);
    }


}
