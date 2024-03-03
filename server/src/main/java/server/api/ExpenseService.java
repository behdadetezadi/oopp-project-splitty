package server.api;


import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense getExpenseById(long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + expenseId));
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    /**
     * Filter expenses by date.
     * @param date The date to filter expenses.
     * @return A list of expenses on the specified date.
     */
    public List<Expense> filterByDate(@PathVariable String date)
    {
        return expenseRepository.findAllByDate(date);
    }

    /**
     * Add a money transfer to the repository.
     * @param transfer The money transfer to be added.
     * @return ResponseEntity indicating the success of the operation.
     */
    public ResponseEntity<Void> addMoneyTransfer(@RequestBody Expense transfer)
    {
        if (transfer== null || transfer.getParticipant()==null||transfer.getParticipant().getFirstName()==null || transfer.getParticipant().getLastName()==null)
        {
            return ResponseEntity.badRequest().build();
        }
        expenseRepository.save(transfer);
        return ResponseEntity.ok().build();
    }

    /**
     * Filter expenses by person.
     * @param participantId The person to filter expenses.
     * @return A list of expenses associated with the specified person.
     */
    public List<Expense> filterByParticipantId(@PathVariable long participantId)
    {
        return expenseRepository.findAllByParticipantId(participantId);
    }

    /**
     * Filter expenses involving a specific person.
     * @param participantId The person to filter expenses.
     * @return A list of expenses involving the specified person.
     */
    public List<Expense> filterByInvolving(@PathVariable long participantId) {
        return expenseRepository.findAllBySplittingOptionContaining(participantId);
    }

    /**
     * Get details of an expense by its ID.
     * @param id The ID of the expense to retrieve details.
     * @return ResponseEntity containing the details of the expense, or an error message if not found.
     */
    public ResponseEntity<String> getDetails(@PathVariable("id") long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(value -> ResponseEntity.ok(value.toString())).orElseGet(() -> ResponseEntity.notFound().build());
    }


    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(long expenseId, Expense expense) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("Expense not found with ID: " + expenseId);
        }
        expense.setId(expenseId);
        return expenseRepository.save(expense);
    }

    public ResponseEntity<Void> deleteExpense(long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        expenseRepository.deleteById(expenseId);
        return ResponseEntity.ok().build();
    }
}
