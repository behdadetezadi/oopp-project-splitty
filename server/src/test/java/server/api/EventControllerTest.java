package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.EventService;
import server.api.EventController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;
    private Participant participant;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        participant = new Participant();
        participant.setId(1L);
        participant.setFirstName("Test");
        participant.setLastName("User");    }

    @Test
    public void testGetEventByInviteCode() {
        Event expectedEvent = new Event("Test Event");
        expectedEvent.setId(1);

        when(eventService.getEventByInviteCode(anyLong())).thenReturn(expectedEvent);

        Event result = eventController.getEventByInviteCode(12345L);

        assertEquals(expectedEvent, result);
    }

    @Test
    public void testDeleteEventById_Success() {
        doNothing().when(eventService).deleteEvent(anyLong());

        ResponseEntity<?> expectedResponse = ResponseEntity.noContent().build();
        ResponseEntity<?> result = eventController.deleteEventById(1L);

        assertEquals(expectedResponse, result);
    }

    @Test
    public void testDeleteEventById_Failure() {
        //TODO
    }

    @Test
    public void testGetAllEvents() {
        List<Event> expectedEvents = new ArrayList<>();
        expectedEvents.add(new Event("Event 1"));
        expectedEvents.add(new Event("Event 2"));

        when(eventService.getAllEvents()).thenReturn(expectedEvents);

        List<Event> result = eventController.getAllEvents();

        assertEquals(expectedEvents, result);
    }

    @Test
    public void testCreateEvent() {
        Event eventToCreate = new Event("New Event");
        Event createdEvent = new Event("New Event");
        createdEvent.setId(1);

        when(eventService.createEvent(any(Event.class))).thenReturn(createdEvent);

        Event result = eventController.createEvent(eventToCreate);

        assertEquals(createdEvent, result);
    }

    @Test
    public void addParticipant_Success() {
        when(eventService.addParticipantToEvent(anyLong(), any(Participant.class))).thenReturn(participant);
        ResponseEntity<Participant> response = eventController.addParticipant(1L, participant);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participant, response.getBody());
        verify(eventService).addParticipantToEvent(1L, participant);
    }

    @Test
    public void addParticipant_NotFound() {
        when(eventService.addParticipantToEvent(anyLong(), any(Participant.class))).thenReturn(null);
        ResponseEntity<Participant> response = eventController.addParticipant(1L, participant);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(eventService).addParticipantToEvent(1L, participant);
    }

    @Test
    public void removeParticipant_Success() {
        doNothing().when(eventService).removeParticipantFromEvent(anyLong(), anyLong());
        ResponseEntity<Void> response = eventController.removeParticipant(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService).removeParticipantFromEvent(1L, 1L);
    }

    @Test
    public void updateParticipant_Success() {
        Participant updatedParticipant = new Participant();
        updatedParticipant.setId(1L);
        updatedParticipant.setFirstName("Updated");
        updatedParticipant.setLastName("User");
        when(eventService.updateParticipantInEvent(anyLong(), anyLong(), any(Participant.class))).thenReturn(updatedParticipant);

        ResponseEntity<Participant> response = eventController.updateParticipantInEvent(1L, 1L, updatedParticipant);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedParticipant, response.getBody());
        verify(eventService).updateParticipantInEvent(1L, 1L, updatedParticipant);
    }

    @Test
    public void updateParticipant_BadRequest() {
        when(eventService.updateParticipantInEvent(anyLong(), anyLong(), any(Participant.class))).thenThrow(new IllegalArgumentException("Invalid request"));
        ResponseEntity<Participant> response = eventController.updateParticipantInEvent(1L, 1L, new Participant());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(eventService).updateParticipantInEvent(1L, 1L, new Participant());
    }

    @Test
    public void updateParticipant_InternalServerError() {
        when(eventService.updateParticipantInEvent(anyLong(), anyLong(), any(Participant.class))).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<Participant> response = eventController.updateParticipantInEvent(1L, 1L, new Participant());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(eventService).updateParticipantInEvent(1L, 1L, new Participant());
    }
}

