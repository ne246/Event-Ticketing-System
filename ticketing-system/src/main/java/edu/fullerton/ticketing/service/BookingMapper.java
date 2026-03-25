package edu.fullerton.ticketing.service;

import edu.fullerton.ticketing.dto.BookingResponseDto;
import edu.fullerton.ticketing.entity.Booking;

final class BookingMapper {

    private BookingMapper() {
    }

    static BookingResponseDto toDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getBookingReference(),
                booking.getBookingDate(),
                booking.getPaymentStatus(),
                booking.getAttendee().getName(),
                booking.getTicketType().getEvent().getTitle(),
                booking.getTicketType().getName(),
                booking.getTicketType().getPrice());
    }
}
