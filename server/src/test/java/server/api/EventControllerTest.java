package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.ParticipantDeletionRequest;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import server.EventService;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
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
        participant.setLastName("User");
    }

    @Test
    void testGetEventById() {
        Event expectedEvent = new Event();
        when(eventService.findEventById(1L)).thenReturn(Optional.of(expectedEvent));

        ResponseEntity<?> responseEntity = eventController.getEventById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEvent, responseEntity.getBody());

        verify(eventService, times(1)).findEventById(1L);
    }

    @Test
    void testGetEventByIdNotFound() {
        long eventId = 1L;
        when(eventService.findEventById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = eventController.getEventById(eventId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Event not found with ID: " + eventId, responseEntity.getBody());

        verify(eventService, times(1)).findEventById(eventId);
    }

    @Test
    void testGetEventByInvalidId() {
        long eventId = -1L;
        ResponseEntity<?> responseEntity = eventController.getEventById(eventId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetEventByIdIllegalArgumentException() {
        when(eventService.findEventById(anyLong())).thenThrow(new IllegalArgumentException("Invalid ID"));

        ResponseEntity<?> response = eventController.getEventById(anyLong());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody());
    }

    @Test
    void testGetParticipantUpdatesByEventId() throws InterruptedException {
        long eventId = 1L;
        Participant participant = new Participant();
        var listeners = new HashMap<Object, Consumer<Participant>>();
        try {
            Field listenersField = EventController.class.getDeclaredField("listeners");
            listenersField.setAccessible(true);
            listenersField.set(eventController, listeners);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        DeferredResult<ResponseEntity<Participant>> result = eventController.getParticipantUpdatesByEventId(eventId);
        var key = new Object();
        listeners.put(key, participantValue -> result.setResult(ResponseEntity.ok(participantValue)));

        TimeUnit.SECONDS.sleep(1);
        result.onCompletion(() -> {
            ResponseEntity<Participant> responseEntity = (ResponseEntity<Participant>) result.getResult();
            assert responseEntity != null;
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(participant, responseEntity.getBody());
        });
    }

    @Test
    void testGetEventByIdServiceException() {
        long eventId = 1L;
        when(eventService.findEventById(eventId)).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> responseEntity = eventController.getEventById(eventId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(eventService, times(1)).findEventById(eventId);
    }

    @Test
    void testGetEventByTitle() {
        List<Event> events = List.of(new Event());
        when(eventService.getEventByTitle("test")).thenReturn(events);

        ResponseEntity<?> responseEntity = eventController.getEventByTitle("test");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(events, responseEntity.getBody());

        verify(eventService, times(1)).getEventByTitle("test");
    }

    @Test
    void testGetEventByTitleException() {
        when(eventService.getEventByTitle("title")).thenThrow(new IllegalArgumentException("Invalid title"));
        ResponseEntity<?> responseEntity = eventController.getEventByTitle("title");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Failed to find event by title: Invalid title", responseEntity.getBody());

        verify(eventService, times(1)).getEventByTitle("title");
    }


    @Test
    void testGetEventByTitle_ServiceException() {
        when(eventService.getEventByTitle("title")).thenThrow(new ServiceException("Service exception"));
        ResponseEntity<?> responseEntity = eventController.getEventByTitle("title");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(eventService, times(1)).getEventByTitle("title");
    }

    @Test
    public void testGetEventByInviteCode() {
        Event expectedEvent = new Event("Test Event");
        expectedEvent.setId(1);

        when(eventService.getEventByInviteCode(anyLong())).thenReturn(expectedEvent);

        Event result = eventController.getEventByInviteCode(12345L);

        assertEquals(expectedEvent, result);
    }

    @Test
    void testGetEventByInviteCodeNotFound() {
        when(eventService.getEventByInviteCode(anyLong())).thenReturn(null);
        Event result = eventController.getEventByInviteCode(999999L);

        assertNull(result);
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
        doAnswer(invocation -> {
            throw new Exception("Failed to delete event");
        }).when(eventService).deleteEvent(anyLong());
        ResponseEntity<?> responseEntity = eventController.deleteEventById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed deletion.", responseEntity.getBody());
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
    void testUpdateEventTitle_Success() {
        when(eventService.updateEventTitle(anyLong(), anyString()))
                .thenReturn(new Event(new ArrayList<>(), new ArrayList<>(), "test", 1L));

        ResponseEntity<?> responseEntity = eventController.updateEventTitle(1L, "new");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(eventService, times(1)).updateEventTitle(1L, "new");
    }

    @Test
    void testUpdateEventTitleError() {
        when(eventService.updateEventTitle(anyLong(), anyString()))
                .thenThrow(new ServiceException("Update failed"));

        ResponseEntity<?> responseEntity = eventController.updateEventTitle(1L, "new");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to update event title: Update failed", responseEntity.getBody());

        verify(eventService, times(1)).updateEventTitle(1L, "new");
    }

    @Test
    void testUpdateEventTitleIllegalArgumentException() {
        when(eventService.updateEventTitle(anyLong(), anyString())).thenThrow(new IllegalArgumentException("Invalid ID"));

        ResponseEntity<?> response = eventController.updateEventTitle(1L, "new");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody());

        verify(eventService, times(1)).updateEventTitle(1L, "new");
    }

    @Test
    void testGetParticipantsByEventId() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("ab", "cd"));
        when(eventService.findParticipantsByEventId(anyLong())).thenReturn(participants);

        ResponseEntity<List<Participant>> responseEntity = eventController.getParticipantsByEventId(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(participants, responseEntity.getBody());

        verify(eventService, times(1)).findParticipantsByEventId(1L);
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
    public void removeParticipantSuccess() {
        ResponseEntity<Void> response = eventController.removeParticipant(1L, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService).removeParticipantFromEvent(1L, 1L);
    }

    @Test
    void testRemoveParticipantError() {
        doThrow(new RuntimeException("Failed to remove participant")).when(eventService).removeParticipantFromEvent(anyLong(), anyLong());

        assertThrows(RuntimeException.class, () -> eventController.removeParticipant(1L, 1L));

        verify(eventService, times(1)).removeParticipantFromEvent(1L, 1L);
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

    @Test
    void testUpdateParticipantWebSockets() {
        EventController eventControllerMock = mock(EventController.class);
        eventControllerMock.updateParticipantInEvent(1L, 1L, new Participant());

        verify(eventControllerMock).updateParticipantInEvent(1L, 1L, new Participant());
    }

    @Test
    public void testRemoveParticipantWebSockets() {
        ParticipantDeletionRequest request = new ParticipantDeletionRequest(1L, 2L);
        Participant expectedParticipant = new Participant();
        when(eventService.removeParticipantFromEvent(request.getEventId(), request.getParticipantId())).thenReturn(expectedParticipant);
        Participant result = eventController.removeParticipantWebSockets(request);
        assertEquals(expectedParticipant, result);
    }

    @Test
    void testGetExpensesByEventId() {
        when(eventService.findExpensesByEventId(1L)).thenReturn(List.of(new Expense()));
        ResponseEntity<?> response = eventController.getExpensesByEventId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(new Expense()), response.getBody());
    }

    @Test
    void testGetExpensesByEventIdNothingFound() {
        when(eventService.findExpensesByEventId(1L)).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = eventController.getExpensesByEventId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getEventByIdNotFoundCase() {
        long eventId = 2L;
        when(eventService.findEventById(eventId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = eventController.getEventById(eventId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found with ID: " + eventId, response.getBody());
        verify(eventService).findEventById(eventId);
    }

    @Test
    public void getEventByTitleNoEventsFound() {
        String title = "Nonexistent";
        when(eventService.getEventByTitle(title)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = eventController.getEventByTitle(title);

        assertTrue(((List<?>) Objects.requireNonNull(response.getBody())).isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService).getEventByTitle(title);
    }

    @Test
    public void updateEventTitleEventNotFound() {
        long eventId = 1L;
        String newTitle = "Updated Title";
        when(eventService.updateEventTitle(eventId, newTitle)).thenThrow(new ServiceException("Event not found"));

        ResponseEntity<?> response = eventController.updateEventTitle(eventId, newTitle);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to update event title: Event not found", ((ResponseEntity<String>) response).getBody());
        verify(eventService).updateEventTitle(eventId, newTitle);
    }

    @Test
    public void addExpenseSuccessful() {
        long eventId = 1L;
        Expense expense = new Expense();
        expense.setExpenseType("Lunch");
        expense.setAmount(20.0);

        when(eventService.addExpenseToEvent(eq(eventId), any(Expense.class))).thenReturn(expense);

        ResponseEntity<Expense> response = eventController.addExpense(eventId, expense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Lunch", response.getBody().getExpenseType());
        verify(eventService).addExpenseToEvent(eq(eventId), any(Expense.class));
    }

    @Test
    void testAddExpenseNotFound() {
        when(eventService.addExpenseToEvent(1L, new Expense())).thenReturn(null);
        ResponseEntity<Expense> response = eventController.addExpense(1L, new Expense());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateEventTitleNotFound() {
        long eventId = 99L;
        String newTitle = "Nonexistent Event";
        when(eventService.updateEventTitle(eq(eventId), anyString())).thenThrow(new ServiceException("Event not found"));

        ResponseEntity<?> response = eventController.updateEventTitle(eventId, newTitle);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(eventService).updateEventTitle(eq(eventId), anyString());
    }

    @Test
    public void updateParticipantNotFound() {
        Participant updatedParticipant = new Participant();
        updatedParticipant.setId(1L);
        updatedParticipant.setFirstName("Nonexistent");
        updatedParticipant.setLastName("Participant");

        when(eventService.updateParticipantInEvent(anyLong(), anyLong(), any(Participant.class)))
                .thenThrow(new ServiceException("Participant not found"));

        ResponseEntity<Participant> response = eventController.updateParticipantInEvent(1L, 1L, updatedParticipant);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(eventService).updateParticipantInEvent(anyLong(), anyLong(), any(Participant.class));
    }

    @Test
    void testRemoveExpense() {
        doNothing().when(eventService).removeExpenseFromEvent(1L, 1L);

        ResponseEntity<Void> response = eventController.removeExpense(1L, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveExpenseNotFound() {
        doThrow(IllegalArgumentException.class).when(eventService).removeExpenseFromEvent(1L, 1L);
        ResponseEntity<Void> response = eventController.removeExpense(1L, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateExpenseInEvent() {
        when(eventService.updateExpenseInEvent(1L, 1L, new Expense())).thenReturn(new Expense());
        ResponseEntity<Expense> response = eventController.updateExpenseInEvent(1L, 1L, new Expense());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new Expense(), response.getBody());
    }

    @Test
    void testUpdateExpenseInEventBadRequest() {
        when(eventService.updateExpenseInEvent(1L, 1L, new Expense())).thenThrow(IllegalArgumentException.class);
        ResponseEntity<Expense> response = eventController.updateExpenseInEvent(1L, 1L, new Expense());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateExpenseInEvent_InternalServerError() {
        when(eventService.updateExpenseInEvent(1L, 1L, new Expense())).thenThrow(new RuntimeException());
        ResponseEntity<Expense> response = eventController.updateExpenseInEvent(1L, 1L, new Expense());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}