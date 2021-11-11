package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    public List<OrderDetail> findAllByMenuOrder(MenuOrder menuOrder);

}
