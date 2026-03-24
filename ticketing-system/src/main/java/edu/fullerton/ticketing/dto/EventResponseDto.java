package edu.fullerton.ticketing.dto;

import edu.fullerton.ticketing.entity.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponseDto(
        Long id,
        String title,
        String description,
        LocalDateTime eventDate,
        EventStatus status,
        String organizerName,
        String venueName,
        List<TicketTypeDto> ticketTypes
) {
}
