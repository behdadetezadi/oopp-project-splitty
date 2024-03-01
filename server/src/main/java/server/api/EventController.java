package server.api;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.api.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    /**
     * Event Controller
     * @param eventService Event service
     */

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * getting event by the id
     * @param eventId long number which is event id
     * @return a long id
     */
    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable long eventId) {
        return eventService.getEventById(eventId);
    }

    /**
     * getter for all events
     * @return an array list of events
     */
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * post method to create an event
     * @param event Event
     * @return Event
     */
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    /**
     * event updates
     * @param eventId long number
     * @param event Event
     * @return an Event
     */
    @PutMapping("/{eventId}")
    public Event updateEvent(@PathVariable long eventId, @RequestBody Event event) {
        return eventService.updateEvent(eventId, event);
    }

    /**
     * method to delete event
     * @param eventId long number
     */
    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable long eventId) {
        eventService.deleteEvent(eventId);
    }
}
