package edu.fullerton.ticketing.repository;

import edu.fullerton.ticketing.entity.Event;
import edu.fullerton.ticketing.entity.EventStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"organizer", "venue"})
    List<Event> findByStatusOrderByEventDateAsc(EventStatus status);

    @EntityGraph(attributePaths = {"organizer", "venue"})
    Optional<Event> findById(Long id);
}
