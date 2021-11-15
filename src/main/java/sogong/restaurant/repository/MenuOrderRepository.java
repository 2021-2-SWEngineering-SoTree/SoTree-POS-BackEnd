package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.Payment;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder,Long> {
    //public Optional<MenuOrder>findById(Long)
    @Query(value = "select orderType,OrderId,endTime,startTime,totalPrice,isSeated,seatNumber,takeoutTicketNumber,EmployeeId,BranchId from MenuOrder where BranchId = :bid and startTime between :st and :en", nativeQuery = true)
    public List<MenuOrder> findByManagerAndDateBetween(@Param(value = "bid")Long bid, @Param("st") String stdate, @Param("en") String endate);

}
