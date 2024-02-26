package commons;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {


    /**
     * hello world method which servers no purpose at all.
     * @return it says hello world
     */
    @RequestMapping("/")
    public String helloWorld(){
        return "Hello World from Spring Boot";
    }
    /**
     * getter for event by id
     * @param eventId the id of the event to get
     * @return events with the corresponding id
     */
    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable long eventId) {

        return eventService.getEventById(eventId);
    }


    /**
     * getter for all events
     * @return a list containing all events
     */
    @GetMapping
    public List<Event> getAllEvents() {

        return eventService.getAllEvents();
    }

    /**
     * event creation method
     * @param event the event to be created
     * @return the event
     */

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    /**
     * method that updates events.
     * @param eventId: the id of the event that needs to be updated
     * @param event: the updated event
     * @return updates the event
     */
    @PutMapping("/{eventId}")
    public Event updateEvent(@PathVariable long eventId, @RequestBody Event event) {
        return eventService.updateEvent(eventId, event);
    }


    /**
     * method that deletes events.
     * @param eventId: the id of the event that needs to be updated
     */

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable long eventId) {
        eventService.deleteEvent(eventId);
    }

}