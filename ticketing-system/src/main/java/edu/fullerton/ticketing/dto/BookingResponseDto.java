package edu.fullerton.ticketing.dto;

import edu.fullerton.ticketing.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponseDto(
        Long id,
        String bookingReference,
        LocalDateTime bookingDate,
        PaymentStatus paymentStatus,
        String attendeeName,
        String eventTitle,
        String ticketTypeName,
        BigDecimal price
) {
}
