package edu.fullerton.ticketing.repository;

import edu.fullerton.ticketing.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
