package edu.fullerton.ticketing.dto;

import java.math.BigDecimal;

public record RevenueDto(
        Long eventId,
        String eventTitle,
        BigDecimal totalConfirmedRevenue
) {
}
