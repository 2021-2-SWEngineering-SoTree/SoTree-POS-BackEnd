package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;
import sogong.restaurant.summary.OrderDetailSummary;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Optional<List<OrderDetail>> findOrderDetailByMenuOrder(MenuOrder menuorder);

    Optional<List<OrderDetail>> findOrderDetailByMenuOrderAndMenu(MenuOrder menuOrder, Menu menu);

    List<OrderDetailSummary> findAllByMenuOrder(MenuOrder menuOrder);
}



