package server.api;

import commons.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.ExpenseService;
import server.database.ExpenseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class ExpenseServiceTest {
    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExpenseByIdFound() {
        long id = 1L;
        Expense expense = new Expense();
        expense.setId(id);
        when(expenseRepository.findById(id)).thenReturn(Optional.of(expense));

        Expense found = expenseService.getExpenseById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void getExpenseByIdNotFound() {
        long id = 1L;
        when(expenseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> expenseService.getExpenseById(id));
    }

    @Test
    void getAllExpenses() {
        Expense expense1 = new Expense();
        Expense expense2 = new Expense();
        List<Expense> expenses = Arrays.asList(expense1, expense2);
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.getAllExpenses();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void createExpenseSuccess() {
        Expense expense = new Expense();
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        ResponseEntity<String> response = expenseService.createExpense(expense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Expense created successfully"));
    }

    @Test
    void createExpenseFailure() {
        Expense expense = new Expense();
        when(expenseRepository.save(any(Expense.class))).thenThrow(new DataAccessException("Error") {
        });

        ResponseEntity<String> response = expenseService.createExpense(expense);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error creating expense"));
    }

    @Test
    void filterByDateValidDate() {
        String validDate = "2024-03-19";
        List<Expense> expectedExpenses = Arrays.asList(new Expense(), new Expense());
        when(expenseRepository.findAllByDate(validDate)).thenReturn(expectedExpenses);

        List<Expense> result = expenseService.filterByDate(validDate);

        assertNotNull(result);
        assertEquals(expectedExpenses.size(), result.size());
    }

    @Test
    void filterByDateInvalidDateFormat() {
        String invalidDate = "2024/03/19";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                expenseService.filterByDate(invalidDate));
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    void filterByParticipantIdFound() {
        long participantId = 1L;
        List<Expense> expectedExpenses = Arrays.asList(new Expense(), new Expense());
        when(expenseRepository.findAllByParticipantId(participantId)).thenReturn(expectedExpenses);

        List<Expense> result = expenseService.filterByParticipantId(participantId);

        assertNotNull(result);
        assertEquals(expectedExpenses.size(), result.size());
    }

    @Test
    void filterByParticipantIdNotFound() {
        long participantId = 1L;
        when(expenseRepository.findAllByParticipantId(participantId)).thenReturn(Arrays.asList());

        Exception exception = assertThrows(NoSuchElementException.class, () ->
                expenseService.filterByParticipantId(participantId));
        assertTrue(exception.getMessage().contains("No expenses found for participant ID"));
    }

    @Test
    void filterByInvolvingFound() {
        long participantId = 1L;
        List<Expense> expectedExpenses = Arrays.asList(new Expense());
        when(expenseRepository.findAllBySplittingOptionContaining(participantId)).thenReturn(expectedExpenses);

        List<Expense> result = expenseService.filterByInvolving(participantId);

        assertNotNull(result);
        assertEquals(expectedExpenses, result);
    }

    @Test
    void filterByInvolvingNotFound() {
        long participantId = 1L;
        when(expenseRepository.findAllBySplittingOptionContaining(participantId)).thenReturn(Arrays.asList());

        Exception exception = assertThrows(NoSuchElementException.class, () ->
                expenseService.filterByInvolving(participantId));
        assertTrue(exception.getMessage().contains("No expenses found involving participant ID"));
    }

    @Test
    void updateExpenseFound() {
        long expenseId = 1L;
        Expense expenseToUpdate = new Expense();
        expenseToUpdate.setId(expenseId);
        when(expenseRepository.existsById(expenseId)).thenReturn(true);
        when(expenseRepository.save(expenseToUpdate)).thenReturn(expenseToUpdate);

        Expense result = expenseService.updateExpense(expenseId, expenseToUpdate);

        assertNotNull(result);
        assertEquals(expenseId, result.getId());
    }

    @Test
    void updateExpenseNotFound() {
        long expenseId = 1L;
        Expense expenseToUpdate = new Expense();
        when(expenseRepository.existsById(expenseId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                expenseService.updateExpense(expenseId, expenseToUpdate));
        assertTrue(exception.getMessage().contains("Expense not found with ID"));
    }

    @Test
    void deleteExpenseFound() {
        long expenseId = 1L;
        when(expenseRepository.existsById(expenseId)).thenReturn(true);

        ResponseEntity<Void> response = expenseService.deleteExpense(expenseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(expenseRepository, times(1)).deleteById(expenseId);
    }

    @Test
    void deleteExpenseNotFound() {
        long expenseId = 1L;
        when(expenseRepository.existsById(expenseId)).thenReturn(false);

        ResponseEntity<Void> response = expenseService.deleteExpense(expenseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}


