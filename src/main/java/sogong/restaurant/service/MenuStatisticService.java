package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.MenuStatistic;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.MenuRepository;
import sogong.restaurant.repository.MenuStatisticRepository;
import sogong.restaurant.summary.MenuStatisticWeeklySummary;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class MenuStatisticService {

    @Autowired
    private MenuStatisticRepository menuStatisticRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private MenuRepository menuRepository;

    private List<Map<String,String>> makeRet(List<MenuStatistic> all) {
        List<Map<String, String>> ret = new ArrayList<>();
        Map<String, Long> totalCnt = new HashMap<>();
        for (MenuStatistic ms : all) {
            if (totalCnt.containsKey(ms.getMenu().getMenuName())) {
                Long prev = totalCnt.get(ms.getMenu().getMenuName());
                totalCnt.replace(ms.getMenu().getMenuName(), prev + ms.getQuantity());
            } else {
                totalCnt.put(ms.getMenu().getMenuName(), Long.valueOf(ms.getQuantity()));
            }
        }

        totalCnt.forEach((k, v) -> {
            Map<String, String> one = new HashMap<>();
            one.put("menuName", k);
            one.put("orderQuantity", String.valueOf(v));
            Long price = menuRepository.findMenuByMenuName(k).get().getPrice() * v;
            one.put("price", String.valueOf(price));
            ret.add(one);
        });
        return ret;
    }

    public List<Map<String,String>> getAllByTime(Long branchId, String stDate,String enDate) {
        List<Map<String,String>> ret = new ArrayList<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        List<MenuStatistic> all = menuStatisticRepository.findByMenuIdAndBranchIdAndDateBetween(branchId, stDate, enDate);
        return makeRet(all);
    }


    public List<Map<String,String>> getAllByTimeAndCategory(Long branchId, String stDate,String enDate,String menuCategory){
        List<Map<String,String>> ret = new ArrayList<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        List<MenuStatistic> all = menuStatisticRepository.findByMenuIdAndBranchIdAndMenuCategoryAndDateBetween(branchId, stDate, enDate,menuCategory);
        return makeRet(all);
    }


    public List<Map<String,String>> getAllByDay(Long branchId, Long day){
        List<Map<String,String>> ret = new ArrayList<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        List<MenuStatistic> all = menuStatisticRepository.findByBranchIdAndDay(branchId, day);
        return makeRet(all);
    }

    public List<Map<String,String>> getAllByDayAndCategory(Long branchId, Long day,String menuCategory){
        List<Map<String,String>> ret = new ArrayList<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        List<MenuStatistic> all = menuStatisticRepository.findByBranchIdAndCategoryAndDay(branchId, day,menuCategory);
        return makeRet(all);
    }

    public List<MenuStatisticWeeklySummary> getWeeklyTopFiveMenu(Long branchId){

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if(optionalManager.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        return menuStatisticRepository.findByBranchAndMenuONRecentWeekly(branchId);
    }

}
