package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final Map<Long, Event> events = new HashMap<>();
    private final EventRepository eventRepository;
    private long nextId = 1;



    /**
     * getter for all events
     * @return an array list of events
     */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }

    /**
     * create an event
     * @param event Event
     * @return an Event
     */
    public Event createEvent(Event event) {
        event.setId(nextId++);
        events.put(event.getId(), event);
        return event;
    }

    /**
     * update an event
     * @param eventId long number
     * @param event Event
     * @return Event
     */
    public Event updateEvent(long eventId, Event event) {
        if (!events.containsKey(eventId)) {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
        event.setId(eventId);
        events.put(eventId, event);
        return event;
    }

    /**
     * delete event
     * @param eventId long number
     */
    public void deleteEvent(long eventId) {
        events.remove(eventId);
    }


    /**
     * constructor
     * @param eventRepository this eventRepository
     */
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * get event by the id
     * @param eventId long number
     * @return an event
     */
    public Event getEventById(long eventId) {
        return eventRepository.eventById(eventId);
    }

    /**
     * get event by the title
     * @param title title of event
     * @return an event
     */
    public Event getEventByTitle(String title) {
        return eventRepository.eventByTitle(title);
    }

    /**
     * get event by the invite code
     * @param inviteCode long number
     * @return an event
     */
    public Event getEventByInviteCode(long inviteCode) {
        return eventRepository.eventByInviteCode(inviteCode);
    }

    /**
     * get participants of an event by the id
     * @param id id of event
     * @return list containing all participants
     */
    public List<Participant> getParticipantsById(long id) {
        return eventRepository.participantsOfEventById(id);
    }
    /**
     * get event participants by the event title
     * @param title title of event
     * @return list containing all participants
     */

    public List<Participant> getParticipantsByTitle(String title) {
        return eventRepository.participantsOfEventByTitle(title);
    }

    /**
     * get event's participants by the invite code
     * @param inviteCode the events invite code
     * @return an event
     */
    public List<Participant> getParticipantsByInviteCode(long inviteCode) {
        return eventRepository.participantsOfEventByInviteCode(inviteCode);
    }


    /**
     * get event by expense
     * @param expense the expense
     * @return event corresponding to expense
     */

    public Event getEventByExpense(Expense expense) {
        return eventRepository.eventByExpense(expense);
    }

}
