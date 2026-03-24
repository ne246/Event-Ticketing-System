package edu.fullerton.ticketing.controller;

import edu.fullerton.ticketing.dto.BookingResponseDto;
import edu.fullerton.ticketing.dto.CreateBookingRequest;
import edu.fullerton.ticketing.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestBody CreateBookingRequest request) {
        return bookingService.createBooking(request);
    }

    @PutMapping("/{id}/cancel")
    public BookingResponseDto cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
}
