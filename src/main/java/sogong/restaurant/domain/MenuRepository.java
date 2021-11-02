package sogong.restaurant.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    public Menu findMenuByMenuName(String MenuName);
    public List<Menu> findAll();
}
