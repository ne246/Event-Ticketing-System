package edu.fullerton.ticketing.repository;

import edu.fullerton.ticketing.entity.Booking;
import edu.fullerton.ticketing.entity.PaymentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByAttendeeIdAndTicketTypeId(Long attendeeId, Long ticketTypeId);

    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event"})
    List<Booking> findByAttendeeIdOrderByBookingDateDesc(Long attendeeId);

    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event"})
    Optional<Booking> findById(Long id);

    @Query("""
        select coalesce(sum(tt.price), 0)
        from Booking b
        join b.ticketType tt
        join tt.event e
        where e.id = :eventId and b.paymentStatus = :paymentStatus
        """)
    BigDecimal calculateRevenueForEvent(
            @Param("eventId") Long eventId,
            @Param("paymentStatus") PaymentStatus paymentStatus);
}
