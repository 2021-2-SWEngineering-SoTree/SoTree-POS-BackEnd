package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.User;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>{
    public Optional<Employee> findEmployeeByUser(User user);
}
