package edu.fullerton.ticketing.dto;

public record CreateVenueRequest(
        String name,
        String address,
        String city,
        Integer totalCapacity
) {
}
