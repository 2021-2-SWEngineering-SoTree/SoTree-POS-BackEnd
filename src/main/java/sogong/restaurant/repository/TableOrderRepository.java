package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.TableOrder;

import java.util.Optional;

@Repository
public interface TableOrderRepository extends JpaRepository<TableOrder,Long> {

    Optional<TableOrder> findByMenuOrderAndSeatNumber(MenuOrder menuOrder,int seatNumber);
}