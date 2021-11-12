package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.MenuIngredient;
import sogong.restaurant.domain.User;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {
    public Optional<Manager> findByStoreName(String storeName);
    public Optional<Manager> findByUser(User user);
}
