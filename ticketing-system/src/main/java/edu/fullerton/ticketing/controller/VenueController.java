package edu.fullerton.ticketing.controller;

import edu.fullerton.ticketing.dto.CreateVenueRequest;
import edu.fullerton.ticketing.dto.VenueResponseDto;
import edu.fullerton.ticketing.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenueResponseDto createVenue(@RequestBody CreateVenueRequest request) {
        return venueService.createVenue(request);
    }
}
