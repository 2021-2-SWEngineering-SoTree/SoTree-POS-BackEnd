package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.TableOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableOrderRepository extends JpaRepository<TableOrder, Long> {

    Optional<TableOrder> findTableOrderBySeatNumber(int seatNumber);

    Optional<TableOrder> findTableOrderByManagerAndId(Manager manager, Long id);

    List<TableOrder> findAllByManager(Manager manager);

}
