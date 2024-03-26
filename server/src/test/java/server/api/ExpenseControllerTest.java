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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", new HashSet<>());

        when(expenseService.createExpense(any(Expense.class))).thenReturn(ResponseEntity.ok("Expense created successfully"));
        ResponseEntity<Void> response = controller.add(expense);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        verify(expenseService, times(1)).createExpense(any(Expense.class));
    }


    @Test
    void getAllExpensesTest() {
        Expense expense1=new Expense(new Participant("Carlos","Sainz"),"dinner",16000,
                "EUR","2023-08-31", List.of(new Participant("Carlos","Sainz")),"Food", new HashSet<>());
        Expense expense2=new Expense(new Participant("Steph","Curry"),"dinner",16000,
                "EUR","2023-08-31",List.of(new Participant("Steph","Curry")),"Food", new HashSet<>());
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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", new HashSet<>());
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
                "EUR", "2023-08-03", List.of(new Participant("Jay", "Z")), "food", new HashSet<>());

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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", new HashSet<>());
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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", new HashSet<>());
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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education", new HashSet<>());

        when(expenseService.updateExpense(id, updatedExpense)).thenReturn(updatedExpense);
        ResponseEntity<Void> response = controller.update(id, updatedExpense);
        verify(expenseService, times(1)).updateExpense(id, updatedExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
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

}




