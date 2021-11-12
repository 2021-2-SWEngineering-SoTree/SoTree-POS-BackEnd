package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.TakeoutOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface TakeoutOrderRepository extends JpaRepository<TakeoutOrder,Long> {

    Optional<TakeoutOrder> findTakeoutOrderByTakeoutTicketNumber(int takeoutTicketNumber);
    List<TakeoutOrder> findTakeoutOrdersByManager(Manager manager);

}