package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByLoginId(String loginId);
}
