package commons;

import java.util.Map;
import java.util.Set;


public class Participant {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String iban; // international bank account number.
    private String bic; // bank identifier code. Similar to the iban, it is required in the backlog.
    private Map<Event, Integer> owedAmount; //at the time of the code (no Event class yet).
    private Map<Event, Integer> payedAmount; //at the time of the code (no Event class yet).
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
}
