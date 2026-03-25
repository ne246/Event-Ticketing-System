package edu.fullerton.ticketing.dto;

public record CreateAttendeeRequest(
        String name,
        String email
) {
}
