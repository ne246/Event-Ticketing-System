package edu.fullerton.ticketing.controller;

import edu.fullerton.ticketing.dto.CreateOrganizerRequest;
import edu.fullerton.ticketing.dto.OrganizerResponseDto;
import edu.fullerton.ticketing.service.OrganizerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizers")
public class OrganizerController {

    private final OrganizerService organizerService;

    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizerResponseDto createOrganizer(@RequestBody CreateOrganizerRequest request) {
        return organizerService.createOrganizer(request);
    }
}
