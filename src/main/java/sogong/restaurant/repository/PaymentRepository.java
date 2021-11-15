package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where PaymentId = :id and BranchId = :branchId", nativeQuery = true)
    public Optional<Payment>findByIdAndManager(@Param(value = "id")Long id, @Param(value = "branchId") Long branchId);
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where BranchId = :bid and payTime between :st and :en", nativeQuery = true)
    public List<Payment> findByManagerAndDateBetween(@Param(value = "bid")Long bid, @Param("st") String stdate, @Param("en") String endate);
}
