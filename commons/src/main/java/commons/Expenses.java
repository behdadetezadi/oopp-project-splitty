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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Person> getSplittingOption() {
        return splittingOption;
    }

    public void setSplittingOption(List<Person> splittingOption) {
        this.splittingOption = splittingOption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

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
