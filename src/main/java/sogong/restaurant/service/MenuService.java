package sogong.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.repository.MenuRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
public class MenuService {

    @Autowired
    private final MenuRepository menuRepository;

    @Transactional
    public Long saveMenu(Menu menu) {
        validateDuplicateMenu(menu); //중복 메뉴 검증
        return menuRepository.save(menu).getId();
    }

    // 같은 지점에서 메뉴 이름 중복 방지
    private void validateDuplicateMenu(Menu menu) {
        menuRepository.findMenuByMenuNameAndManager(menu.getMenuName(), menu.getManager())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 메뉴입니다.");
                });
    }

    public List<Menu> getAllMenu(Manager manager) {
        return menuRepository.findAllByManager(manager);
    }

    public Optional<Menu> getOneMenu(String menuName, Manager manager) {
        return menuRepository.findMenuByMenuNameAndManager(menuName, manager);
    }

    @Transactional
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

}
