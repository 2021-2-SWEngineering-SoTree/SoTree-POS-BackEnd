package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByLoginId(String loginId);
    public List<User> findAll();
    public Optional<User> findByUserNameAndBirthDayAndPhoneNumberAndEmail(String userName, String birthDay,String phoneNumber, String email);
    public Optional<User> findByUserNameAndLoginIdAndPhoneNumberAndEmail(String userName,String loginId, String phoneNumber, String email);
}
