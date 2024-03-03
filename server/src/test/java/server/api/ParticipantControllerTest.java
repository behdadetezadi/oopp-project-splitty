package server.api;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.ParticipantService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testFindParticipantById() {
        Optional<Participant> optionalParticipant = Optional.of(new Participant());
        when(participantService.findParticipantById(anyLong())).thenReturn(optionalParticipant);

        ResponseEntity<?> response = participantController.findParticipantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(participantService).findParticipantById(1L);
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
    void testDeleteParticipantById() {
        doNothing().when(participantService).deleteParticipantById(anyLong());

        ResponseEntity<?> response = participantController.deleteParticipantById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(participantService).deleteParticipantById(1L);
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
    void testFindParticipantByEmail() {
        Participant expectedParticipant = new Participant();
        when(participantService.findParticipantByEmail(anyString())).thenReturn(expectedParticipant);

        ResponseEntity<?> response = participantController.findParticipantByEmail("email@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipant, response.getBody());
    }

    @Test
    void testFindParticipantsByFirstName() {
        List<Participant> expectedParticipants = Arrays.asList(new Participant());
        when(participantService.findParticipantsByFirstName(anyString())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsByFirstName("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantsByLastName() {
        List<Participant> expectedParticipants = Arrays.asList(new Participant());
        when(participantService.findParticipantsByLastName(anyString())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsByLastName("Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantsByLanguageChoice() {
        List<Participant> expectedParticipants = Arrays.asList(new Participant());
        when(participantService.findParticipantsByLanguageChoice(anyString())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsByLanguageChoice("English");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantsOwingForEvent() {
        List<Participant> expectedParticipants = Arrays.asList(new Participant());
        when(participantService.findParticipantsOwingForEvent(anyLong())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsOwingForEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }

    @Test
    void testFindParticipantsPaidForEvent() {
        List<Participant> expectedParticipants = Arrays.asList(new Participant());
        when(participantService.findParticipantsPaidForEvent(anyLong())).thenReturn(expectedParticipants);

        ResponseEntity<?> response = participantController.findParticipantsPaidForEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedParticipants, response.getBody());
    }
}
