package sogong.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.User;
import sogong.restaurant.repository.UserRepository;
import sogong.restaurant.util.JwtTokenProvider;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class LoginService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    //@Autowired
    //public LoginService(UserRepository userRepository){
        //this.userRepository = userRepository;
    //}

    // null이면 실패

    public boolean isPresent(String loginId){
        Optional<User> user = userRepository.findByLoginId(loginId);

        if(user.isEmpty()) return false;
        else return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);



        Optional<User> byLoginId = userRepository.findByLoginId(username);
        if(!byLoginId.isPresent()){
            System.out.println("존재하지않습니다.");
        }

        System.out.println("byLoginId = " + byLoginId.get().getRoles().get(0));

        return this.userRepository.findByLoginId(username).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
