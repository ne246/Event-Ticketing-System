package edu.fullerton.ticketing.service;

import edu.fullerton.ticketing.dto.CreateOrganizerRequest;
import edu.fullerton.ticketing.dto.OrganizerResponseDto;
import edu.fullerton.ticketing.entity.Organizer;
import edu.fullerton.ticketing.repository.OrganizerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizerService {

    private final OrganizerRepository organizerRepository;

    public OrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @Transactional
    public OrganizerResponseDto createOrganizer(CreateOrganizerRequest request) {
        validateText(request.name(), "Organizer name is required.");
        validateText(request.email(), "Organizer email is required.");

        organizerRepository.findByEmail(request.email().trim())
                .ifPresent(organizer -> {
                    throw new IllegalArgumentException("Organizer email must be unique.");
                });

        Organizer organizer = new Organizer();
        organizer.setName(request.name().trim());
        organizer.setEmail(request.email().trim());
        organizer.setPhone(request.phone() == null ? null : request.phone().trim());

        Organizer savedOrganizer = organizerRepository.save(organizer);
        return new OrganizerResponseDto(
                savedOrganizer.getId(),
                savedOrganizer.getName(),
                savedOrganizer.getEmail(),
                savedOrganizer.getPhone());
    }

    private void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
