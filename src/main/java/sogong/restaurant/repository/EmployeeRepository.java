package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByUserAndIsActive(User user, boolean isActive);

    List<Employee> findEmployeesByManagerAndIsActive(Manager manager, boolean isActive);

    @Query(value = "select EmployeeId,commuteState,BranchId,UserId,workSchedule,isActive from Employee where EmployeeId = :id and BranchId = :branchId and isActive=b'1'", nativeQuery = true)
    Optional<Employee> findEmployeeByIdAndManager(@Param(value = "id") Long id, @Param(value = "branchId") Long branchId);

    List<Employee> findAllByManagerAndCommuteStateAndIsActive(Manager manager, boolean commuteState, boolean isActive);

}
