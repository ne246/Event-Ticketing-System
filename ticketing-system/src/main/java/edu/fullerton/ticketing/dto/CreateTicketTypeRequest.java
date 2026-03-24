package edu.fullerton.ticketing.dto;

import java.math.BigDecimal;

public record CreateTicketTypeRequest(
        String name,
        BigDecimal price,
        Integer quantityAvailable
) {
}
