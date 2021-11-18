package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.restaurant.VO.EventVO;
import sogong.restaurant.domain.Event;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.service.EventService;
import sogong.restaurant.service.ManagerService;

import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;
    @Autowired
    ManagerService managerService;

    @PostMapping("/add")
    public String addEvent(@RequestBody EventVO eventVO) {
        Event event = new Event();

        Manager manager = managerService.getOneManager(eventVO.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        if (eventVO.getEventDiscountRate() == null && eventVO.getEventDiscountValue() == null) {
            System.out.println("blank!");
            return "null input";
        }
        if (eventVO.getEventDiscountRate() != null) {
            event.setEventDiscountRate(eventVO.getEventDiscountRate());
        }
        if (eventVO.getEventDiscountValue() != null) {
            event.setEventDiscountValue(eventVO.getEventDiscountValue());
        }
        event.setManager(manager);

        eventService.saveEvent(event);
        return Double.toString(event.getId());
    }
}
