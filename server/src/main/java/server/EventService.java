package server;

import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    //private final Map<Long, Event> events = new HashMap<>();
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final ExpenseRepository expenseRepository;

    //private long nextId = 1;
    /**
     * constructor
     *
     * @param eventRepository       this eventRepository
     * @param participantRepository participant repo
     * @param expenseRepository the expense repository
     */
    public EventService(EventRepository eventRepository, ParticipantRepository participantRepository,
                        ExpenseRepository expenseRepository) {
        this.eventRepository = eventRepository;
        this.participantRepository=participantRepository;
        this.expenseRepository = expenseRepository;
    }

    /**
     * getter for all events
     * @return an array list of events
     */
    public List<Event> getAllEvents() {
        try{
            return eventRepository.findAll();
        } catch (Exception e){
            throw new ServiceException("Error retrieving all the events", e);
        }
    }

    /**
     * create an event
     * @param event Event
     * @return an Event
     */
    public Event createEvent(Event event) {
        if(event==null){
            throw new IllegalArgumentException("Event is not allowed to be null");
        }
        try{
            return eventRepository.save(event);
        } catch (Exception e){
            throw new ServiceException("Error saving the event", e);
        }
    }

    /**
     * delete event
     * @param id long number
     */
    public void deleteEvent(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be positive and not null");
        }
        try {
            eventRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException("Error deleting the event", e);
        }
    }

    /**
     * Deletes all events from the database.
     */
    @Transactional
    public void deleteAllEvents() {
        try {
            eventRepository.deleteAllInBatch();
        } catch (Exception e) {
            throw new ServiceException("Error deleting all events", e);
        }
    }



    /**
     * get event by the id
     * @param id long number
     * @return an event
     */
    public Optional<Event> findEventById(Long id) {
        if(id==null || (id < 0)){
            throw new IllegalArgumentException("ID must be positive and not null");
        }
        try {
            return eventRepository.findById(id);
        } catch (Exception e){
            throw new ServiceException("Error finding the participant by id", e);
        }
    }

    /**
     * get event by the title
     * @param title title of event
     * @return an event
     */
    public List<Event> getEventByTitle(String title) {
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
    public List<Participant> findParticipantsByEventId(long id) {
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


    /**
     * updates events title in eventRepository
     * @param eventId the id of event
     * @param newTitle the new title
     * @return the changed event
     */
    public Event updateEventTitle(Long eventId, String newTitle) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setTitle(newTitle);
            return eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
    }

    /**
     * adds participant to an event
     * @param eventId long
     * @param participant Participant
     * @return a Participant
     */
    public Participant addParticipantToEvent(long eventId, Participant participant) {
        participantRepository.save(participant);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        event.addParticipant(participant);
        eventRepository.save(event);
        return participant;
    }

    /**
     * remove participant from an event
     * @param eventId long
     * @param participantId long
     * @return participant
     */
    @Transactional
    public Participant removeParticipantFromEvent(long eventId, long participantId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        event.setPeople(event.getPeople().stream()
                .filter(p -> p.getId() != participantId)
                .collect(Collectors.toList()));
        eventRepository.save(event);
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        participant.getEventIds().removeIf(eId -> eId == eventId);
        participantRepository.save(participant);
        return participant;
    }

    /**
     * update participant in an event
     * @param eventId long
     * @param participantId long
     * @param participantDetails participant
     * @return the updated participant
     */
    public Participant updateParticipantInEvent(Long eventId, Long participantId, Participant participantDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        participantDetails.setId(participantId);

        boolean updated = event.updateParticipant(participantDetails);
        if (!updated) {
            throw new IllegalArgumentException("Participant with ID: " + participantId
                    + " not found in event with ID: " + eventId);
        }

        return participantRepository.save(participantDetails);
    }

    /**
     * get expenses of an event by the id
     * @param id id of event
     * @return list containing all expenses
     */
    public List<Expense> findExpensesByEventId(long id) {
        return eventRepository.expensesOfEventById(id);
    }

    /**
     * adds expense to an event
     * @param eventId long
     * @param expense expense
     * @return an expense
     */
    public Expense addExpenseToEvent(long eventId, Expense expense) {
        expenseRepository.save(expense);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        event.addExpense(expense);
        eventRepository.save(event);
        return expense;
    }

    /**
     * remove expense from an event
     * @param eventId long
     * @param expenseId long
     */

    public void removeExpenseFromEvent(long eventId, long expenseId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (event.getExpenses() == null) {
            event.setExpenses(new ArrayList<>());
        }
        else
        {
            event.setExpenses(event.getExpenses().stream()
                    .filter(p -> p.getId() != expenseId)
                    .collect(Collectors.toList()));
        }
        eventRepository.save(event);

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (expense.getEventId() == eventId) {
            expense.setEventId(null);
        }
        expenseRepository.save(expense);
    }


    /**
     * updates an expense in the event
     * @param eventId the event
     * @param expenseId the expense to be updated
     * @param expenseDetails the updated expense
     * @return the updated expense
     */
    public Expense updateExpenseInEvent(Long eventId, Long expenseId, Expense expenseDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        expenseDetails.setId(expenseId);

        boolean isUpdated = event.updateExpense(expenseDetails);
        if (!isUpdated) {
            throw new IllegalArgumentException("Expense with ID: " + expenseId
                    + " not found in event with ID: " + eventId);
        }

        return expenseRepository.save(expenseDetails);
    }

    /**
     * filters expenses by participant id
     * @param eventId event ID
     * @param participantId participant ID
     * @return list of filtered expenses by participant id
     */
    public List<Expense> filterByParticipantId(Long eventId, long participantId) {
        if (eventId == null || eventId < 0) {
            throw new IllegalArgumentException("Event ID must be positive and not null.");
        }
        if (participantId < 0) {
            throw new IllegalArgumentException("Participant ID must be positive.");
        }

        // Fetch the event to ensure it exists and to access its expenses.
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        // Filter the expenses by participantId.
        List<Expense> filteredExpenses = event.getExpenses().stream()
                .filter(expense -> expense.getParticipant() != null && expense.getParticipant().getId() == participantId)
                .collect(Collectors.toList());

        return filteredExpenses;
    }
}
