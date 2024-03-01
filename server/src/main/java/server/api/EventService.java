package server.api;

import commons.Event;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final Map<Long, Event> events = new HashMap<>();
    private long nextId = 1;

    /**
     * get event by the id
     * @param eventId long number
     * @return an event
     */
    public Event getEventById(long eventId) {
        return events.get(eventId);
    }

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
}
