package sogong.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.repository.MenuRepository;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@AllArgsConstructor
public class MenuService {

    @Autowired
    private final MenuRepository menuRepository;
    @Autowired
    private final ManagerRepository managerRepository;

    @Transactional
    public Long saveMenu(Menu menu) {
        validateDuplicateMenu(menu); //중복 메뉴 검증
        return menuRepository.save(menu).getId();
    }

    @Transactional
    public Long updateMenuWithoutName(Menu menu) {
        return menuRepository.save(menu).getId();
    }

    // 같은 지점에서 메뉴 이름 중복 방지
    private void validateDuplicateMenu(Menu menu) {
        menuRepository.findMenuByMenuNameAndManager(menu.getMenuName(), menu.getManager())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 메뉴입니다.");
                });
    }

    // Active한 메뉴만 뽑기
    public List<Menu> getAllActiveMenu(Manager manager) {
        return menuRepository.findAllByManagerAndActive(manager, Boolean.TRUE);
    }

    public Optional<Menu> getOneMenu(Manager manager, String menuName) {
        return menuRepository.findMenuByMenuNameAndManager(menuName, manager);
    }

    @Transactional
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public Map<String, Integer> getMeanTimeByCategory(Long branchId, String category) {
        Map<String, Integer> ret = new HashMap<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("가게가 존재하지 않습니다.");
        }

        Manager manager = optionalManager.get();

        List<Menu> all = menuRepository.findAllByManagerAndMenuCategoryAndActive(manager, category, true);

        for (Menu menu : all) {
            int meanTime = -1;
            if (menu.getTotalQuantity() != 0) {
                meanTime = (int) (menu.getTotalTime() / menu.getTotalQuantity());
            }
            ret.put(menu.getMenuName(), meanTime);
        }

        return ret;
    }

}
