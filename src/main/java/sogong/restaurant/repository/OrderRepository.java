package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuOrder;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<MenuOrder,Long> {

    public List<MenuOrder> findMenuOrdersByManager(Manager manager);
}
