package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


//for now invite code is equal to id

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private long inviteCode;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Participant> people;
    @OneToMany
    private List<Expense> expenses;


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
        this.inviteCode = this.id;
        this.title = title;
    }

    /**
     * constructor with just title
     * @param title title of the event
     */

    public Event(String title) {
        this.title = title;
        this.people = new ArrayList<>();
        this.expenses = new ArrayList<>();
        //this.inviteCode = Objects.hash(this.id);
        this.inviteCode = 5;

    }

    //TODO check constructor with invite code

    /**
     * Empty public constructor (required)
     */
    public Event() {
        this.inviteCode = 5;
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
        this.inviteCode = 5;
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
     * setter for the ID (might need to delete later)
     * @param id Respective Events ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * setter for the title of the event
     * @param title the title to be set as String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for the invite code (might need to be edited)
     * @param inviteCode the invite code of the Event
     */
    public void setInviteCode(long inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Setter for the list of participating people
     * @param people the list of Person s
     */
    public void setPeople(List<Participant> people) {
        this.people = people;
    }

    /**
     * Setter for the list of Expenses present in the Event
     * @param expenses a List of Expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
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
        return this.people.add(participant);
    }


    /**
     *
     * This method removes a Person from the list of Person s in the Event
     * @param participant the person to be removed
     * @return true if the Person was removed successfully otherwise returns false
     */
    public boolean removeParticipant(Participant participant){
        return this.people.remove(participant);
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
        return this.expenses.add(expense);
    }


    /**
     *
     * This method removes an expense from the list of Expenses in the Event
     * @param expense the expense to be removed
     * @return true if the expense was removed successfully otherwise returns false
     */
    public boolean removeExpense(Expense expense){
        return this.expenses.remove(expense);
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


    /**
     * java generated equals method
     * @param o Object
     * @return boolean
     */
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id || inviteCode != event.inviteCode) return false;
        if (!Objects.equals(title, event.title)) return false;
        if (!Objects.equals(people, event.people)) return false;
        return Objects.equals(expenses, event.expenses);
    }
*/






    /**
     * temporary hashcode look into fixing a failing pipeline (NEEDS TO BE FIXED LATER)
     * @return int
     */
    /*
    @Override
    public int hashCode() {
        return Objects.hash(title,inviteCode);
    }
    */
    /**
     * default toString
     * @return a type String
     */
    /*
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Event{");

        stringBuilder.append("id=").append(id).append(", ");

        if (title != null && !title.isEmpty()) {
            stringBuilder.append("title='").append(title).append("', ");
        }

        if (inviteCode != 0) {
            stringBuilder.append("inviteCode=").append(inviteCode).append(", ");
        }

        if (people != null && !people.isEmpty()) {
            stringBuilder.append("people=").append(people).append(", ");
        }

        if (expenses != null && !expenses.isEmpty()) {
            stringBuilder.append("expenses=").append(expenses).append(", ");
        }


        if (stringBuilder.length() > "Event{".length()) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }

        stringBuilder.append('}');
        String result = stringBuilder.toString();
        return  result;
    }
    */

}