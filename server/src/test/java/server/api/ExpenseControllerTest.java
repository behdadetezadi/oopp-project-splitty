package server.api;

import commons.Expense;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExpenseControllerTest {

    private ExpenseController controller;

    @Mock
    private ExpenseRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ExpenseController(repository);
    }


    @Test
    void addExpense()
    {
        Expense expense = new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");

        when(repository.save(expense)).thenReturn(expense);

        ResponseEntity<Void> response = controller.addExpense(expense);

        verify(repository, times(1)).save(expense);

    }

    @Test
    void getAllExpenses()
    {
        Expense expense1=new Expense(new Person("Carlos","Sainz"),"dinner",16000,"EUR","2023-08-31",List.of(new Person("Carlos","Sainz")),"Food");
        Expense expense2=new Expense(new Person("Steph","Curry"),"dinner",16000,"EUR","2023-08-31",List.of(new Person("Steph","Curry")),"Food");
        List<Expense> expenses = Arrays.asList(expense1, expense2);

        when(repository.findAll()).thenReturn(expenses);

        List<Expense> result = controller.getAllExpenses();

        verify(repository, times(1)).findAll();

        assertEquals(expenses, result);
        when(repository.findAll()).thenReturn(expenses);
        assertEquals(expenses, controller.getAllExpenses());
    }

    @Test
    void filterExpensesByDate()
    {
        String date = "2023-08-27";
        Expense expense=new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        List<Expense> expenses = Arrays.asList(expense);

        when(repository.findAllByDate(date)).thenReturn(expenses);

        List<Expense> result = controller.filterExpensesByDate(date);

        verify(repository, times(1)).findAllByDate(date);

        assertEquals(expenses, result);
    }

    @Test
    void addMoneyTransfer()
    {
        Expense transfer = new Expense();
        ResponseEntity<Void> responseEntity = controller.addMoneyTransfer(transfer);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(repository, times(1)).save(transfer);
    }

    @Test
    void filterExpensesByPerson()
    {
        Expense expense=new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        Person person=new Person("Jodie","Zhao");
        when(repository.findAllByPerson(person)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), controller.filterExpensesByPerson(person));
    }

    @Test
    void filterExpensesInvolvingSomeone()
    {
        Expense expense=new Expense(new Person("Jodie","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        Person person=new Person("Jodie","Zhao");
        when(repository.findAllBySplittingOptionContaining(person)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), controller.filterExpensesInvolvingSomeone(person));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    public void testGetExpenseDetails(long id) {
        // Mock data
        Expense expense = (id == 1) ? new Expense(new Person("Jodie","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education") : null;
        Optional<Expense> optionalExpense = Optional.ofNullable(expense);

        when(repository.findById(id)).thenReturn(optionalExpense);

        ResponseEntity<String> response = controller.getExpenseDetails(id);

        verify(repository, times(1)).findById(id);


        assertEquals(HttpStatus.OK, response.getStatusCode());

        if (optionalExpense.isPresent()) {
            String expectedResponseBody = "Optional["+optionalExpense.get().toString()+"]"; // Extract Expense from Optional
            assertEquals(expectedResponseBody, response.getBody());
        } else {
            String expectedResponseBody = "Optional.empty";
            assertEquals(expectedResponseBody, response.getBody());
        }
    }
    @Test
    public void testUpdateExpense() {
        long id = 1;
        Expense updatedExpense = new Expense(new Person("Yanran","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        Expense existingExpense = new Expense(new Person("Jodie","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");

        when(repository.findById(id)).thenReturn(Optional.of(existingExpense));
        when(repository.save(existingExpense)).thenReturn(existingExpense);

        ResponseEntity<Void> response = controller.updateExpense(id, updatedExpense);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existingExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteExpense() {
        long id = 1;

        ResponseEntity<Void> response = controller.deleteExpense(id);

        verify(repository, times(1)).deleteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

