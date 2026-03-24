package edu.fullerton.ticketing.dto;

public record CreateOrganizerRequest(
        String name,
        String email,
        String phone
) {
}
