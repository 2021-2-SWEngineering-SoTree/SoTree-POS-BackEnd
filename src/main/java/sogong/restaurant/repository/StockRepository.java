package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.summary.StockSummary;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findStockByManagerAndStockName(Manager manager, String stockName);
    Optional<Stock> findStockByManagerAndId(Manager manager, Long stockId);

    List<Stock> findAllByManagerAndActive(Manager manager, Boolean active);
    List<StockSummary> findAllByManager(Manager manager);
    List<Stock> findAllById(Long id);
}
