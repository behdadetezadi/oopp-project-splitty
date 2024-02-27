package server.api;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.ExpenseRepository;
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
        Expense expense = new Expense(new Participant("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");

        when(repository.save(expense)).thenReturn(expense);

        ResponseEntity<Void> response = controller.add(expense);

        verify(repository, times(1)).save(expense);

    }

    @Test
    void getAllExpenses()
    {
        Expense expense1=new Expense(new Participant("Carlos","Sainz"),"dinner",16000,"EUR","2023-08-31",List.of(new Participant("Carlos","Sainz")),"Food");
        Expense expense2=new Expense(new Participant("Steph","Curry"),"dinner",16000,"EUR","2023-08-31",List.of(new Participant("Steph","Curry")),"Food");
        List<Expense> expenses = Arrays.asList(expense1, expense2);

        when(repository.findAll()).thenReturn(expenses);

        List<Expense> result = controller.getAll();

        verify(repository, times(1)).findAll();

        assertEquals(expenses, result);
        when(repository.findAll()).thenReturn(expenses);
        assertEquals(expenses, controller.getAll());
    }

    @Test
    void filterExpensesByDate()
    {
        String date = "2023-08-27";
        Expense expense=new Expense(new Participant("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
        List<Expense> expenses = Arrays.asList(expense);

        when(repository.findAllByDate(date)).thenReturn(expenses);

        List<Expense> result = controller.filterByDate(date);

        verify(repository, times(1)).findAllByDate(date);

        assertEquals(expenses, result);
    }

    @Test
    void addMoneyTransfer()
    {
        Expense transfer = new Expense(new Participant("Jay","Z"),"dinner",16000,"EUR","2023-08-03",List.of(new Participant("Jay","Z")),"food");
        ResponseEntity<Void> responseEntity = controller.addMoneyTransfer(transfer);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(repository, times(1)).save(transfer);
    }

    @Test
    void filterExpensesByParticipant()
    {
        Expense expense=new Expense(new Participant("Jodie","Zhao"),"CSE tution fee",16000,"EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
        Participant participant=new Participant("Jodie","Zhao");
        when(repository.findAllByParticipant(participant)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), controller.filterByPerson(participant));
    }

    @Test
    void filterExpensesInvolvingSomeone()
    {
        Expense expense=new Expense(new Participant("Jodie","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
        Participant participant=new Participant("Jodie","Zhao");
        when(repository.findAllBySplittingOptionContaining(participant)).thenReturn(List.of(expense));
        assertEquals(List.of(expense), controller.filterByInvolving(participant));
    }


    @Test
    public void testUpdateExpense() {
        long id = 1;
        Expense updatedExpense = new Expense(new Participant("Yanran","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");
        Expense existingExpense = new Expense(new Participant("Jodie","Zhao")," CSE tution fee",16000,"EUR","2023-08-27",List.of(new Participant("Jodie","Zhao")),"Education");

        when(repository.findById(id)).thenReturn(Optional.of(existingExpense));
        when(repository.save(existingExpense)).thenReturn(existingExpense);

        ResponseEntity<Void> response = controller.update(id, updatedExpense);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existingExpense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteExpense() {
        long id = 1;

        // Mocking repository behavior to return true for existsById(1L)
        when(repository.existsById(id)).thenReturn(true);

        ResponseEntity<Void> response = controller.delete(id);

        // Verify that deleteById is called with ID 1 exactly once
        verify(repository, times(1)).deleteById(id);

        // Verify that the controller returns HttpStatus.OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}


