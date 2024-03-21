package server.api;

import commons.Event;
import commons.Participant;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.EventService;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private EventRepository db;


    /**
     * Event Controller
     * @param eventService Event service
     * @param db database
     */

    @Autowired
    public EventController(EventService eventService, EventRepository db) {
        this.eventService = eventService;
        this.db=db;
    }

    /**
     * getting event by the id
     * @param id long number which is event id
     * @return a long id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id) {
        try {
            Optional<Event> eventOptional = eventService.findEventById(id);
            if(eventOptional.isPresent()){
                return ResponseEntity.ok(eventOptional.get());
            } else{
                return new ResponseEntity<>("Event not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to find Event by ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * getter for event by title
     * @param title the title of this event
     * @return the event
     */
    @GetMapping("/title/{title}")
    public ResponseEntity<?> getEventByTitle(@PathVariable String title) {
        try {
            List<Event> events = eventService
                    .getEventByTitle(title);
            return ResponseEntity.ok(events);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find event by title: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * getter for event by invite code
     * @param inviteCode the invite code
     * @return the event
     */

    @GetMapping("/inviteCode/{inviteCode}")
    public Event getEventByInviteCode(@PathVariable Long inviteCode) {
        return eventService.getEventByInviteCode(inviteCode);
    }

    /**
     * delete an event by its id
     * @param eventId the event to be deleted
     * @return nothing really
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEventById(@PathVariable Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Failed deletion.", HttpStatus.INTERNAL_SERVER_ERROR);
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
//        db.save(event);
        return eventService.createEvent(event);
    }

    /**
     * updates an events title
     * @param eventId id of event
     * @param newTitle the new title
     * @return response entity
     */
    @PutMapping("/{eventId}/updateTitle")
    public ResponseEntity<?> updateEventTitle(@PathVariable Long eventId, @RequestParam String newTitle) {
        try {
            Event updatedEvent = eventService.updateEventTitle(eventId, newTitle);
            return ResponseEntity.ok(updatedEvent);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to update event title: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


 //TODO
//    /**
//     * event updates
//     * @param eventId long number
//     * @param event Event
//     * @return an Event
//     */
//    @PutMapping("/{eventId}")
//    public Event updateEvent(@PathVariable long eventId, @RequestBody Event event) {
////        db.save(event);
//        return eventService.updateEvent(eventId, event);
//    }

}
