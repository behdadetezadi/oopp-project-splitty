package server.database;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {


    /**
     * Finds participants associated with a specific event ID.
     *
     * @param eventId the ID of the event
     * @return list of participants associated with the event
     */
    @Query("SELECT p FROM Participant p JOIN p.eventIds e WHERE e = :eventId")
    List<Participant> participantsOfEventById(@Param("eventId") long eventId);

    /**
     * Finds expenses associated with a specific event ID.
     *
     * @param eventId the ID of the event
     * @return list of expenses associated with the event
     */
    @Query("SELECT p FROM Expense p JOIN p.eventIds e WHERE e = :eventId")
    List<Expense> expensesOfEventById(@Param("eventId") long eventId);

    /**
     * finds all participants of an event through its title
     * added this method just in case, I think the id method
     * would work better.
     * @param title the event's title
     * @return a list containing all participants
     */
    @Query("SELECT e FROM Event e WHERE e.title = :title")
    List<Participant> participantsOfEventByTitle(String title);

    /**
     * finds all participants of an event through its invite code
     * probably not needed but just in case
     * @param inviteCode the event's invite code
     * @return a list containing all participants
     */
    @Query("SELECT e FROM Event e WHERE e.inviteCode = :inviteCode")
    List<Participant> participantsOfEventByInviteCode(long inviteCode);


    /**
     * method that returns the event with this title
     * @param title title of the event
     * @return event corresponding with title
     */
    @Query("SELECT e FROM Event e WHERE e.title = :title")
    List<Event> eventByTitle(String title);

    /**
     * method that returns the event with this invite code
     * @param inviteCode invite code of the event
     * @return event corresponding with invite code
     */
    @Query("SELECT e FROM Event e WHERE e.inviteCode = :inviteCode")
    Event eventByInviteCode(long inviteCode);



    /**
     * finds all events which have this expanse
     * method probably not needed.
     * @param expense the expense to check
     * @return event  which has this expense
     */
    @Query("SELECT e FROM Event e WHERE :expense MEMBER OF e.expenses")
    Event eventByExpense(Expense expense);




}
