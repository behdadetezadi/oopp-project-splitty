package server.api;


import commons.Expense;
import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository repository;

    @Autowired
    public ExpenseController(ExpenseRepository repository)
    {
        this.repository = repository;
    }

    /**
     * Add an expense to the repository.
     * @param expense The expense to be added.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addExpense(@RequestBody Expense expense)
    {
        repository.save(expense);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieve all expenses from the repository.
     * @return A list of all expenses.
     */
    @GetMapping("/all")
    public List<Expense> getAllExpenses()
    {
        return repository.findAll();
    }

    /**
     * Filter expenses by date.
     * @param date The date to filter expenses.
     * @return A list of expenses on the specified date.
     */
    @GetMapping("/filterByDate/{date}")
    public List<Expense> filterExpensesByDate(@PathVariable String date)
    {
        return repository.findAllByDate(date);
    }

    /**
     * Add a money transfer to the repository.
     * @param transfer The money transfer to be added.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/addMoneyTransfer")
    public ResponseEntity<Void> addMoneyTransfer(@RequestBody Expense transfer) {
        repository.save(transfer);
        return ResponseEntity.ok().build();
    }

    /**
     * Filter expenses by person.
     * @param participant The person to filter expenses.
     * @return A list of expenses associated with the specified person.
     */
    @GetMapping("/filterByPerson/{person}")
    public List<Expense> filterExpensesByPerson(@PathVariable Participant participant) {
        return repository.findAllByPerson(participant);
    }

    /**
     * Filter expenses involving a specific person.
     * @param participant The person to filter expenses.
     * @return A list of expenses involving the specified person.
     */
    @GetMapping("/filterByInvolving/{person}")
    public List<Expense> filterExpensesInvolvingSomeone(@PathVariable Participant participant) {
        return repository.findAllBySplittingOptionContaining(participant);
    }

    /**
     * Get details of an expense by its ID.
     * @param id The ID of the expense to retrieve details.
     * @return ResponseEntity containing the details of the expense, or an error message if not found.
     */
    @GetMapping("/details/{id}")
    public ResponseEntity<String> getExpenseDetails(@PathVariable("id") long id) {
        Optional<Expense> expense = repository.findById(id);
        return ResponseEntity.ok((expense != null) ? expense.toString() : "Expense not found");
    }
    /**
     * update an expense
     * @param id The ID of the expense to retrieve details.
     * @param updatedExpense the updated version of the expense
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateExpense(@PathVariable("id") long id, @RequestBody Expense updatedExpense)
    {
        Optional<Expense> expenseOptional = repository.findById(id);
        if (expenseOptional.isPresent())
        {
            Expense existingExpense = expenseOptional.get();

            existingExpense.setDate(updatedExpense.getDate());
            existingExpense.setCategory(updatedExpense.getCategory());
            existingExpense.setCurrency(updatedExpense.getCurrency());
            existingExpense.setExpenseType(updatedExpense.getExpenseType());
            existingExpense.setAmount(updatedExpense.getAmount());
            existingExpense.setParticipant(updatedExpense.getParticipant());
            existingExpense.setSplittingOption(updatedExpense.getSplittingOption());
            repository.save(existingExpense);
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * delete an expense
     * @param id The ID of the expense to retrieve details.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") long id)
    {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

