package edu.fullerton.ticketing.dto;

import java.util.List;

public record AttendeeBookingsDto(
        Long attendeeId,
        String attendeeName,
        List<BookingResponseDto> bookings
) {
}
