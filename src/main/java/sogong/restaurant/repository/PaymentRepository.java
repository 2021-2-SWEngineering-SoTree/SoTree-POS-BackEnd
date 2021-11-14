package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {


}
