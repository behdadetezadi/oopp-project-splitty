package commons;

import jakarta.persistence.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
public class Participant {
    //Participant ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String iban; // international bank account number.
    private String bic; // bank identifier code. Similar to the iban, it is required in the backlog.
    @ElementCollection
    @CollectionTable(name = "participant_owed_amount", joinColumns = @JoinColumn(name = "participant_id"))
    @MapKeyJoinColumn(name = "event_id")
    @Column(name = "owed_amount")
    private Map<Event, Integer> owedAmount; //at the time of the code (no Event class yet).
    @ElementCollection
    @CollectionTable(name = "participant_owed_amount", joinColumns = @JoinColumn(name = "participant_id"))
    @MapKeyJoinColumn(name = "event_id")
    @Column(name = "owed_amount")    private Map<Event, Integer> payedAmount; //at the time of the code (no Event class yet).
    @ElementCollection
    private Set<Integer> eventIds;
    private String languageChoice;

    /**
     * Constructor of a participant
     * @param username String
     * @param firstName String
     * @param lastName String
     * @param email String
     * @param iban String
     * @param bic String
     * @param owedAmount HashMap
     * @param payedAmount HashMap
     * @param eventIds HashSet
     * @param languageChoice String
     */
    public Participant(String username, String firstName, String lastName, String email,
                       String iban, String bic, Map<Event, Integer> owedAmount,
                       Map<Event, Integer> payedAmount, Set<Integer> eventIds,
                       String languageChoice) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.owedAmount = owedAmount;
        this.payedAmount = payedAmount;
        this.eventIds = eventIds;
        this.languageChoice = languageChoice;
    }

    /**
     * basic constructor with only first and last names
     * @param firstName String
     * @param lastName String
     */
    public Participant(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Default constructor for my entity.
     */
    public Participant() {

    }

    /**
     * getter for the id
     * @return the id as a long
     */
    public long getId() {
        return id;
    }

    /**
     * username getter
     * @return String
     */
    public String getUsername() {
        return username;
    }
    /**
     * first name getter
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * last name getter
     * @return String
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * email getter
     * @return String
     */
    public String getEmail() {
        return email;
    }
    /**
     * IBAN getter
     * @return String
     */
    public String getIban() {
        return iban;
    }

    /**
     * BIC getter
     * @return String
     */
    public String getBic() {
        return bic;
    }
    /**
     * owed Amount map getter
     * @return hashmap
     */
    public Map<Event, Integer> getOwedAmount() {
        return owedAmount;
    }

    /**
     * payed amount map getter
     * @return hashmap
     */
    public Map<Event, Integer> getPayedAmount() {
        return payedAmount;
    }

    /**
     * get set of event ids
     * @return hashset
     */
    public Set<Integer> getEventIds() {
        return eventIds;
    }

    /**
     * language choice
     * @return String
     */
    public String getLanguageChoice() {
        return languageChoice;
    }

    /**
     * username setter
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * email setter
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * iban setter
     * @param iban
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * bic setter
     * @param bic
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * owed amount setter
     * @param owedAmount
     */
    public void setOwedAmount(Map<Event, Integer> owedAmount) {
        this.owedAmount = owedAmount;
    }

    /**
     * payed amount setter
     * @param payedAmount
     */
    public void setPayedAmount(Map<Event, Integer> payedAmount) {
        this.payedAmount = payedAmount;
    }

    /**
     * event ids setter
     * @param eventIds
     */
    public void setEventIds(Set<Integer> eventIds) {
        this.eventIds = eventIds;
    }

    /**
     * language choice setter
     * @param languageChoice
     */
    public void setLanguageChoice(String languageChoice) {
        this.languageChoice = languageChoice;
    }

    /**
     * Get owed amount for a particular event in a hashmap
     * @param event Event
     * @return Integer
     */
    public Integer getOwedAmountForEvent(Event event) {
        return owedAmount.getOrDefault(event, 0);
    }
    /**
     * Get owed amount for a particular event in a hashmap
     * @param event Event
     * @return Integer
     */
    public Integer getPaidAmountForEvent(Event event) {
        return payedAmount.getOrDefault(event, 0);
    }

    /**
     * equals method
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (!Objects.equals(username, that.username)) return false;
        if (!Objects.equals(firstName, that.firstName)) return false;
        if (!Objects.equals(lastName, that.lastName)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(iban, that.iban)) return false;
        if (!Objects.equals(bic, that.bic)) return false;
        if (!Objects.equals(owedAmount, that.owedAmount)) return false;
        if (!Objects.equals(payedAmount, that.payedAmount)) return false;
        if (!Objects.equals(eventIds, that.eventIds)) return false;
        return Objects.equals(languageChoice, that.languageChoice);
    }

    /**
     * private int hashcode
     * @return unique hash
     */
    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (iban != null ? iban.hashCode() : 0);
        result = 31 * result + (bic != null ? bic.hashCode() : 0);
        result = 31 * result + (owedAmount != null ? owedAmount.hashCode() : 0);
        result = 31 * result + (payedAmount != null ? payedAmount.hashCode() : 0);
        result = 31 * result + (eventIds != null ? eventIds.hashCode() : 0);
        result = 31 * result + (languageChoice != null ? languageChoice.hashCode() : 0);
        return result;
    }

    /**
     * to string method that is human readable
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Participant Info:\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Name: ").append(firstName).append(" ").append(lastName).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Bank Info:\n");
        sb.append("IBAN: ").append(iban).append("\n");
        sb.append("BIC: ").append(bic).append("\n");
        sb.append("Language Choice: ").append(languageChoice).append("\n");
        sb.append("Events Owed Amount:\n");
        for (Map.Entry<Event, Integer> entry : owedAmount.entrySet()) {
            sb.append("Owes for ").
                    append(entry.getKey().getTitle()).
                    append(": ").append(entry.getValue()).
                    append("\n");
        }
        sb.append("Events Paid Amount:\n");
        for (Map.Entry<Event, Integer> entry : payedAmount.entrySet()) {
            sb.append("Paid for ").
                    append(entry.getKey().getTitle()).
                    append(": ").append(entry.getValue()).
                    append("\n");
        }
        sb.append("Event IDs: ").
                append(eventIds).
                append("\n");
        return sb.toString();
    }


    /**
     * adding into hahsmap owed amount
     * @param event Event
     * @param amount int
     */
    public void addOwedAmountForSpecificEvent(Event event, int amount) {
        owedAmount.put(event, amount);
    }
    /**
     * adding into hahsmap paid amount
     * @param event Event
     * @param amount int
     */
    public void addPaidAmountForSpecificEvent(Event event, int amount) {
        payedAmount.put(event, amount);
    }

    /**
     * boolean checker if someone owes for an event
     * @param event Event
     * @return boolean
     */
    public boolean owesForEvent(Event event) {
        return owedAmount.containsKey(event) && owedAmount.get(event) > 0;
    }

    /**
     * boolean event
     * @param event
     * @return boolean
     */
    public boolean hasPaidForEvent(Event event) {
        return payedAmount.containsKey(event) && payedAmount.get(event) > 0;
    }

    /**
     * calculates total owed amount
     * @return int
     */
    public int calculateOwed() {
        int totalOwed = 0;
        for (int amount : owedAmount.values()) {
            totalOwed = totalOwed +  amount;
        }
        return totalOwed;
    }

    /**
     * calculates total paid amount
     * @return int
     */
    public int calculatePaid() {
        int totalPaid = 0;
        for (int amount : payedAmount.values()) {
            totalPaid = totalPaid + amount;
        }
        return totalPaid;
    }
}
