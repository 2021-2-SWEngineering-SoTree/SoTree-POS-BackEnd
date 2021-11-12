package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findAllByMenuOrder(MenuOrder menuOrder);
    Optional<List<OrderDetail>> findOrderDetailByMenuOrder(MenuOrder menuorder);
    Optional<List<OrderDetail>> findOrderDetailByMenu(Menu menu);
}


