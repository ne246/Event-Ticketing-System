package edu.fullerton.ticketing.service;

import edu.fullerton.ticketing.dto.BookingResponseDto;
import edu.fullerton.ticketing.dto.CreateBookingRequest;
import edu.fullerton.ticketing.entity.Attendee;
import edu.fullerton.ticketing.entity.Booking;
import edu.fullerton.ticketing.entity.PaymentStatus;
import edu.fullerton.ticketing.entity.TicketType;
import edu.fullerton.ticketing.exception.NotFoundException;
import edu.fullerton.ticketing.repository.AttendeeRepository;
import edu.fullerton.ticketing.repository.BookingRepository;
import edu.fullerton.ticketing.repository.TicketTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AttendeeRepository attendeeRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public BookingService(
            BookingRepository bookingRepository,
            AttendeeRepository attendeeRepository,
            TicketTypeRepository ticketTypeRepository) {
        this.bookingRepository = bookingRepository;
        this.attendeeRepository = attendeeRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Transactional
    public BookingResponseDto createBooking(CreateBookingRequest request) {
        if (request.attendeeId() == null) {
            throw new IllegalArgumentException("Attendee id is required.");
        }
        if (request.ticketTypeId() == null) {
            throw new IllegalArgumentException("Ticket type id is required.");
        }

        Attendee attendee = attendeeRepository.findById(request.attendeeId())
                .orElseThrow(() -> new NotFoundException("Attendee not found."));
        TicketType ticketType = ticketTypeRepository.findById(request.ticketTypeId())
                .orElseThrow(() -> new NotFoundException("Ticket type not found."));

        if (ticketType.getQuantityAvailable() <= 0) {
            throw new IllegalArgumentException("Sorry, this ticket type is sold out.");
        }
        if (bookingRepository.existsByAttendeeIdAndTicketTypeId(attendee.getId(), ticketType.getId())) {
            throw new IllegalArgumentException("You have already booked this ticket type.");
        }

        ticketType.setQuantityAvailable(ticketType.getQuantityAvailable() - 1);

        Booking booking = new Booking();
        booking.setAttendee(attendee);
        booking.setTicketType(ticketType);
        booking.setBookingDate(LocalDateTime.now());
        booking.setPaymentStatus(PaymentStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        savedBooking.setBookingReference(generateBookingReference(savedBooking.getId(), savedBooking.getBookingDate().getYear()));

        return BookingMapper.toDto(savedBooking);
    }

    @Transactional
    public BookingResponseDto cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found."));

        if (booking.getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled.");
        }

        booking.setPaymentStatus(PaymentStatus.CANCELLED);
        TicketType ticketType = booking.getTicketType();
        ticketType.setQuantityAvailable(ticketType.getQuantityAvailable() + 1);

        return BookingMapper.toDto(booking);
    }

    private String generateBookingReference(Long bookingId, int year) {
        return "TKT-" + year + "-" + String.format("%05d", bookingId);
    }
}
