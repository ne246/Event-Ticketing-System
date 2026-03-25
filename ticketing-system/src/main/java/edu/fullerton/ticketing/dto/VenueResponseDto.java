package edu.fullerton.ticketing.dto;

public record VenueResponseDto(
        Long id,
        String name,
        String address,
        String city,
        Integer totalCapacity
) {
}
