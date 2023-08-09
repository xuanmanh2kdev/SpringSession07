package fa.training.eventbox.repository;

import fa.training.eventbox.model.entity.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<Event, Long> { // EventDAO

    boolean existsByNameAndDeletedFalse(String name);

    @Query(value = "SELECT e FROM Event e " +
            " WHERE (e.isPublic = true AND e.capacity >= :capacity) OR " +
            " (e.isPublic = false AND e.capacity < :capacity)")
    List<Event> findAllEvents(@Param("capacity") Integer capacity);
}
