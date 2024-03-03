package server.api;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService)
    {
        this.expenseService = expenseService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody Expense expense) {
        if (expense == null || expense.getParticipant() == null || expense.getParticipant().getFirstName() == null || expense.getParticipant().getLastName() == null) {
            return ResponseEntity.badRequest().build();
        }
        expenseService.createExpense(expense);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public List<Expense> getAll() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{date}")
    public List<Expense> filterByDate(@PathVariable String date) {
        return expenseService.filterByDate(date);
    }

    /**
     * filtering expenses by the person who made the payment
     * @param participantId provided as a long
     * @return ResponseEntity<List<Expense>>
     */
    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<Expense>> filterExpenseByParticipant(@PathVariable long participantId) {
        List<Expense> expenses =  expenseService.filterByParticipantId(participantId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * filtering expenses by the person who needs to pay something back
     * @param participantId provided as a long
     * @return ResponseEntity<List<Expense>>
     */
    @GetMapping("/involvedParticipant/{participantId}")
    public ResponseEntity<List<Expense>> filterByInvolving(@PathVariable long participantId) {
        List<Expense> expenses =  expenseService.filterByInvolving(participantId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<String> getDetails(@PathVariable("id") long id) {
        try {
            Expense expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(expense.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") long id, @RequestBody Expense updatedExpense) {
        try {
            expenseService.updateExpense(id, updatedExpense);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

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


