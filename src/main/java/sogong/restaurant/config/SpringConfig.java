package sogong.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sogong.restaurant.repository.MenuIngredientRepository;
import sogong.restaurant.repository.MenuRepository;
import sogong.restaurant.repository.StockDetailRepository;
import sogong.restaurant.repository.StockRepository;
import sogong.restaurant.service.MenuIngredientService;
import sogong.restaurant.service.MenuService;

@Configuration
public class SpringConfig {

    private final MenuRepository menuRepository;
    private final MenuIngredientRepository menuIngredientRepository;
    private final StockRepository stockRepository;
    private final StockDetailRepository stockDetailRepository;

    public SpringConfig(MenuRepository menuRepository, MenuIngredientRepository menuIngredientRepository, StockRepository stockRepository, StockDetailRepository stockDetailRepository) {
        this.menuRepository = menuRepository;
        this.menuIngredientRepository = menuIngredientRepository;
        this.stockRepository = stockRepository;
        this.stockDetailRepository = stockDetailRepository;
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