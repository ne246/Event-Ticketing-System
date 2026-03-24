package edu.fullerton.ticketing.dto;

import edu.fullerton.ticketing.entity.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CreateEventRequest(
        String title,
        String description,
        LocalDateTime eventDate,
        EventStatus status,
        Long organizerId,
        Long venueId,
        List<CreateTicketTypeRequest> ticketTypes
) {
}
