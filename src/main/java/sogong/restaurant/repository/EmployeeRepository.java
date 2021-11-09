package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>{
}
