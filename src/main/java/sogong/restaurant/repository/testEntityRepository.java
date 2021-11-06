package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.testEntity;

@Repository
public interface testEntityRepository extends JpaRepository<testEntity,Long> {

}
