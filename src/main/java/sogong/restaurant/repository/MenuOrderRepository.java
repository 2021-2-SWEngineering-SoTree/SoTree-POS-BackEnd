package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.MenuOrder;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder,Long> {
    //public Optional<MenuOrder>findById(Long)
}
