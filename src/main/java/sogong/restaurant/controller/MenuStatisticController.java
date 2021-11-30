package sogong.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.service.MenuStatisticService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/menuStatistic")
public class MenuStatisticController {

    @Autowired
    private MenuStatisticService menuStatisticService;

    @PostMapping("/getAll")
    public List<Map<String,String>>getAllByTime(@RequestBody Map<String,String>param){
        Long branchId = Long.parseLong(param.get("branchId"));
        String stDate = param.get("stDate");
        String enDate = param.get("enDate");
        //String menuName = param.get("menuName");
        String menuCategory = param.get("menuCategory");
        if(menuCategory==null){
            return menuStatisticService.getAllByTime(branchId,stDate,enDate);
        }
        else return menuStatisticService.getAllByTimeAndCategory(branchId,stDate,enDate,menuCategory);

    }


    @PostMapping("/getByDay")
    public List<Map<String,String>>getByDay(@RequestBody Map<String,String>param){
        Long branchId = Long.parseLong(param.get("branchId"));
        Long day = Long.parseLong(param.get("day"));

        String menuCategory = param.get("menuCategory");
        if(menuCategory==null){
            return menuStatisticService.getAllByDay(branchId,day);
        }
        else return menuStatisticService.getAllByDayAndCategory(branchId,day,menuCategory);

    }



}
