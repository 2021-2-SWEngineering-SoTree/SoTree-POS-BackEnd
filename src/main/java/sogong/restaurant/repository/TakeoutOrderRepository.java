package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.TakeoutOrder;

import java.util.List;

@Repository
public interface TakeoutOrderRepository extends JpaRepository<TakeoutOrder, Long> {

    List<TakeoutOrder> findAllByManager(Manager manager);

}