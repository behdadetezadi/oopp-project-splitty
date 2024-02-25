package server.api;

import commons.Expense;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.ExpenseRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExpenseControllerTest {

    private ExpenseController expenseController;

    @Mock
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expenseController = new ExpenseController(expenseRepository);
    }

    @Test
    void addExpense()
    {
        Expense expense=new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        ResponseEntity<Void> responseEntity = expenseController.addExpense(expense);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(expenseRepository, times(1)).save(expense);
        assertEquals(1, expenseController.getAllExpenses().size());
        assertEquals("Jodie Zhao",expenseController.getAllExpenses().get(0).getPerson());
        assertEquals(" CSE tution fee",expenseController.getAllExpenses().get(0).getCategory());
        assertEquals(16000, expenseController.getAllExpenses().get(0).getAmount());
        assertEquals("EUR", expenseController.getAllExpenses().get(0).getCurrency());
        assertEquals("2023-08-27", expenseController.getAllExpenses().get(0).getDate());
        assertEquals(List.of(new Person("Jodie","Zhao")), expenseController.getAllExpenses().get(0).getSplittingOption());
        assertEquals("Education", expenseController.getAllExpenses().get(0).getExpenseType());
    }

    @Test
    void getAllExpenses() {
        Expense expense=new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        when(expenseRepository.findAll()).thenReturn(List.of(expense));
        assertEquals(List.of(expense), expenseController.getAllExpenses());
    }

    @Test
    void filterExpensesByDate() {
        Expense expense=new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        String date="2023-08-27";
        when(expenseRepository.findAllByDate(date)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), expenseController.filterExpensesByDate(date));
    }

    @Test
    void addMoneyTransfer()
    {
        Expense transfer = new Expense();
        ResponseEntity<Void> responseEntity = expenseController.addMoneyTransfer(transfer);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(expenseRepository, times(1)).save(transfer);
    }

    @Test
    void filterExpensesByPerson()
    {
        Expense expense=new Expense(new Person("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        Person person=new Person("Jodie","Zhao");
        when(expenseRepository.findAllByPerson(person)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), expenseController.filterExpensesByPerson(person));
    }

    @Test
    void filterExpensesInvolvingSomeone()
    {
        Expense expense=new Expense(new Person("Jodie","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Person("Jodie","Zhao")),"Education");
        Person person=new Person("Jodie","Zhao");
        when(expenseRepository.findAllBySplittingOptionContaining(person)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), expenseController.filterExpensesInvolvingSomeone(person));
    }

    @Test
    void getExpenseDetails() {
        long id = 1L;
        Expense expense = new Expense();
        when(expenseRepository.findById(id)).thenReturn(expense);
        ResponseEntity<String> responseEntity = expenseController.getExpenseDetails(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expense.toString(), responseEntity.getBody());
    }
}
