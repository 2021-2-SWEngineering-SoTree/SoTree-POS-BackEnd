package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.domain.User;
import sogong.restaurant.service.LoginService;

import java.util.HashMap;

@RestController
@Slf4j
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

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
        String ret = "";
        User user = loginService.login(id, pw);
        //System.out.println("user : "+user.getUserName());

        if(user==null) return "아이디 또는 비밀번호가 일치하지 않습니다.";

        return "OK";
    }

    @RequestMapping("/addUser")
    public User addUser(@RequestBody User user){
        return loginService.addUser(user);
    }


}
