package edu.fullerton.ticketing.controller;

import edu.fullerton.ticketing.dto.AttendeeBookingsDto;
import edu.fullerton.ticketing.dto.AttendeeResponseDto;
import edu.fullerton.ticketing.dto.CreateAttendeeRequest;
import edu.fullerton.ticketing.service.AttendeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendees")
public class AttendeeController {

    private final AttendeeService attendeeService;

    public AttendeeController(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttendeeResponseDto createAttendee(@RequestBody CreateAttendeeRequest request) {
        return attendeeService.createAttendee(request);
    }

    @GetMapping("/{id}/bookings")
    public AttendeeBookingsDto getAttendeeBookings(@PathVariable Long id) {
        return attendeeService.getBookingsForAttendee(id);
    }
}
