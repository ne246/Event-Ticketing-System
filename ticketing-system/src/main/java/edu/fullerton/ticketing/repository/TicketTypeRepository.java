package edu.fullerton.ticketing.repository;

import edu.fullerton.ticketing.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByEventIdOrderByPriceAsc(Long eventId);
}
