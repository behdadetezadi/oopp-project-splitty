package server;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.*;

@Service
public class EventService {

    //private final Map<Long, Event> events = new HashMap<>();
    private final EventRepository eventRepository;
    //private long nextId = 1;
    /**
     * constructor
     * @param eventRepository this eventRepository
     */
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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


 //TODO
//    /**
//     * update an event
//     * @param eventId long number
//     * @param event Event
//     * @return Event
//     */
//    public Event updateEvent(long eventId, Event event) {
//        if (!events.containsKey(eventId)) {
//            throw new IllegalArgumentException("Event not found with ID: " + eventId);
//        }
//        event.setId(eventId);
//        events.put(eventId, event);
//        return event;
//    }

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
