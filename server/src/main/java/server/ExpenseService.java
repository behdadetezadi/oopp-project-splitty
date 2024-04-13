package server;

import commons.Expense;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    /**
     * Dependency Injection through the constructor
     *
     * @param expenseRepository of type ExpenseRepository
     */
    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * gets the expense by its id
     *
     * @param expenseId the id of the expense that needs to be retrieved
     * @return the expense
     */
    public Expense getExpenseById(long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: "
                        + expenseId));
    }

    /**
     * gets all the expenses that are there
     *
     * @return a list containing all the expenses
     */
    public List<Expense> getAllExpenses() {
        try {
            return expenseRepository.findAll();
        } catch (Exception ex) {
            throw new ServiceException("Error retrieving all expenses", ex);
        }
    }

    /**
     * Filter expenses by date.
     *
     * @param date The date to filter expenses.
     * @return A list of expenses on the specified date.
     */
    public List<Expense> filterByDate(@PathVariable String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        try {
            return expenseRepository.findAllByDate(date);
        } catch (DataAccessException ex) {
            throw new ServiceException("Error accessing the data source to filter by date.", ex);
        }
    }


    /**
     * Filter expenses by person.
     * @param participantId The person to filter expenses.
     * @return A list of expenses associated with the specified person.
     */
    public List<Expense> filterByParticipantId(@PathVariable long participantId)
    {
        if (participantId < 0) {
            throw new IllegalArgumentException("Participant ID must be positive.");
        }
        List<Expense> expenses = expenseRepository.findAllByParticipantId(participantId);
        return expenses;
    }


    /**
     * Filter expenses involving a specific person.
     * @param participantId The person to filter expenses.
     * @return A list of expenses involving the specified person.
     */
    public List<Expense> filterByInvolving(@PathVariable long participantId)
    {
        if (participantId < 0) {
            throw new IllegalArgumentException("Participant ID must be positive.");
        }
        List<Expense> expenses = expenseRepository.findAllBySplittingOptionContaining(participantId);
        if (expenses.isEmpty()) {
            throw new NoSuchElementException("No expenses found involving participant ID: " + participantId);
        }
        return expenses;
    }

    /**
     * Get details of an expense by its ID.
     * @param id The ID of the expense to retrieve details.
     * @return ResponseEntity containing the
     * details of the expense, or an error message if not found.
     */
    public ResponseEntity<String> getDetails ( @PathVariable("id") long id){
        Optional<Expense> expense = expenseRepository.findById(id);
        return expense.map(value -> ResponseEntity.ok(value.toString())).orElseGet(()
                -> ResponseEntity.notFound().build());
    }

    /**
     * creates a new expense
     * @param expense the new expense
     * @return the new expense
     */
    public ResponseEntity<String> createExpense(Expense expense) {
        try {
            Expense savedExpense = expenseRepository.save(expense);
            return ResponseEntity.ok().body("Expense created successfully with ID: " + savedExpense.getId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating expense: " + e.getMessage());
        }
    }


    /**
     * Method used to update an expense
     * @param expenseId as a long number
     * @param expense as an Expense Object
     * @return an Expense Object
     */
    public Expense updateExpense ( long expenseId, Expense expense){
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("Expense not found with ID: " + expenseId);
        }
        expense.setId(expenseId);
        return expenseRepository.save(expense);
    }

    /**
     * deletes an expense
     * @param expenseId the id of the expense that needs to be deleted
     * @return ResponseEntity<Void>
     */
    public ResponseEntity<Void> deleteExpense ( long expenseId){
        if (!expenseRepository.existsById(expenseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        expenseRepository.deleteById(expenseId);
        return ResponseEntity.ok().build();
    }
}


