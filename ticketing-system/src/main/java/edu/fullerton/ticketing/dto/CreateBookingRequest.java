package edu.fullerton.ticketing.dto;

public record CreateBookingRequest(
        Long attendeeId,
        Long ticketTypeId
) {
}
