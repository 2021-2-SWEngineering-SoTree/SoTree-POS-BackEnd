package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.repository.MenuIngredientRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class MenuIngredientService {

    private MenuIngredientRepository menuIngredientRepository;

    @Autowired
    public MenuIngredientService(MenuIngredientRepository menuIngredientRepository) {
        this.menuIngredientRepository = menuIngredientRepository;
    }

    @Transactional
    public Long addMenuIngredient(MenuIngredient menuIngredient) {
        menuIngredientRepository.save(menuIngredient);
        return menuIngredient.getId();
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

    public List<MenuIngredient> getMenuIngredientByMenu(Menu menu){return menuIngredientRepository.findMenuIngredientsByMenu(menu);}

}
