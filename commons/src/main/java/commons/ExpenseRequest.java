package commons;

public class ExpenseRequest {
    private long eventId;
    private Expense expense;

    /**
     * constructor
     * @param eventId long
     * @param expense Expense
     */
    public ExpenseRequest(long eventId, Expense expense) {
        this.eventId = eventId;
        this.expense = expense;
    }

    /**
     * Default constructor for serialization/deserialization
     */
    public ExpenseRequest(){}

    /**
     * getter for event id
     * @return long
     */
    public long getEventId() {
        return eventId;
    }
    /**
     * setter for event id
     * @param eventId long
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
    /**
     * getter for expense
     * @return Expense
     */
    public Expense getExpense() {
        return expense;
    }
    /**
     * Setter for expense
     * @param expense Expense
     */
    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}
