package sogong.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.domain.User;
import sogong.restaurant.repository.UserRepository;
import sogong.restaurant.service.LoginService;
import sogong.restaurant.util.JwtTokenProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private LoginService loginService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @RequestMapping("/login")
    public String login(@RequestBody HashMap<String, String>map){

        /*
        {
            "id":"아이디",
            "pw":"비밀번호"
        }
        형식
         */
        
        String id = map.get("id");
        String pw = map.get("pw");

        //토큰을 리턴한다??


        User user = userRepository.findByLoginId(id)
                .orElseThrow(() -> new NoSuchElementException());

        if(!passwordEncoder.matches(pw,user.getPassword())) {
            return null;
        }
            else {
            String token = jwtTokenProvider.createToken(user.getLoginId(), user.getRoles());
            //System.out.println("token = " + token);
            return token;

        }
    }

    @RequestMapping("/addUser")
    public Long addUser(@RequestBody Map<String, String> user){


        User user1 = User.builder()
                .loginId(user.get("loginId"))
                .birthDay(user.get("birthDay")).userName(user.get("userName"))
                .email(user.get("email")).phoneNumber(user.get("phoneNumber"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")).build();

        System.out.println("user1.getUsername() = " + user1.getUsername());


        return userRepository.save(user1).getId();
    }

    @RequestMapping("/userIdPresent")
    public boolean validName(@RequestBody String loginId){
        if(userRepository.findByLoginId(loginId).isPresent()) return false;
        else return true;

    }

}
