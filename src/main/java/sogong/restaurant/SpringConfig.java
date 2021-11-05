package sogong.restaurant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sogong.restaurant.repository.MenuIngredientRepository;
import sogong.restaurant.repository.MenuRepository;
import sogong.restaurant.service.MenuIngredientService;
import sogong.restaurant.service.MenuService;

@Configuration
public class SpringConfig {
    private final MenuRepository menuRepository;
    private final MenuIngredientRepository menuIngredientRepository;

    public SpringConfig(MenuRepository menuRepository, MenuIngredientRepository menuIngredientRepository) {
        this.menuRepository = menuRepository;
        this.menuIngredientRepository = menuIngredientRepository;
    }

    @Bean
    public MenuService menuService() {
        return new MenuService(menuRepository);
    }

    @Bean
    public MenuIngredientService menuIngredientService() {
        return new MenuIngredientService(menuIngredientRepository);
    }


}