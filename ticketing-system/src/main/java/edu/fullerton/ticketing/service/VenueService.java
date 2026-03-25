package edu.fullerton.ticketing.service;

import edu.fullerton.ticketing.dto.CreateVenueRequest;
import edu.fullerton.ticketing.dto.VenueResponseDto;
import edu.fullerton.ticketing.entity.Venue;
import edu.fullerton.ticketing.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Transactional
    public VenueResponseDto createVenue(CreateVenueRequest request) {
        validateText(request.name(), "Venue name is required.");
        validateText(request.address(), "Venue address is required.");
        validateText(request.city(), "Venue city is required.");

        if (request.totalCapacity() == null || request.totalCapacity() <= 0) {
            throw new IllegalArgumentException("Venue total capacity must be greater than zero.");
        }

        Venue venue = new Venue();
        venue.setName(request.name().trim());
        venue.setAddress(request.address().trim());
        venue.setCity(request.city().trim());
        venue.setTotalCapacity(request.totalCapacity());

        Venue savedVenue = venueRepository.save(venue);
        return new VenueResponseDto(
                savedVenue.getId(),
                savedVenue.getName(),
                savedVenue.getAddress(),
                savedVenue.getCity(),
                savedVenue.getTotalCapacity());
    }

    private void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
