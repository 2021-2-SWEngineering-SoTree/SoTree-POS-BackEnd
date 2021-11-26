package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.TakeoutOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface TakeoutOrderRepository extends JpaRepository<TakeoutOrder, Long> {

    List<TakeoutOrder> findAllByManager(Manager manager);

    Optional<TakeoutOrder> findTakeoutOrderByManagerAndId(Manager manager, Long id);
}