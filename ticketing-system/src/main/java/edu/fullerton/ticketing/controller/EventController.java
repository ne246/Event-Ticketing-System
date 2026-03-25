package edu.fullerton.ticketing.controller;

import edu.fullerton.ticketing.dto.CreateEventRequest;
import edu.fullerton.ticketing.dto.EventResponseDto;
import edu.fullerton.ticketing.dto.RevenueDto;
import edu.fullerton.ticketing.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@RequestBody CreateEventRequest request) {
        return eventService.createEvent(request);
    }

    @GetMapping
    public List<EventResponseDto> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @GetMapping("/{id}")
    public EventResponseDto getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }

    @GetMapping("/{id}/revenue")
    public RevenueDto getRevenue(@PathVariable Long id) {
        return eventService.getRevenue(id);
    }
}
