package commons;

import java.util.List;
import java.util.Objects;

public class Expenses {
    private Person person;
    private String category;
    private int amount;
    private String currency;
    private String date;
    private List<Person> splittingOption;
    private String expenseType;

    /**
     * This is the constructor that initialises the expense
     * @param person the person who paid
     * @param category what the expense was for
     * @param amount the amount that was spent
     * @param currency what currency was used for the expense
     * @param date when the expense was made
     * @param splittingOption shows a list of people that are included in the splitting option
     * @param expenseType the type of category the expense belongs to
     */
    public Expenses(Person person, String category, int amount, String currency,
                    String date, List<Person> splittingOption, String expenseType) {
        this.person = person;
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.splittingOption = splittingOption;
        this.expenseType = expenseType;
    }

    /**
     * Getter for person
     * @return the person who paid for the expense
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Setter for person
     * @param person the person who paid for the expense
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Getter for splittingOption
     * @return the people the bill is split between
     */
    public List<Person> getSplittingOption() {
        return splittingOption;
    }

    /**
     * Setter for splittingOption
     * @param splittingOption the people the bill is split between
     */
    public void setSplittingOption(List<Person> splittingOption) {
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
    public int getAmount() {
        return amount;
    }

    /**
     * Setter for amount
     * @param amount the price of the expense
     */
    public void setAmount(int amount) {
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
     * Equals method for expenses
     * @param o an object
     * @return whether two expenses are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expenses expenses)) return false;

        if (amount != expenses.amount) return false;
        if (!Objects.equals(person, expenses.person)) return false;
        if (!Objects.equals(category, expenses.category)) return false;
        if (!Objects.equals(currency, expenses.currency)) return false;
        if (!Objects.equals(date, expenses.date)) return false;
        if (!Objects.equals(splittingOption, expenses.splittingOption))
            return false;
        return Objects.equals(expenseType, expenses.expenseType);
    }

    /**
     * Makes a hashcode
     * @return the hashcode for the expense
     */
    @Override
    public int hashCode() {
        int result = person != null ? person.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + amount;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (splittingOption != null ? splittingOption.hashCode() : 0);
        result = 31 * result + (expenseType != null ? expenseType.hashCode() : 0);
        return result;
    }

    /**
     * Shows the details of the expense
     * @return a string
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Expense: " + person + " paid " + amount + " " + currency +
                "for " + category  + " on " + date + ". The bill is split between " );
        for(int i=0; i<splittingOption.size(); i++) {
            res.append(splittingOption.get(i));
            if (i != splittingOption.size() - 1) {
                res.append(", ");
            }
        }
        return res.toString();
    }
}
