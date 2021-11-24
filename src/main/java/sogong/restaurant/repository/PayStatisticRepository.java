package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.restaurant.domain.PayStatistic;

public interface PayStatisticRepository extends JpaRepository<PayStatistic,Long> {
}
