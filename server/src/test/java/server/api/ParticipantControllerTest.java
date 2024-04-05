package server.api;
import commons.Participant;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.ParticipantService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParticipantControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveParticipant() {
        Participant participant = new Participant();
        when(participantService.saveParticipant(any(Participant.class))).thenReturn(participant);

        ResponseEntity<?> response = participantController.saveParticipant(participant);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(participantService).saveParticipant(participant);
    }

    @Test
    void testSaveParticipantIllegalArgumentException() {
        Participant participant = new Participant();
        when(participantService.saveParticipant(participant)).thenThrow(new IllegalArgumentException("Invalid argument"));

        ResponseEntity<?> response = participantController.saveParticipant(participant);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }

    @Test
    void testSaveParticipantException() {
        Participant participant = new Participant();
        when(participantService.saveParticipant(participant)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = participantController.saveParticipant(participant);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to save the participant: Internal server error", response.getBody());
    }

    @Test
    void testFindParticipantById() {
        Optional<Participant> optionalParticipant = Optional.of(new Participant());
        when(participantService.findParticipantById(anyLong())).thenReturn(optionalParticipant);

        ResponseEntity<?> response = participantController.findParticipantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(participantService).findParticipantById(1L);
    }

    @Test
    void testFindParticipantByIdNotFound() {
        when(participantService.findParticipantById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<?> response = participantController.findParticipantById(1L);
        assertEquals(HttpStatus.NOT_FOUND,  response.getStatusCode());
        assertEquals("Participant not found with ID: 1", response.getBody());
    }

    @Test
    void testFindParticipantByIdThrowsIllegalArgumentException(){
        when(participantService.findParticipantById(anyLong())).thenThrow(new IllegalArgumentException("Invalid id"));
        ResponseEntity<?> response = participantController.findParticipantById(-1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid id", response.getBody());
    }

    @Test
    void testFindParticipantByIdThrowsServiceException() {
        when(participantService.findParticipantById(anyLong())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to find Participant by ID: Internal error", response.getBody());
    }

    @Test
    void testFindAllParticipants() {
        List<Participant> participants = Arrays.asList(new Participant(), new Participant());
        when(participantService.findAllParticipants()).thenReturn(participants);

        ResponseEntity<?> response = participantController.findAllParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(participantService).findAllParticipants();
    }

    @Test
    void testFindAllParticipantsException() {
        when(participantService.findAllParticipants()).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = participantController.findAllParticipants();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve all participants: Internal server error", response.getBody());
    }

    @Test
    void testDeleteParticipantById() {
        doNothing().when(participantService).deleteParticipantById(anyLong());

        ResponseEntity<?> response = participantController.deleteParticipantById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(participantService).deleteParticipantById(1L);
    }

    @Test
    void testDeleteParticipantByIdThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Invalid ID")).when(participantService).deleteParticipantById(anyLong());

        ResponseEntity<?> response = participantController.deleteParticipantById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody());
    }
    @Test
    void testDeleteParticipantByIdThrowsServiceException() {
        doThrow(new ServiceException("Database error")).when(participantService).deleteParticipantById(anyLong());

        ResponseEntity<?> response = participantController.deleteParticipantById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete the participant: Database error", response.getBody());
    }

    @Test
    void testFindParticipantByUsername() {
        Participant expectedParticipant = new Participant();
        when(participantService.findParticipantByUsername(anyString())).thenReturn(expectedParticipant);

        ResponseEntity<?> response = participantController.findParticipantByUsername("username");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipant, response.getBody());
    }

    @Test
    void testFindParticipantByUsernameThrowsIllegalArgumentException() {
        when(participantService.findParticipantByUsername(anyString())).thenThrow(new IllegalArgumentException("Invalid username"));

        ResponseEntity<?> response = participantController.findParticipantByUsername("username");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find the participant by username: Invalid username", response.getBody());
    }

    @Test
    void testFindParticipantByUsernameThrowsServiceException() {
        when(participantService.findParticipantByUsername(anyString())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantByUsername("username");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find the participant by username: Internal error", response.getBody());
    }

    @Test
    void testFindParticipantByUsername_NotFound() {
        when(participantService.findParticipantByUsername("username")).thenReturn(null);

        ResponseEntity<?> response = participantController.findParticipantByUsername("username");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testFindParticipantByEmail() {
        Participant expectedParticipant = new Participant();
        when(participantService.findParticipantByEmail(anyString())).thenReturn(expectedParticipant);

        ResponseEntity<?> response = participantController.findParticipantByEmail("email@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipant, response.getBody());
    }

    @Test
    void testFindParticipantByEmailThrowsIllegalArgumentException()  {
        when(participantService.findParticipantByEmail(anyString())).thenThrow(new IllegalArgumentException("Invalid email"));

        ResponseEntity<?> response = participantController.findParticipantByEmail("email@example.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find the participant by email: Invalid email", response.getBody());
    }

    @Test
    void testFindParticipantByEmailThrowsServiceException() {
        when(participantService.findParticipantByEmail(anyString())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantByEmail("email@example.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find the participant by email: Internal error", response.getBody());
    }

    @Test
    void testFindParticipantsByFirstName() {
        List<Participant> expectedParticipants = List.of(new Participant());
        when(participantService.findParticipantsByFirstName(anyString())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsByFirstName("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantByFirstNameThrowsIllegalArgumentException()  {
        when(participantService.findParticipantsByFirstName(anyString())).thenThrow(new IllegalArgumentException("Invalid first name"));

        ResponseEntity<?> response = participantController.findParticipantsByFirstName("John");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants by first name: Invalid first name", response.getBody());
    }

    @Test
    void testFindParticipantByFirstNameThrowsServiceException() {
        when(participantService.findParticipantsByFirstName(anyString())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantsByFirstName("John");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants by first name: Internal error", response.getBody());
    }

    @Test
    void testFindParticipantsByLastName() {
        List<Participant> expectedParticipants = List.of(new Participant());
        when(participantService.findParticipantsByLastName(anyString())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsByLastName("Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantByLastNameThrowsIllegalArgumentException()  {
        when(participantService.findParticipantsByLastName(anyString())).thenThrow(new IllegalArgumentException("Invalid last name"));

        ResponseEntity<?> response = participantController.findParticipantsByLastName("Doe");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants by last name: Invalid last name", response.getBody());
    }

    @Test
    void testFindParticipantByLastNameThrowsServiceException() {
        when(participantService.findParticipantsByLastName(anyString())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantsByLastName("Doe");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants by last name: Internal error", response.getBody());
    }

    @Test
    void testFindParticipantsByLanguageChoice() {
        List<Participant> expectedParticipants = List.of(new Participant());
        when(participantService.findParticipantsByLanguageChoice(anyString())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsByLanguageChoice("English");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantByLanguageChoiceThrowsIllegalArgumentException()  {
        when(participantService.findParticipantsByLanguageChoice(anyString())).thenThrow(new IllegalArgumentException("Invalid language"));

        ResponseEntity<?> response = participantController.findParticipantsByLanguageChoice("ABC");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants by language choice: Invalid language", response.getBody());
    }

    @Test
    void testFindParticipantByLanguageChoiceThrowsServiceException() {
        when(participantService.findParticipantsByLanguageChoice(anyString())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantsByLanguageChoice("ABC");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants by language choice: Internal error", response.getBody());
    }

    @Test
    void testFindParticipantsOwingForEvent() {
        List<Participant> expectedParticipants = List.of(new Participant());
        when(participantService.findParticipantsOwingForEvent(anyLong())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsOwingForEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }
    @Test
    void testFindParticipantsOwingForEventThrowsIllegalArgumentException()  {
        when(participantService.findParticipantsOwingForEvent(anyLong())).thenThrow(new IllegalArgumentException("Invalid eventId"));

        ResponseEntity<?> response = participantController.findParticipantsOwingForEvent(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants owing for the event: Invalid eventId", response.getBody());
    }
    @Test
    void testFindParticipantsOwingForEventThrowsServiceException() {
        when(participantService.findParticipantsOwingForEvent(anyLong())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantsOwingForEvent(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants owing for the event: Internal error", response.getBody());
    }

    @Test
    void testFindParticipantsPaidForEvent() {
        List<Participant> expectedParticipants = List.of(new Participant());
        when(participantService.findParticipantsPaidForEvent(anyLong())).thenReturn(expectedParticipants);
        ResponseEntity<?> response = participantController.findParticipantsPaidForEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantsPaidForEventThrowsIllegalArgumentException()  {
        when(participantService.findParticipantsPaidForEvent(anyLong())).thenThrow(new IllegalArgumentException("Invalid eventId"));

        ResponseEntity<?> response = participantController.findParticipantsPaidForEvent(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants who have paid for the event: Invalid eventId", response.getBody());
    }

    @Test
    void testFindParticipantsPaidForEventThrowsServiceException() {
        when(participantService.findParticipantsPaidForEvent(anyLong())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = participantController.findParticipantsPaidForEvent(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants who have paid for the event: Internal error", response.getBody());
    }

    @Test
    void testFindAllParticipantsEmptyList() {
        when(participantService.findAllParticipants()).thenReturn(List.of());

        ResponseEntity<?> response = participantController.findAllParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ((List<?>) response.getBody()).size(), "Expected an empty list of participants");
        verify(participantService).findAllParticipants();
    }

    @Test
    void testFindParticipantsByLanguageChoiceEmptyList() {
        when(participantService.findParticipantsByLanguageChoice("Nonexistent")).thenReturn(List.of());

        ResponseEntity<?> response = participantController.findParticipantsByLanguageChoice("Nonexistent");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ((List<?>) response.getBody()).size(), "Expected an empty list of participants for the language choice");
    }

    @Test
    void testFindParticipantsByLastNameNotFound() {
        when(participantService.findParticipantsByLastName("Unknown")).thenReturn(List.of());

        ResponseEntity<?> response = participantController.findParticipantsByLastName("Unknown");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, ((List<?>) response.getBody()).size(), "Expected no participants found for the last name");
    }
    @Test
    void testFindParticipantByEmailNotFound() {
        when(participantService.findParticipantByEmail("unknown@example.com")).thenReturn(null);

        ResponseEntity<?> response = participantController.findParticipantByEmail("unknown@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Expected 404 status for not found email");
    }

    @Test
    void testFindParticipantsOwingForEventInternalError() {
        when(participantService.findParticipantsOwingForEvent(anyLong())).thenThrow(new ServiceException("Database error"));
        ResponseEntity<?> response = participantController.findParticipantsOwingForEvent(2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to find participants owing for the event: Database error", response.getBody());
    }

    @Test
    void testFindParticipantsPaidForEventNoContent() {
        when(participantService.findParticipantsPaidForEvent(anyLong())).thenReturn(List.of());
        ResponseEntity<?> response = participantController.findParticipantsPaidForEvent(3L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Expected 204 status for no participants found who have paid for the event");
    }

    @Test
    void testFindParticipantsOwingForEventEmpty() {
        when(participantService.findParticipantsOwingForEvent(1L)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = participantController.findParticipantsOwingForEvent(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
