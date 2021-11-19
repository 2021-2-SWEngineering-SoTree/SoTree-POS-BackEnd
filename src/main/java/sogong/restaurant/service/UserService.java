package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.User;
import sogong.restaurant.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Long saveUser(User user){
        userRepository.save(user);
        return user.getId();
    }

}
