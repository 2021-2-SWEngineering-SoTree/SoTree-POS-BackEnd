package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.repository.MenuIngredientRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class MenuIngredientService {

    private MenuIngredientRepository menuIngredientRepository;

    @Autowired
    public MenuIngredientService(MenuIngredientRepository menuIngredientRepository) {
        this.menuIngredientRepository = menuIngredientRepository;
    }

    @Transactional
    public Long saveMenuIngredient(MenuIngredient menuIngredient) {
        return menuIngredientRepository.save(menuIngredient).getId();
    }

    public Long addMenuIngredient(MenuIngredient menuIngredient) {
        validateDuplicateMenu(menuIngredient); //중복 재료 검증
        menuIngredientRepository.save(menuIngredient);
        return menuIngredient.getId();
    }

    private void validateDuplicateMenu(MenuIngredient menuIngredient) {
        menuIngredientRepository.findMenuIngredientByIngredientName(menuIngredient.getIngredientName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 메뉴입니다.");
                });
    }

//    public List<MenuIngredient> getAllMenuIngredient(){
//        return menuIngredientRepository.findAll();
//    }
//
//    public Optional<MenuIngredient> getOneMenuIngredient(String ingredientName) { return menuIngredientRepository.findMenuIngredientByIngredientName(ingredientName); }

    @Transactional
    public void deleteMenuIngredient(Long id) {
        menuIngredientRepository.deleteById(id);
    }

}
