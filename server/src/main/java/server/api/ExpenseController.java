package server.api;
<<<<<<< HEAD
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
=======

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * dependency injection through constructor
     * @param expenseService the ExpenseService
     */
    @Autowired
    public ExpenseController(ExpenseService expenseService)
    {
        this.expenseService = expenseService;
    }

    /**
     * add a new expense
     * @param expense the expense to be added
     * @return ResponseEntity<Void> ok or bad request
     */
    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody Expense expense) {
        if (expense == null || expense.getParticipant() == null || expense.getParticipant().getFirstName() == null || expense.getParticipant().getLastName() == null) {
            return ResponseEntity.badRequest().build();
        }
        expenseService.createExpense(expense);
        return ResponseEntity.ok().build();
    }

    /**
     * get all the expenses
     * @return the list of expenses
     */
    @GetMapping("/")
    public List<Expense> getAll() {
        return expenseService.getAllExpenses();
    }

    /**
     * gets the expenses from a specific date
     * @param date the desired specific date
     * @return the list of expenses
     */
    @GetMapping("/{date}")
    public List<Expense> filterByDate(@PathVariable String date) {
        return expenseService.filterByDate(date);
    }

    /**
     * gets the expenses from the person who made the payment
     * @param participantId provided as a long
     * @return ResponseEntity<List<Expense>>
     */
    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<Expense>> filterExpenseByParticipant(@PathVariable long participantId) {
        List<Expense> expenses =  expenseService.filterByParticipantId(participantId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * gets the expenses from the people who need to pay something back
     * @param participantId provided as a long
     * @return ResponseEntity<List<Expense>>
     */
    @GetMapping("/involvedParticipant/{participantId}")
    public ResponseEntity<List<Expense>> filterByInvolving(@PathVariable long participantId) {
        List<Expense> expenses =  expenseService.filterByInvolving(participantId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * gets the details of an expense based off of its id
     * @param id the id of the desired expense
     * @return ResponseEntity<String> ok or not found
     */
    @GetMapping("/details/{id}")
    public ResponseEntity<String> getDetails(@PathVariable("id") long id) {
        try {
            Expense expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(expense.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * updates the expense
     * @param id the id of the expense that needs to be updated
     * @param updatedExpense the new updated version of the expense
     * @return ResponseEntity<Void> ok or not found
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") long id, @RequestBody Expense updatedExpense) {
        try {
            expenseService.updateExpense(id, updatedExpense);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * deletes an expense
     * @param id the id of the expense that needs to be deleted
     * @return ResponseEntity<Void> ok or not found
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


>>>>>>> origin/main
