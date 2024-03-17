package server.api;

import commons.Participant;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.ParticipantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participants") // our base endpoint for participants
public class ParticipantController {

    private final ParticipantService participantService;

    /**
     * Dependency injection through constructor
     * @param participantService ParticipantService
     */
    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    /**
     * Create or update a participant.
     * @param participant Participant
     * @return ResponseEntity<Participant>
     */
    @PostMapping
    public ResponseEntity<?> saveParticipant(@RequestBody Participant participant) {
        try {
            Participant savedParticipant = participantService.saveParticipant(participant);
            return new ResponseEntity<>(savedParticipant, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save the participant: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve a participant by their ID.
     * @param id Long
     * @return ResponseEntity<Participant>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findParticipantById(@PathVariable Long id) {
        try {
            Optional<Participant> participantOptional = participantService.findParticipantById(id);
            if(participantOptional.isPresent()){
                return ResponseEntity.ok(participantOptional.get());
            } else{
                return new ResponseEntity<>("Participant not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to find Participant by ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve all participants.
     * @return ResponseEntity<List<Participant>>
     */
    @GetMapping
    public ResponseEntity<?> findAllParticipants() {
        try {
            List<Participant> participants = participantService.findAllParticipants();
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve all participants: "  + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a participant by their ID.
     * @param id Long
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParticipantById(@PathVariable Long id) {
        try {
            participantService.deleteParticipantById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to delete the participant: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve a participant by their username.
     * @param username String
     * @return ResponseEntity<Participant>
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> findParticipantByUsername(@PathVariable String username) {
        try {
            Participant participant = participantService.findParticipantByUsername(username);
            if (participant != null) {
                return ResponseEntity.ok(participant);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find the participant by username: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve a participant by their email.
     * @param email String
     * @return ResponseEntity<Participant>
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> findParticipantByEmail(@PathVariable String email) {
        try {
            Participant participant = participantService.findParticipantByEmail(email);
            if (participant != null) {
                return ResponseEntity.ok(participant);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find the participant by email: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve participants by their first name.
     * @param firstName String
     * @return ResponseEntity<List<Participant>>
     */
    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<?> findParticipantsByFirstName(@PathVariable String firstName) {
        try {
            List<Participant> participants = participantService
                .findParticipantsByFirstName(firstName);
            return ResponseEntity.ok(participants);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find participants by first name: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve participants by their last name.
     * @param lastName String
     * @return ResponseEntity<List<Participant>>
     */
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<?> findParticipantsByLastName(@PathVariable String lastName) {
        try {
            List<Participant> participants = participantService
                    .findParticipantsByLastName(lastName);
            return ResponseEntity.ok(participants);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find participants by last name: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve participants by their language preference.
     * @param languageChoice String
     * @return ResponseEntity<List<Participant>>
     */
    @GetMapping("/language/{languageChoice}")
    public ResponseEntity<?> findParticipantsByLanguageChoice(@PathVariable String languageChoice) {
        try {
            List<Participant> participants = participantService
                    .findParticipantsByLanguageChoice(languageChoice);
            return ResponseEntity.ok(participants);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find participants by language choice: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve participants who owe money for a specific event.
     * @param eventId Long
     * @return ResponseEntity<List<Participant>>
     */
    @GetMapping("/event/owing/{eventId}")
    public ResponseEntity<?> findParticipantsOwingForEvent(@PathVariable Long eventId) {
        try {
            List<Participant> participants = participantService
                    .findParticipantsOwingForEvent(eventId);
            if (!participants.isEmpty()) {
                return ResponseEntity.ok(participants);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find participants owing for the event: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve participants who have paid for a specific event.
     * @param eventId Long
     * @return ResponseEntity<List<Participant>>
     */
    @GetMapping("/event/paid/{eventId}")
    public ResponseEntity<?> findParticipantsPaidForEvent(@PathVariable Long eventId) {
        try {
            List<Participant> participants = participantService
                    .findParticipantsPaidForEvent(eventId);
            if (!participants.isEmpty()) {
                return ResponseEntity.ok(participants);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find participants who have paid for the event: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
