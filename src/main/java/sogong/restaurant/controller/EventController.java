package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.VO.EventVO;
import sogong.restaurant.domain.Event;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.service.EventService;
import sogong.restaurant.service.ManagerService;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 이벤트 추가
     */
    @PostMapping("/addEvent")
    public String addEvent(@RequestBody EventVO eventVO) {
        Event event = new Event();

        Manager manager = managerService.getOneManager(eventVO.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));


        if ((eventVO.getEventDiscountRate() != null && eventVO.getEventDiscountValue() != null)) {
            return "Wrong input";
        }

        // 할인율, 할인가격 입력된 값 예외처리
        //checkEventValues(Double.parseDouble(eventVO.getEventDiscountRate()), eventVO.getEventDiscountValue());
        checkEventValues(eventVO.getEventDiscountRate(), eventVO.getEventDiscountValue());

        //event.setEventDiscountRate(Double.parseDouble(eventVO.getEventDiscountRate()));
        event.setEventDiscountRate(eventVO.getEventDiscountRate());
        event.setEventDiscountValue(eventVO.getEventDiscountValue());
        event.setEventName(eventVO.getEventName());
        event.setManager(manager);
        event.setCriticalPoint(eventVO.getCriticalPoint());

        return Long.toString(eventService.saveEvent(event));
    }

    /**
     * 이벤트 목록 출력
     */
    @PostMapping("/getAllEvent/{branchId}")
    public List<EventVO> getAllEvents(@PathVariable(value = "branchId") Long branchId) {
        // return할 리스트
        List<EventVO> eventVOList = new ArrayList<>();

        // manager
        Manager manager = managerService.getOneManager(branchId)
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        List<Event> eventList = eventService.getAllEvent(manager);

        for (Event event : eventList) {
            EventVO eventVO = new EventVO();
            //eventVO.setEventDiscountRate(Double.toString(event.getEventDiscountRate()));
            eventVO.setEventDiscountRate(event.getEventDiscountRate());
            eventVO.setEventDiscountValue(event.getEventDiscountValue());
            eventVO.setEventName(event.getEventName());
            eventVO.setManagerId(manager.getId());
            eventVO.setId(event.getId());
            eventVO.setCriticalPoint(event.getCriticalPoint());
            eventVOList.add(eventVO);
        }

        return eventVOList;
    }

    /**
     * 이벤트 수정
     */
    @PutMapping("/updateEvent")
    public String updateEvent(@RequestBody EventVO eventVO) {
        Manager manager = managerService.getOneManager(eventVO.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다."));

        Event event = eventService.getOneEvent(manager, eventVO.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));


        // 할인율, 할인가격 입력된 값 예외처리
        //checkEventValues(Double.parseDouble(eventVO.getEventDiscountRate()), eventVO.getEventDiscountValue());
        checkEventValues(eventVO.getEventDiscountRate(), eventVO.getEventDiscountValue());

        //event.setEventDiscountRate(Double.parseDouble(eventVO.getEventDiscountRate()));
        event.setEventDiscountRate(eventVO.getEventDiscountRate());
        event.setEventDiscountValue(eventVO.getEventDiscountValue());
        event.setEventName(eventVO.getEventName());
        event.setCriticalPoint(eventVO.getCriticalPoint());
        // event.setManager(manager);

        return Long.toString(eventService.saveEvent(event));
    }

    /**
     * 이벤트 삭제
     */
    @DeleteMapping("/deleteEvent/{branchId}/{eventId}")
    public String deleteEvent(@PathVariable(value = "branchId") Long branchId, @PathVariable(value = "eventId") Long eventId) {
        Manager manager = managerService.getOneManager(branchId)
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다."));

        Event event = eventService.getOneEvent(manager, eventId)
                .orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));

        eventService.deleteEvent(event.getId());

        return "Event Deleted";
    }


    /**
     * 이벤트에 입력된 할인율, 할인 가격에 대한 검증
     */
    private void checkEventValues(Double eventDiscountRate, Integer eventDiscountValue) {
        // 할인율
        if (eventDiscountRate != null) {
            // 범위 체크
            if (eventDiscountRate <= 0.0 || eventDiscountRate > 1.0) {
                throw new IllegalStateException("올바르지 않은 할인율입니다!");
            }
        }

        // 할인 가격
        if (eventDiscountValue != null) {
            if (eventDiscountValue < 1) {
                throw new IllegalStateException("올바르지 않은 할인 가격입니다!");
            }
        }
    }
}
