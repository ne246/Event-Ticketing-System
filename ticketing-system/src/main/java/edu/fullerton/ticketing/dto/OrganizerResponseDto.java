package edu.fullerton.ticketing.dto;

public record OrganizerResponseDto(
        Long id,
        String name,
        String email,
        String phone
) {
}
