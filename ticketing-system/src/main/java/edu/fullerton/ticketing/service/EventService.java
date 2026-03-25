package edu.fullerton.ticketing.service;

import edu.fullerton.ticketing.dto.*;
import edu.fullerton.ticketing.entity.*;
import edu.fullerton.ticketing.exception.NotFoundException;
import edu.fullerton.ticketing.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final VenueRepository venueRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final BookingRepository bookingRepository;

    public EventService(
            EventRepository eventRepository,
            OrganizerRepository organizerRepository,
            VenueRepository venueRepository,
            TicketTypeRepository ticketTypeRepository,
            BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.venueRepository = venueRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public EventResponseDto createEvent(CreateEventRequest request) {
        validateText(request.title(), "Event title is required.");
        if (request.eventDate() == null) {
            throw new IllegalArgumentException("Event date is required.");
        }
        if (request.status() == null) {
            throw new IllegalArgumentException("Event status is required.");
        }
        if (request.organizerId() == null) {
            throw new IllegalArgumentException("Organizer id is required.");
        }
        if (request.venueId() == null) {
            throw new IllegalArgumentException("Venue id is required.");
        }
        if (request.ticketTypes() == null || request.ticketTypes().isEmpty()) {
            throw new IllegalArgumentException("At least one ticket type is required.");
        }

        Organizer organizer = organizerRepository.findById(request.organizerId())
                .orElseThrow(() -> new NotFoundException("Organizer not found."));
        Venue venue = venueRepository.findById(request.venueId())
                .orElseThrow(() -> new NotFoundException("Venue not found."));

        request.ticketTypes().forEach(this::validateTicketType);

        Event event = new Event();
        event.setTitle(request.title().trim());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setStatus(request.status());
        event.setOrganizer(organizer);
        event.setVenue(venue);

        Event savedEvent = eventRepository.save(event);

        List<TicketType> savedTicketTypes = ticketTypeRepository.saveAll(
                request.ticketTypes().stream()
                        .map(ticketRequest -> toTicketType(ticketRequest, savedEvent))
                        .toList());

        return toEventResponse(savedEvent, savedTicketTypes);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDto> getUpcomingEvents() {
        return eventRepository.findByStatusOrderByEventDateAsc(EventStatus.UPCOMING)
                .stream()
                .map(event -> toEventResponse(event, ticketTypeRepository.findByEventIdOrderByPriceAsc(event.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponseDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found."));
        return toEventResponse(event, ticketTypeRepository.findByEventIdOrderByPriceAsc(eventId));
    }

    @Transactional(readOnly = true)
    public RevenueDto getRevenue(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found."));
        BigDecimal totalRevenue = bookingRepository.calculateRevenueForEvent(eventId, PaymentStatus.CONFIRMED);
        return new RevenueDto(event.getId(), event.getTitle(), totalRevenue);
    }

    private TicketType toTicketType(CreateTicketTypeRequest request, Event event) {
        TicketType ticketType = new TicketType();
        ticketType.setName(request.name().trim());
        ticketType.setPrice(request.price());
        ticketType.setQuantityAvailable(request.quantityAvailable());
        ticketType.setEvent(event);
        return ticketType;
    }

    private EventResponseDto toEventResponse(Event event, List<TicketType> ticketTypes) {
        return new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getStatus(),
                event.getOrganizer().getName(),
                event.getVenue().getName(),
                ticketTypes.stream()
                        .map(ticketType -> new TicketTypeDto(
                                ticketType.getId(),
                                ticketType.getName(),
                                ticketType.getPrice(),
                                ticketType.getQuantityAvailable()))
                        .toList());
    }

    private void validateTicketType(CreateTicketTypeRequest request) {
        validateText(request.name(), "Ticket type name is required.");
        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Ticket type price must be greater than or equal to zero.");
        }
        if (request.quantityAvailable() == null || request.quantityAvailable() < 0) {
            throw new IllegalArgumentException("Ticket type quantity must be greater than or equal to zero.");
        }
    }

    private void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
