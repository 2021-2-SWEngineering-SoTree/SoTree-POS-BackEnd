package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class testController {

    public testController() {
    }

    @PostMapping("/test")
    public String testAPI(){
        log.info("IN THE TEST API");
        return "hello sogong";
    }

}
