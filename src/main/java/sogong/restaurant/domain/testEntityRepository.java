package sogong.restaurant.domain;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface testEntityRepository extends JpaRepository<testEntity,Long> {

}
