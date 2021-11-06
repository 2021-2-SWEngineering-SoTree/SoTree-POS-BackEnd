package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {
    public Optional<Stock> findStockByStockName(String StockName);
    public List<Stock> findAll();
}
