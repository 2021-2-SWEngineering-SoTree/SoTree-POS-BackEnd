package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.User;
import sogong.restaurant.domain.UserRepository;

@Service
public class LoginService {
    private UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // null이면 실패
    public User login(String id, String pw){
        User user = userRepository.findByLoginId(id);
        if(user==null) return null;

        if(!user.getPassword().equals(pw)) return null;
        return user;
    }

    public User addUser(User user){
        userRepository.save(user);

        return user;
    }


}
