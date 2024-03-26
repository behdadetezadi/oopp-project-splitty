package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.EventService;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private EventService eventService;
    private Event event;
    private Participant participant;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event();
        event.setId(1L);
        event.setPeople(new ArrayList<>());

        participant = new Participant();
        participant.setId(1L);    }

    @Test
    void getAllEventsTest() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);
        List<Event> fetchedEvents = eventService.getAllEvents();

        assertEquals(events.size(), fetchedEvents.size());
        verify(eventRepository).findAll();
    }

    @Test
    void createEventTest() {
        Event event = new Event();
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event createdEvent = eventService.createEvent(event);

        assertEquals(event, createdEvent);
        verify(eventRepository).save(event);
    }

    @Test
    void deleteEventTest() {
        doNothing().when(eventRepository).deleteById(anyLong());
        eventService.deleteEvent(1L);

        verify(eventRepository).deleteById(1L);
    }

    @Test
    void findEventByIdTest() {
        Optional<Event> event = Optional.of(new Event());
        when(eventRepository.findById(anyLong())).thenReturn(event);
        Optional<Event> foundEvent = eventService.findEventById(1L);

        assertEquals(event, foundEvent);
        verify(eventRepository).findById(1L);
    }

    @Test
    void updateEventTitleTest() {
        Event event = new Event();
        event.setTitle("Title");
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event updatedEvent = eventService.updateEventTitle(1L, "New Title");

        assertEquals("New Title", updatedEvent.getTitle());
        verify(eventRepository).save(event);
    }

// TODO this is currently failing
//
//    @Test
//    void removeParticipantFromEventTest() {
//        Event event = new Event();
//        event.setId(1L);
//        Participant participant = new Participant();
//        participant.setId(1L);
//        event.setPeople(Arrays.asList(participant));
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//        eventService.removeParticipantFromEvent(1L, 1L);
//
//        verify(eventRepository).save(event);
//        assertEquals(0, event.getPeople().size());
//    }


    @Test
    void addParticipantToEventSuccessfully() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Participant result = eventService.addParticipantToEvent(event.getId(), participant);

        assertEquals(participant, result);
        assertTrue(event.getPeople().contains(participant));
        verify(eventRepository).save(event);
        verify(participantRepository).save(participant);
    }

    @Test
    void addParticipantToEventEventNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventService.addParticipantToEvent(1L, participant));
    }

// TODO this is currently failing
//
//    @Test
//    void removeParticipantFromEventSuccessfully() {
//        event.setPeople(Arrays.asList(participant));
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
//
//        eventService.removeParticipantFromEvent(event.getId(), participant.getId());
//
//        assertTrue(event.getPeople().isEmpty());
//        verify(eventRepository).save(event);
//    }

    @Test
    void removeParticipantFromEventEventNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                eventService.removeParticipantFromEvent(1L, 1L));
    }

    @Test
    void updateParticipantInEventSuccessfully() {
        Participant updatedDetails = new Participant();
        updatedDetails.setFirstName("Updated");
        updatedDetails.setLastName("Participant");
        updatedDetails.setUsername("updatedUsername");
        updatedDetails.setEmail("updated@example.com");
        updatedDetails.setBic("updatedBIC");
        updatedDetails.setIban("updatedIBAN");
        updatedDetails.setLanguageChoice("English");

        event.setPeople(Arrays.asList(participant));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        Participant updatedParticipant = eventService.updateParticipantInEvent(event.getId(), participant.getId(), updatedDetails);

        assertEquals("Updated", updatedParticipant.getFirstName());
        assertEquals("Participant", updatedParticipant.getLastName());
        assertEquals("updatedUsername", updatedParticipant.getUsername());
        assertEquals("updated@example.com", updatedParticipant.getEmail());
        assertEquals("updatedBIC", updatedParticipant.getBic());
        assertEquals("updatedIBAN", updatedParticipant.getIban());
        assertEquals("English", updatedParticipant.getLanguageChoice());
        verify(participantRepository).save(participant);
    }

    @Test
    void updateParticipantInEventEventNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                eventService.updateParticipantInEvent(1L, 1L, new Participant()));
    }

    @Test
    void updateParticipantInEventParticipantNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        assertThrows(IllegalArgumentException.class, () ->
                eventService.updateParticipantInEvent(event.getId(), participant.getId(), new Participant()));
    }
}

