package sogong.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.Event;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.repository.EventRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class EventService {
    @Autowired
    private final EventRepository eventRepository;

    @Transactional
    public Long saveEvent(Event event) {
        return eventRepository.save(event).getId();
    }

    public List<Event> getAllEvent(Manager manager) {
        return eventRepository.findAllByManager(manager);
    }

    public Optional<Event> getOneEvent(Manager manager, Long Id) {
        return eventRepository.findEventByManagerAndId(manager, Id);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
