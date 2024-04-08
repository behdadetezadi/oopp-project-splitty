package commons;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private long inviteCode;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Participant> people;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Expense> expenses;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lastActivity;


    /**
     * Constructor for Event
     * @param people a List of Person s for the people involved in the Event
     * @param expenses a List of Expense s made in the Event
     * @param title a string stating the title of the Event
     * @param inviteCode the invitation code for sharing the Event (for now type long)
     */
    public Event(List<Participant> people, List<Expense> expenses, String title, long inviteCode) {
        this.people = people;
        this.expenses = expenses;
        this.inviteCode = inviteCode;
        this.title = title;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Constructor for Event
     * @param people a List of Person s for the people involved in the Event
     * @param expenses a List of Expense s made in the Event
     * @param title a string stating the title of the Event
     */
    public Event(List<Participant> people, List<Expense> expenses, String title) {
        this.people = people;
        this.expenses = expenses;
        this.inviteCode = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.title = title;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * constructor with just title
     * @param title title of the event
     */
    public Event(String title) {
        this.title = title;
        this.people = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.inviteCode = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * constructor
     * @param id event id
     * @param participants a List of Person s for the people involved in the Event
     * @param expenses a List of Expense s made in the Event
     * @param title a string stating the title of the Event
     * @param inviteCode the invitation code for sharing the Event (for now type long)
     */
    public Event(long id, String title, long inviteCode, List<Participant> participants, List<Expense> expenses) {
        this.id = id;
        this.title =title;
        this.inviteCode = inviteCode;
        this.people = participants;
        this.expenses = expenses;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Empty public constructor (required)
     */
    public Event() {
        this.inviteCode = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * constructor with the title and a list of participants (empty array list for expenses + invite code = 0 //TODO)
     * @param title  title of event
     * @param people the people involved
     */
    public Event(String title, List<Participant> people) {
        this.title = title;
        this.people = people;
        this.expenses = new ArrayList<>();
        this.inviteCode = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Getter for the ID
     * @return the ID of the Event
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the title of the Event
     * @return the title of the Event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the Invite code of the Event
     * @return the invite code of the Event
     */
    public long getInviteCode() {
        return inviteCode;
    }

    /**
     * Getter for the list of people involved in the Event
     * @return list of people involved in the Event
     */
    public List<Participant> getPeople() {
        return people;
    }

    /**
     * Getter for the list of Expenses in the Event
     * @return List of Expenses in the Event
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Getter for the creation date of the event
     * @return date and time of when the event was created
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Getter for the date of the last activity of the event
     * @return date and time of the last activity of the event
     */
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    /**
     * setter for the ID (might need to delete later)
     * @param id Respective Events ID
     */
    public void setId(long id) {
        this.id = id;
        updateLastActivity();
    }

    /**
     * setter for the title of the event
     * @param title the title to be set as String
     */
    public void setTitle(String title) {
        this.title = title;
        updateLastActivity();
    }

    /**
     * Setter for the invite code (might need to be edited)
     * @param inviteCode the invite code of the Event
     */
    public void setInviteCode(long inviteCode) {
        this.inviteCode = inviteCode;
        updateLastActivity();
    }

    /**
     * Setter for the list of participating people
     * @param people the list of Person s
     */
    public void setPeople(List<Participant> people) {
        this.people = people;
        updateLastActivity();
    }

    /**
     * Setter for the list of Expenses present in the Event
     * @param expenses a List of Expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        updateLastActivity();
    }

    /**
     * Method for updating the last activity date and time
     */
    private void updateLastActivity() {
        this.lastActivity = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * This method adds a person to the list of Person s in the Event
     * @param participant the Person to be added
     * @return true if the Person was added successfully otherwise returns false
     */
    public boolean addParticipant(Participant participant){
        if(participant == null){
            return false;
        }
        if(people.contains(participant)){
            return false;
        }
        updateLastActivity();
        return this.people.add(participant);
    }

    /**
     *
     * This method removes a Person from the list of Person s in the Event
     * @param participant the person to be removed
     * @return true if the Person was removed successfully otherwise returns false
     */
    public boolean removeParticipant(Participant participant){
        updateLastActivity();
        return this.people.remove(participant);
    }

    /**
     * Updates the details of an existing participant in the event.
     * @param participantDetails The updated details of the participant.
     * @return true if the participant was found and updated, false otherwise.
     */
    public boolean updateParticipant(Participant participantDetails) {
        for (Participant participant : this.people) {
            if (participant.getId() == participantDetails.getId()) {
                participant.setFirstName(participantDetails.getFirstName());
                participant.setLastName(participantDetails.getLastName());
                participant.setUsername(participantDetails.getUsername());
                participant.setEmail(participantDetails.getEmail());
                participant.setBic(participantDetails.getBic());
                participant.setIban(participantDetails.getIban());
                participant.setLanguageChoice(participantDetails.getLanguageChoice());
                updateLastActivity();
                return true;
            }
        }
        return false;
    }


    /**
     * This method adds an expense to the list of Expenses in the Event
     * @param expense the Expense to be added
     * @return true if the Expense was added successfully otherwise returns false
     */
    public boolean addExpense(Expense expense){
        if(expense == null){
            return false;
        }
        if (expenses.contains(expense)){
            return false;
        }
        updateLastActivity();
        return this.expenses.add(expense);
    }

    /**
     *
     * This method removes an expense from the list of Expenses in the Event
     * @param expense the expense to be removed
     * @return true if the expense was removed successfully otherwise returns false
     */
    public boolean removeExpense(Expense expense){
        updateLastActivity();
        return this.expenses.remove(expense);
    }

    /**
     * Updates the details of an existing expense in the event.
     * @param expenseDetails The updated details of the participant.
     * @return true if the participant was found and updated, false otherwise.
     */
    public boolean updateExpense(Expense expenseDetails) {
        for (Expense expense : this.expenses) {
            if (expense.getId() == expenseDetails.getId()) {
                expense.setParticipant(expenseDetails.getParticipant());
                expense.setCategory(expenseDetails.getCategory());
                expense.setAmount(expenseDetails.getAmount());
                expense.setCurrency(expenseDetails.getCurrency());
                expense.setDate(expenseDetails.getDate());
                expense.setSplittingOption(expenseDetails.getSplittingOption());
                expense.setExpenseType(expenseDetails.getExpenseType());
                updateLastActivity();
                return true;
            }
        }
        return false;
    }

    /**
     * equals method using equalsbuilder (we should use this approach)
     * @param obj object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * hashcode using hashbuilder
     * @return int representing hash
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString using ToStringBuilder
     * @return string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}