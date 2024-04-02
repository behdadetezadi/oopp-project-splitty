package server.api;

import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.ExpenseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController controller;

    @Mock
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addExpenseTest() {
        Expense expense = new Expense(new Participant("Jodie","Zhao"),"CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", (long)1234);

        when(expenseService.createExpense(any(Expense.class))).thenReturn(ResponseEntity.ok("Expense created successfully"));
        ResponseEntity<Void> response = controller.add(expense);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        verify(expenseService, times(1)).createExpense(any(Expense.class));
    }

    @Test
    void testAddBadRequest() {
        Expense expense = new Expense();
        ResponseEntity<Void> responseEntity = controller.add(expense);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(expenseService, never()).createExpense(any());
    }


    @Test
    void testGetAll() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        expenses.add(new Expense());
        when(expenseService.getAllExpenses()).thenReturn(expenses);
        List<Expense> result = controller.getAll();
        assertNotNull(result);
        assertEquals(expenses.size(), result.size());
        verify(expenseService, times(1)).getAllExpenses();
    }

    @Test
    void testFilterByDate() {
        List<Expense> filteredExpenses = new ArrayList<>();

        when(expenseService.filterByDate("2024-03-31")).thenReturn(filteredExpenses);
        List<Expense> result = controller.filterByDate("2024-03-31");

        assertNotNull(result);
        assertEquals(filteredExpenses.size(), result.size());
        verify(expenseService, times(1)).filterByDate("2024-03-31");
    }

    @Test
    void testGetDetails() {
        Expense expense = new Expense();
        when(expenseService.getExpenseById(anyLong())).thenReturn(expense);
        ResponseEntity<String> response = controller.getDetails(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expense.toString(), response.getBody());
        verify(expenseService, times(1)).getExpenseById(1L);

    }

    @Test
    void testGetDetailsException() {
        when(expenseService.getExpenseById(anyLong())).thenThrow(new IllegalArgumentException());
        ResponseEntity<String> response = controller.getDetails(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(expenseService, times(1)).getExpenseById(1L);
    }
    @Test
    void getAllExpensesTest() {
        Expense expense1=new Expense(new Participant("Carlos","Sainz"),"dinner",16000,
                "EUR","2023-08-31", List.of(new Participant("Carlos","Sainz")),"Food", (long)2345);
        Expense expense2=new Expense(new Participant("Steph","Curry"),"dinner",16000,
                "EUR","2023-08-31",List.of(new Participant("Steph","Curry")),"Food", (long)2345);
        List<Expense> expenses = Arrays.asList(expense1, expense2);

        when(expenseService.getAllExpenses()).thenReturn(expenses);
        List<Expense> result = controller.getAll();
        verify(expenseService, times(1)).getAllExpenses();

        assertNotNull(result);
        assertEquals(expenses, result);
    }

    @Test
    void filterExpensesByDateTest() {
        String date = "2023-08-27";
        Expense expense=new Expense(new Participant("Jodie","Zhao"),"CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", (long)4576);
        List<Expense> expenses = List.of(expense);

        when(expenseService.filterByDate(date)).thenReturn(expenses);
        List<Expense> result = controller.filterByDate(date);
        verify(expenseService, times(1)).filterByDate(date);

        assertNotNull(result);
        assertEquals(expenses, result);
    }

    @Test
    void addMoneyTransferTest() {
        Expense transfer = new Expense(new Participant("Jay", "Z"), "dinner", 16000,
                "EUR", "2023-08-03", List.of(new Participant("Jay", "Z")), "food", (long)3456);

        when(expenseService.createExpense(any(Expense.class))).thenReturn(ResponseEntity.ok().body("Success"));

        ResponseEntity<Void> response = controller.add(transfer);

        verify(expenseService, times(1)).createExpense(any(Expense.class));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // No need to check for body content as the response type is ResponseEntity<Void>
    }


    @Test
    void filterExpensesByParticipantTest()
    {
        Expense expense=new Expense(new Participant("Jodie","Zhao"),"CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education",(long)2345);
        Participant participant=new Participant("Jodie","Zhao");

        when(expenseService.filterByParticipantId(participant.getId())).thenReturn(List.of(expense));
        ResponseEntity<List<Expense>> responseEntity = controller.filterExpenseByParticipant(participant.getId());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Expense> responseResult = responseEntity.getBody();
        assertNotNull(responseResult);
        assertEquals(List.of(expense), responseResult);
    }

    @Test
    void filterExpensesInvolvingSomeoneTest()
    {
        Expense expense=new Expense(new Participant("Jodie","Zhao")," CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", (long)1234);
        Participant participant=new Participant("Jodie","Zhao");

        when(expenseService.filterByInvolving(participant.getId())).thenReturn(List.of(expense));
        ResponseEntity<List<Expense>> responseEntity = controller.filterByInvolving(participant.getId());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Expense> responseResult = responseEntity.getBody();
        assertNotNull(responseResult);
        assertEquals(List.of(expense), responseResult);
    }


    @Test
    public void testUpdateExpense() {
        long id = 1;
        Expense updatedExpense = new Expense(new Participant("Yanran","Zhao")," CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education",(long)2344);

        when(expenseService.updateExpense(id, updatedExpense)).thenReturn(updatedExpense);
        ResponseEntity<Void> response = controller.update(id, updatedExpense);
        verify(expenseService, times(1)).updateExpense(id, updatedExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateExpenseException() {
        long id = 1;
        Expense updatedExpense = new Expense(new Participant("Yanran","Zhao")," CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", (long)2345);

        when(expenseService.updateExpense(id, updatedExpense)).thenThrow(new IllegalArgumentException());
        ResponseEntity<Void> response = controller.update(id, updatedExpense);
        verify(expenseService, times(1)).updateExpense(id, updatedExpense);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteExpense() {
        long id = 1;
        when(expenseService.deleteExpense(id)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = controller.delete(id);
        verify(expenseService, times(1)).deleteExpense(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteExpenseNotFoundTest() {
        doThrow(new IllegalArgumentException()).when(expenseService).deleteExpense(anyLong());
        ResponseEntity<Void> response = controller.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(expenseService, times(1)).deleteExpense(1L);
    }

}




