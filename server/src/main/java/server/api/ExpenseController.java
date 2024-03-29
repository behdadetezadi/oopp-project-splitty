package server.api;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.ExpenseService;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private ExpenseRepository db;

    /**
     * dependency injection through constructor
     * @param expenseService the ExpenseService
     */
    @Autowired
    public ExpenseController(ExpenseService expenseService, ExpenseRepository db)
    {
        this.expenseService = expenseService;
        this.db=db;
    }

    /**
     * add a new expense
     * @param expense the expense to be added
     * @return ResponseEntity<Void> ok or bad request
     */
    @PostMapping("/")
    public ResponseEntity<Void> add(@RequestBody Expense expense) {
        if (expense == null || expense.getParticipant() == null ||
                expense.getParticipant().getFirstName() == null ||
                expense.getParticipant().getLastName() == null) {
            return ResponseEntity.badRequest().build();
        }
        expenseService.createExpense(expense);
//        db.save(expense);
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
    public ResponseEntity<List<Expense>>
        filterExpenseByParticipant(@PathVariable long participantId) {
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
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id")
                                           long id, @RequestBody Expense updatedExpense) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


