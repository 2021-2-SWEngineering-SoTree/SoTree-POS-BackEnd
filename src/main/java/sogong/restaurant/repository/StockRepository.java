package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findStockByManagerAndStockName(Manager manager, String stockName);

    List<Stock> findAllByManagerAndActive(Manager manager, Boolean active);

    List<Stock> findAllById(Long id);
}
