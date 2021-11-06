package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.StockDetail;

import java.util.List;
import java.util.Optional;

public interface StockDetailRepository extends JpaRepository<StockDetail,Long> {
    List<StockDetail> findAll();
}

