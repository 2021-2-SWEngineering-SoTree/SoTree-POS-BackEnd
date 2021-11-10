package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.domain.StockDetail;

import java.util.List;

public interface StockDetailRepository extends JpaRepository<StockDetail,Long> {
    List<StockDetail> findAll();
    List<StockDetail> findStockDetailByStock(Stock stock);
}