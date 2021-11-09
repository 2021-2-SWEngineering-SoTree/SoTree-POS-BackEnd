package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuIngredient;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuIngredientRepository extends JpaRepository<MenuIngredient,Long> {

     List<MenuIngredient> findAll();
     List<MenuIngredient> findMenuIngredientsByMenu(Menu menu);
}
