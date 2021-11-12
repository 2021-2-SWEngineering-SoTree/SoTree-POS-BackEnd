package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {
    Optional<Stock> findStockByStockName(String StockName);
    List<Stock> findAllByManager(Manager manager);
    List<Stock> findAllById(Long id);
}
