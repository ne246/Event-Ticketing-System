package edu.fullerton.ticketing.dto;

import java.math.BigDecimal;

public record TicketTypeDto(
        Long id,
        String name,
        BigDecimal price,
        Integer quantityAvailable
) {
}
