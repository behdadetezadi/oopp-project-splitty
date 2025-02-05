package server.api;

import commons.*;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.EventService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private Map<Object, Consumer<Participant>> listeners = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate template;

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
     * Deletes all events.
     * @return A response entity indicating the outcome of the operation.
     */
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllEvents() {
        try {
            eventService.deleteAllEvents();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete all events: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    /**
     * websocket method for updating participant titles
     * @param payload map
     * @return event
     */
    @MessageMapping("/eventTitle")
    @SendTo("/topic/eventTitle")
    public Event updateEventByTitleWebsockets(Map<String, Object> payload){
        long eventId = Long.parseLong(payload.get("eventId").toString());
        String newTitle = payload.get("newTitle").toString();
        Event event = eventService.updateEventTitle(eventId, newTitle);
        return event;
    }

    /**
     * finds participants by the event id
     * @param id as a long number
     * @return an array list of participants if possible (need to modify this later on for try-catch)
     */
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Participant>> getParticipantsByEventId(@PathVariable Long id) {
        List<Participant> participants = eventService.findParticipantsByEventId(id);
        return ResponseEntity.ok(participants);
    }

    /**
     * Returns updates about participants by event ID with long polling.
     * @param id the event ID to fetch participant updates for
     * @return a deferred result wrapped in a ResponseEntity containing participant updates or no content
     */
    @GetMapping("/{id}/participants/updates")
    public DeferredResult<ResponseEntity<Participant>> getParticipantUpdatesByEventId(@PathVariable Long id) {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Participant>>(5000L,noContent);
        var key = new Object();//since never equal to each other (always diff instances)
        listeners.put(key, participant -> res.setResult( ResponseEntity.ok(participant)));
        res.onCompletion(() -> listeners.remove(key));
        return res;

    }

    /**
     * Adds a participant to an event and notifies listeners.
     * @param eventId the ID of the event
     * @param participant the participant to add
     * @return a ResponseEntity containing the added participant or a notFound status
     */
    @PostMapping("/{eventId}/participants")
    public ResponseEntity<Participant> addParticipant(@PathVariable long eventId, @RequestBody Participant participant) {
        Participant addedParticipant = eventService.addParticipantToEvent(eventId, participant);
        if (addedParticipant != null) {
            listeners.forEach((k,l) -> l.accept(addedParticipant));
            return ResponseEntity.ok(addedParticipant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Removes a participant from an event, including all related expenses.
     * @param eventId the ID of the event
     * @param participantId the ID of the participant to remove
     * @return an OK ResponseEntity if successful
     */
    @DeleteMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable long eventId, @PathVariable long participantId)
    {

        List<Expense>expenseList=eventService.filterByParticipantId(eventId,participantId);
        for(Expense expense:expenseList)
        {
            removeExpense(eventId,expense.getId());
        }
        eventService.removeParticipantFromEvent(eventId, participantId);
        return ResponseEntity.ok().build();
    }

    /**
     * WebSockets endpoint to handle participant deletion.
     * @param request the ParticipantDeletionRequest containing event and participant IDs
     * @return the removed participant
     */
    @MessageMapping("/participantDeletion")
    @SendTo("/topic/participantDeletion")
    public Participant removeParticipantWebSockets(ParticipantDeletionRequest request){
        return eventService.removeParticipantFromEvent(request.getEventId(), request.getParticipantId());
    }

    /**
     * Updates a participant's details in an event.
     * @param eventId the ID of the event
     * @param participantId the ID of the participant to update
     * @param participantDetails the updated details of the participant
     * @return a ResponseEntity containing the updated participant or error status
     */
    @PutMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Participant> updateParticipantInEvent(@PathVariable Long eventId,
                                                                @PathVariable Long participantId,
                                                                @RequestBody Participant participantDetails) {
        try {
            Participant updatedParticipant = eventService
                    .updateParticipantInEvent(eventId, participantId, participantDetails);
            template.convertAndSend("/topic/participants", updatedParticipant);
            return ResponseEntity.ok(updatedParticipant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * WebSockets endpoint to update participant details.
     * @param eventId the event ID
     * @param participantId the participant ID
     * @param participantDetails the new details for the participant
     */
    @MessageMapping("/participants")
    @SendTo("/topic/participants")
    public void updateParticipantWebSockets( @PathVariable Long eventId,
                                             @PathVariable Long participantId,
                                             @RequestBody Participant participantDetails){
        updateParticipantInEvent(eventId, participantId, participantDetails);
    }

    /**
     * finds expenses by the event id
     * @param id as a long number
     * @return an array list of expenses if possible
     */
    @GetMapping("/{id}/expenses")
    public ResponseEntity<?> getExpensesByEventId(@PathVariable Long id) {
        List<Expense> expenses = eventService.findExpensesByEventId(id);
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }

    /**
     * adds expense to an event
     * @param eventId the event
     * @param expense the expense
     * @return response code
     */
    @PostMapping("/{eventId}/expenses")
    public ResponseEntity<Expense> addExpense(@PathVariable long eventId, @RequestBody Expense expense) {
        Expense addedExpense = eventService.addExpenseToEvent(eventId, expense);
        if (addedExpense != null) {
            return ResponseEntity.ok(addedExpense);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * WebSockets endpoint to add an expense to an event.
     * @param expenseRequest contains the details of the expense and event ID
     */
    @MessageMapping("/expense")
    @SendTo("/topic/expense")
    public void addExpenseWS(ExpenseRequest expenseRequest){
        long eventId = expenseRequest.getEventId();
        Expense expense = expenseRequest.getExpense();
        addExpense(eventId,expense);
    }

    /**
     * deletes an expense from an event
     * @param eventId the event
     * @param expenseId the expense
     * @return response code
     */
    @DeleteMapping("/{eventId}/expenses/{expenseId}")
    public ResponseEntity<Void> removeExpense(@PathVariable long eventId, @PathVariable long expenseId) {
        try {
            eventService.removeExpenseFromEvent(eventId, expenseId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * updates an expense from an event
     * @param eventId the event
     * @param expenseId the expense id
     * @param updatedExpense the updated expense
     * @return response code
     */
    @PutMapping("/{eventId}/expenses/{expenseId}")
    public ResponseEntity<Expense> updateExpenseInEvent(
            @PathVariable Long eventId,
            @PathVariable Long expenseId,
            @RequestBody Expense updatedExpense) {
        try {
            Expense updated = eventService.updateExpenseInEvent(eventId, expenseId, updatedExpense);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}