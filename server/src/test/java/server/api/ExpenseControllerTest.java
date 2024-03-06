package server.api;
<<<<<<< HEAD

import commons.Expense;
import commons.Person;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseControllerTest
{

    private ExpenseController expenseController;
    private ExpenseModel expenseModel;

    @BeforeEach
    void start()
    {
        expenseModel = new ExpenseModel();
        expenseController = new ExpenseController(expenseModel);
    }

    @Test
    void addExpense()
    {
        Person person = new Person("Jodie","Zhao");
        String category = " CSE tution fee";
        int amount = 16000;
        String currency = "euro";
        String date = "2023-08-27";
        List<Person> splittingOption = List.of(person);
        String expenseType = "Education";
        expenseController.addExpense(person, category, amount, currency, date, splittingOption, expenseType);

        List<Expense> expenses = expenseController.getAllExpenses();
        assertEquals(1, expenses.size());
        assertEquals(person, expenses.get(0).getPerson());
        assertEquals(category, expenses.get(0).getCategory());
        assertEquals(amount, expenses.get(0).getAmount());
        assertEquals(currency, expenses.get(0).getCurrency());
        assertEquals(date, expenses.get(0).getDate());
        assertEquals(splittingOption, expenses.get(0).getSplittingOption());
        assertEquals(expenseType, expenses.get(0).getExpenseType());
    }

    @Test
    void filterExpensesByDate()
    {
        addExpense();
        String date = "2023-08-27";
        List<Expense> filteredExpenses = expenseController.filterExpensesByDate(date);
        assertEquals(1, filteredExpenses.size());
        assertEquals(date, filteredExpenses.get(0).getDate());
    }

    @Test
    void addMoneyTransfer() {
        Person personA = new Person("Yanran","Zhao");
        Person personB = new Person("Jodie","Zhao");
        int amount = 100;
        String currency = "euro";
        String date = "2024-02-24";

        expenseController.addMoneyTransfer(personA, personB, amount, currency, date);

        List<Expense> expenses = expenseController.getAllExpenses();
        assertEquals(1, expenses.size());
        assertEquals("Money Transfer", expenses.get(0).getCategory());
        assertEquals(amount, expenses.get(0).getAmount());
        assertEquals(currency, expenses.get(0).getCurrency());
        assertEquals(date, expenses.get(0).getDate());
        assertTrue(expenses.get(0).getSplittingOption().contains(personB));
        assertEquals("Transfer", expenses.get(0).getExpenseType());
    }

    @Test
    void filterExpensesByPerson()
    {
        addExpense();
        Person person= new Person("Jodie","Zhao");
        List<Expense> filteredExpenses = expenseController.filterExpensesByPerson(person);
        assertEquals(1, filteredExpenses.size());
        assertEquals(person, filteredExpenses.get(0).getPerson());
    }

    @Test
    void filterExpensesInvolvingSomeone()
    {
        addExpense();

        Person person = new Person("Jodie","Zhao");
        List<Expense> filteredExpenses = expenseController.filterExpensesInvolvingSomeone(person);

        assertEquals(1, filteredExpenses.size());
        assertTrue(filteredExpenses.get(0).getSplittingOption().contains(person));
    }

    @Test
    void getExpenseDetails()
    {
        addExpense();
        Expense expense = expenseController.getAllExpenses().get(0);
        String details = expenseController.getExpenseDetails(expense);

        if(details != null)
        {
            assertTrue(details.contains("Jodie"));
            assertTrue(details.contains("CSE tution fee"));
            assertTrue(details.contains("16000"));
            assertTrue(details.contains("euro"));
            assertTrue(details.contains("2023-08-27"));
        }
    }
}
=======
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");

        when(expenseService.createExpense(any(Expense.class))).thenReturn(expense);
        ResponseEntity<Void> response = controller.add(expense);
        verify(expenseService, times(1)).createExpense(any(Expense.class));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllExpensesTest() {
        Expense expense1=new Expense(new Participant("Carlos","Sainz"),"dinner",16000,
                "EUR","2023-08-31",List.of(new Participant("Carlos","Sainz")),"Food");
        Expense expense2=new Expense(new Participant("Steph","Curry"),"dinner",16000,
                "EUR","2023-08-31",List.of(new Participant("Steph","Curry")),"Food");
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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
        List<Expense> expenses = List.of(expense);

        when(expenseService.filterByDate(date)).thenReturn(expenses);
        List<Expense> result = controller.filterByDate(date);
        verify(expenseService, times(1)).filterByDate(date);

        assertNotNull(result);
        assertEquals(expenses, result);
    }

    @Test
    void addMoneyTransferTest() {
        Expense transfer = new Expense(new Participant("Jay","Z"),"dinner",16000,
                "EUR","2023-08-03",List.of(new Participant("Jay","Z")),"food");

        when(expenseService.createExpense(any(Expense.class))).thenReturn(transfer);
        ResponseEntity<Void> response = controller.add(transfer);
        verify(expenseService,times(1)).createExpense(any(Expense.class));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void filterExpensesByParticipantTest()
    {
        Expense expense=new Expense(new Participant("Jodie","Zhao"),"CSE tuition fee",16000,
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
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
                "EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");

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




>>>>>>> origin/main
