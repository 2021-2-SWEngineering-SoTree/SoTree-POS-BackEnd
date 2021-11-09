package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.repository.MenuRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class MenuService {

    private MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository){
        this.menuRepository= menuRepository;
    }

    @Transactional
    public Long saveMenu(Menu menu) {
        return menuRepository.save(menu).getId();
    }

    @Transactional
    public Long addMenu(Menu menu) {
        validateDuplicateMenu(menu); //중복 메뉴 검증
        menuRepository.save(menu);
        return menu.getId();
    }

    private void validateDuplicateMenu(Menu menu) {
        menuRepository.findMenuByMenuName(menu.getMenuName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 메뉴입니다.");
                });
    }

    public List<Menu> getAllMenu(Manager manager){
        return menuRepository.findAllByManager(manager);
    }

    public Optional<Menu> getOneMenu(String menuName) { return menuRepository.findMenuByMenuName(menuName); }

    @Transactional
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

}
