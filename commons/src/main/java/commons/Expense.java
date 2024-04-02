package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    private Participant participant;
    private String category;
    private double amount;
    private String currency;
    private String date;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "expense_splitting_option",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> splittingOption;
    private String expenseType;
    private Long eventId;

    /**
     * This is the constructor that initialises the expense
     * @param participant the person who paid
     * @param category what the expense was for
     * @param amount the amount that was spent
     * @param currency what currency was used for the expense
     * @param date when the expense was made
     * @param splittingOption shows a list of people that are included in the splitting option
     * @param expenseType the type of category the expense belongs to
     */
    public Expense(Participant participant, String category, double amount, String currency,
                    String date, List<Participant> splittingOption, String expenseType, Long eventId) {
        this.participant = participant;
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.splittingOption = splittingOption;
        this.expenseType = expenseType;
        this.eventId=eventId;
    }

    /**
     * Simpler constructor for the expense
     * @param participant the participant
     * @param category the category
     * @param amount the amount paid
     */
    public Expense(Participant participant, String category, double amount,long eventId) {
        this.participant = participant;
        this.category = category;
        this.amount = amount;
        this.eventId=eventId;
    }

    /**
     * constructor used by the jackson methods
     * @param id the expense id
     * @param participant the person who paid
     * @param category what the expense was for
     * @param amount the amount that was spent
     * @param currency what currency was used for the expense
     * @param date when the expense was made
     * @param splittingOption shows a list of people that are included in the splitting option
     * @param expenseType the type of category the expense belongs to
     * @param eventId the event Ids
     */
    public Expense(long id, Participant participant, String category,
                   double amount, String currency, String date,
                   List<Participant> splittingOption, String expenseType,
                   long eventId) {
        this.id = id;
        this.participant = participant;
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.splittingOption = splittingOption;
        this.expenseType = expenseType;
        this.eventId = eventId;
    }

    /**
     * constructor created for the purpose of persistence.
     */
    public Expense() {}

    /**
     * id for the database
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * setter for the id
     * @param id as a long number
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for person
     * @return the person who paid for the expense
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * getter for eventIds
     * @return the eventIds
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * setter for eventIds
     * @param eventId a hashset
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * Setter for person
     * @param participant the person who paid for the expense
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Getter for splittingOption
     * @return the people the bill is split between
     */
    public List<Participant> getSplittingOption() {
        return splittingOption;
    }

    /**
     * Setter for splittingOption
     * @param splittingOption the people the bill is split between
     */
    public void setSplittingOption(List<Participant> splittingOption) {
        this.splittingOption = splittingOption;
    }

    /**
     * Getter for category
     * @return what the expense is for
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter for category
     * @param category what the expense is for
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter for amount
     * @return the price of the expense
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setter for amount
     * @param amount the price of the expense
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Getter for currency
     * @return what currency was used for the expense
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Setter for currency
     * @param currency what currency was used for the expense
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Getter for date
     * @return when the expense was made
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for date
     * @param date when the expense was made
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for expenseType
     * @return what type of category the expense belongs to
     */
    public String getExpenseType() {
        return expenseType;
    }

    /**
     * Setter for expenseType
     * @param expenseType what type of category the expense belongs to
     */
    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }


    /**
     * equals method using equals builder (we should use this approach)
     * @param obj object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * hashcode using hash builder
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
