package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Menu;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    public Optional<Menu> findMenuByMenuName(String MenuName);
    public List<Menu> findAllByManager(Manager manager);
    public List<Menu> findAllById(Long id);
}
