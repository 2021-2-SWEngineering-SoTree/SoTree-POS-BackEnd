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


        if ((eventVO.getEventDiscountRate() == null && eventVO.getEventDiscountValue() == null)
                || (eventVO.getEventDiscountRate() != null && eventVO.getEventDiscountValue() != null)) {
            return "Wrong input";
        }

        // 할인율
        if (eventVO.getEventDiscountRate() != null) {
            Double eventDiscountRate = eventVO.getEventDiscountRate();
            // 범위 체크
            if (eventDiscountRate <= 0.0 || eventDiscountRate > 1.0) {
                return "Wrong with eventDiscountRate";
            }
        }

        // 할인 가격
        if (eventVO.getEventDiscountValue() != null) {
            Integer eventDiscountValue = eventVO.getEventDiscountValue();
            if (eventDiscountValue < 1) {
                return "Wrong with eventDiscountValue";
            }
        }

        event.setEventDiscountRate(eventVO.getEventDiscountRate());
        event.setEventDiscountValue(eventVO.getEventDiscountValue());
        event.setEventName(eventVO.getEventName());
        event.setManager(manager);

        eventService.saveEvent(event);
        return Long.toString(event.getId());
    }
}
