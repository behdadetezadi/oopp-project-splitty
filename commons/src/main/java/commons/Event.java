package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@Entity
public class Event {

    //Event ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    //Event title
    private String title;

    //Todo: should be changed to a safer type for now we treat it as a number
    //Event invite code
    private long inviteCode;



    //List of Person s involved in the Event
    @OneToMany
    private List<Person> people;


    //List of Expense s in the Event
    @OneToMany
    private List<Expense> expenses;


    /**
     * Constructor for Event
     * @param people a List of Person s for the people involved in the Event
     * @param expenses a List of Expense s made in the Event
     * @param title a string stating the title of the Event
     * @param inviteCode the invitation code for sharing the Event (for now type long)
     */
    public Event(List<Person> people, List<Expense> expenses, String title, long inviteCode) {
        this.people = people;
        this.expenses = expenses;
        this.inviteCode = inviteCode;
        this.title = title;
    }

    /**
     * Empty public constructor (required)
     */
    public Event() {
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
    public List<Person> getPeople() {
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
     * This method adds a person to the list of Person s in the Event
     * @param person the Person to be added
     * @return true if the Person was added successfully otherwise returns false
     */
    public boolean addPerson(Person person){
        return this.people.add(person);
    }


    /**
     *
     * This method removes a Person from the list of Person s in the Event
     * @param person the person to be removed
     * @return true if the Person was removed successfully otherwise returns false
     */
    public boolean removePerson(Person person){
        return this.people.remove(person);
    }



    /**
     * Equals method using EqualsBuilder
     * @param obj the object to check the equality with
     * @return a boolean stating whether the object is equal to this or not (true if equal)
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash method using HashCodeBuilder
     * @return type int as a hashcode for this
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * toString method using ToStringBuilder using MULTI_LINE_STYLE (possibly needs to be changed in the future)
     * @return a type String which is the presentation of this in a suitable String format
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}