package server.api;
import commons.Expense;
import commons.Person;
import java.util.List;
import java.util.stream.Collectors;



public class ExpenseController {
    private ExpenseModel expenseModel;

    /**
     * construct the ExpenseController object
     * @param expenseModel the expenseModel object including a expenseList
     */
    public ExpenseController(ExpenseModel expenseModel)
    {
        this.expenseModel = expenseModel;
    }
    /**
     * add an Expense object by passing individual parameters
     * @param person the person who paid
     * @param category what the expense was for
     * @param amount the amount that was spent
     * @param currency what currency was used for the expense
     * @param date when the expense was made
     * @param splittingOption shows a list of people that are included in the splitting option
     * @param expenseType the type of category the expense belongs to
     */

    public void addExpense(Person person, String category,
                           int amount, String currency, String date,
                           List<Person> splittingOption, String expenseType)
    {
        Expense expense = new Expense(person, category, amount, currency, date, splittingOption, expenseType);
        expenseModel.addExpense(expense);
    }

    /**
     * show all Expense objects in the list of expenseModel object
     * @return return expenseList of expenseModel object
     */
    public List<Expense> getAllExpenses() {
        return expenseModel.getAllExpenses();
    }
    /**
     * Display all expenses specific to that date.
     * @param date all the expenses of certain date that the user want to check
     */
    public List<Expense> filterExpensesByDate(String date) {
        return expenseModel.getAllExpenses().stream()
                .filter(expense -> expense.getDate().equals(date))
                .collect(Collectors.toList());
    }
    /** add money transfer form person A to B(I'm not sure if the requirement is need to add the transfer in to the expenseList in the expenseModel)
     * @param A the person who need to transfer the money
     * @param B the person who receive the transfer
     * @param amount the amount of transfer
     * @param currency the currency of the transfer
     * @param date the date of the transfer
     */
    public void addMoneyTransfer(Person A, Person B, int amount, String currency, String date)
    {
        Expense transfer = new Expense(A, "Money Transfer", amount, currency, date, List.of(B), "Transfer");
        expenseModel.getAllExpenses().add(transfer);
    }
    /**
     * Display all expenses specific to the person.
     * @param person the expense of certain person that the user want to check
     */
    public List<Expense> filterExpensesByPerson(Person person)
    {
        return expenseModel.getAllExpenses().stream()
                .filter(expense -> expense.getPerson().equals(person))
                .collect(Collectors.toList());
    }
    /**
     * Display all expenses involving certain person.
     * @param person the expense of certain person that the user want to check
     */
    public List<Expense> filterExpensesInvolvingSomeone(Person person) {
        return expenseModel.getAllExpenses().stream()
                .filter(expense -> expense.getSplittingOption().contains(person))
                .collect(Collectors.toList());
    }
    /**
     * Display all the details of certain expense.
     * @param expense the expense that the user want to check details
     */
    public String getExpenseDetails(Expense expense)
    {
        return expense.toString();
    }

}



