package sogong.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sogong.restaurant.repository.*;
import sogong.restaurant.service.MenuIngredientService;
import sogong.restaurant.service.MenuService;

@Configuration
public class SpringConfig {

    private final MenuRepository menuRepository;
    private final MenuIngredientRepository menuIngredientRepository;
    private final StockRepository stockRepository;
    private final StockDetailRepository stockDetailRepository;
    private final ManagerRepository managerRepository;

    public SpringConfig(MenuRepository menuRepository, MenuIngredientRepository menuIngredientRepository, StockRepository stockRepository, StockDetailRepository stockDetailRepository,ManagerRepository managerRepository) {
        this.menuRepository = menuRepository;
        this.menuIngredientRepository = menuIngredientRepository;
        this.stockRepository = stockRepository;
        this.stockDetailRepository = stockDetailRepository;
        this.managerRepository = managerRepository;
    }

    @Bean
    public MenuService menuService() {
        return new MenuService(menuRepository,managerRepository);
    }

    @Bean
    public MenuIngredientService menuIngredientService() {
        return new MenuIngredientService(menuIngredientRepository);
    }

}