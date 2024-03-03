package server.api;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * getter for event by title
     * @param title the title of this event
     * @return the event
     */

    @GetMapping("/{title}")
    public Event getEventByTitle(@PathVariable String title) {
        return eventService.getEventByTitle(title);
    }

    /**
     * getter for event by invite code
     * @param inviteCode the invite code
     * @return the event
     */

    @GetMapping("/{inviteCode}")
    public Event getEventByInviteCode(@PathVariable Long inviteCode) {
        return eventService.getEventByInviteCode(inviteCode);
    }

    /**
     * delete an event by its id
     * @param id the event to be deleted
     * @return nothing really
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEventById(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return  ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete the event.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
