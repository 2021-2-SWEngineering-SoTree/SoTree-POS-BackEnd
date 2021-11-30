package sogong.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class errorController {

    public errorController(){

    }

    @PostMapping("/noAuth")
    public String notAuth(){
        return "인증되지 않았습니다";
    }


}
