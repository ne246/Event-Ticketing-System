package edu.fullerton.ticketing.service;

import edu.fullerton.ticketing.dto.AttendeeBookingsDto;
import edu.fullerton.ticketing.dto.AttendeeResponseDto;
import edu.fullerton.ticketing.dto.BookingResponseDto;
import edu.fullerton.ticketing.dto.CreateAttendeeRequest;
import edu.fullerton.ticketing.entity.Attendee;
import edu.fullerton.ticketing.exception.NotFoundException;
import edu.fullerton.ticketing.repository.AttendeeRepository;
import edu.fullerton.ticketing.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final BookingRepository bookingRepository;

    public AttendeeService(AttendeeRepository attendeeRepository, BookingRepository bookingRepository) {
        this.attendeeRepository = attendeeRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public AttendeeResponseDto createAttendee(CreateAttendeeRequest request) {
        validateText(request.name(), "Attendee name is required.");
        validateText(request.email(), "Attendee email is required.");

        attendeeRepository.findByEmail(request.email().trim())
                .ifPresent(attendee -> {
                    throw new IllegalArgumentException("Attendee email must be unique.");
                });

        Attendee attendee = new Attendee();
        attendee.setName(request.name().trim());
        attendee.setEmail(request.email().trim());

        Attendee savedAttendee = attendeeRepository.save(attendee);
        return new AttendeeResponseDto(savedAttendee.getId(), savedAttendee.getName(), savedAttendee.getEmail());
    }

    @Transactional(readOnly = true)
    public AttendeeBookingsDto getBookingsForAttendee(Long attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new NotFoundException("Attendee not found."));

        List<BookingResponseDto> bookings = bookingRepository.findByAttendeeIdOrderByBookingDateDesc(attendeeId)
                .stream()
                .map(BookingMapper::toDto)
                .toList();

        return new AttendeeBookingsDto(attendee.getId(), attendee.getName(), bookings);
    }

    private void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
