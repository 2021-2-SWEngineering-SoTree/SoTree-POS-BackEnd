package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Event;
import sogong.restaurant.domain.Manager;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByManager(Manager manager);

    Optional<Event> findEventByManagerAndId(Manager manager, Long id);
}
