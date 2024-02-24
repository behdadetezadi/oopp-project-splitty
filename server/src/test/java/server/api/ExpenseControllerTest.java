package server.api;

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
