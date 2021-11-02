package sogong.restaurant.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuIngredientRepository extends JpaRepository<MenuIngredient,Long> {

    //public List<MenuIngredient> findMenuIngredientsByMenu_Id(Long menuId);
}
