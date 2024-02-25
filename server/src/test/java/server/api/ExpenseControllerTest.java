package server.api;

import commons.Expense;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseControllerTest {

    private ExpenseController expenseController;

    @BeforeEach
    void setUp()
    {
        // Create a mock ExpenseModel
        ExpenseModel expenseModel = new ExpenseModel()
        {
            private final List<Expense> expensesList = new ArrayList<>();

            @Override
            public void addExpense(Expense expense)
            {
                expensesList.add(expense);
            }

            @Override
            public List<Expense> getAllExpenses()
            {
                return expensesList;
            }
        };

        // Initialize the ExpenseController with the mock ExpenseModel
        expenseController = new ExpenseController(expenseModel);
    }

    @Test
    void testAddExpense()
    {
        Person person = new Person("Jodie", "Zhao");
        List<Person> splittingOption = new ArrayList<>();
        splittingOption.add(person);
        expenseController.addExpense(person, "tuition fee", 15900, "EUR", "2023-08-27", splittingOption, "Education");

        List<Expense> expenses = expenseController.getAllExpenses();
        assertEquals(1, expenses.size());

        Expense addedExpense = expenses.get(0);
        assertEquals(person, addedExpense.getPerson());
        assertEquals("tuition fee", addedExpense.getCategory());
        assertEquals(15900, addedExpense.getAmount());
        assertEquals("EUR", addedExpense.getCurrency());
        assertEquals("2023-08-27", addedExpense.getDate());
        assertEquals(splittingOption, addedExpense.getSplittingOption());
        assertEquals("Education", addedExpense.getExpenseType());
    }

    @Test
    void testFilterExpensesByDate() {
        Person person = new Person("Jodie", "Zhao");
        List<Person> splittingOption = new ArrayList<>();
        splittingOption.add(person);
        expenseController.addExpense(person, "Shopping", 100, "EUR", "2024-02-25", splittingOption, "Retail");
        expenseController.addExpense(person, "Dining", 75, "EUR", "2024-02-26", splittingOption, "Food");
        expenseController.addExpense(person, "Groceries", 50, "EUR", "2024-02-27", splittingOption, "Food");

        List<Expense> filteredExpenses = expenseController.filterExpensesByDate("2024-02-26");
        assertEquals(1, filteredExpenses.size());
        assertEquals("Dining", filteredExpenses.get(0).getCategory());
    }

    @Test
    void testAddMoneyTransfer() {
        Person personA = new Person("Yanran", "Zhao");
        Person personB = new Person("Jodie", "Zhao");
        expenseController.addMoneyTransfer(personA, personB, 1350, "EUR", "2024-02-25");

        List<Expense> expenses = expenseController.getAllExpenses();
        assertEquals(1, expenses.size());

        Expense addedExpense = expenses.get(0);
        assertEquals(personA, addedExpense.getPerson());
        assertEquals(personB, addedExpense.getSplittingOption().get(0));
        assertEquals("Money Transfer", addedExpense.getCategory());
        assertEquals(1350, addedExpense.getAmount());
        assertEquals("EUR", addedExpense.getCurrency());
        assertEquals("2024-02-25", addedExpense.getDate());
        assertEquals("Transfer", addedExpense.getExpenseType());
    }

    @Test
    void testFilterExpensesByPerson() {
        Person personA = new Person("Carlos", "Sainz");
        Person personB = new Person("Jamel", "Musiala");
        List<Person> splittingOption = new ArrayList<>();
        splittingOption.add(personA);
        splittingOption.add(personB);
        expenseController.addExpense(personA, "Dining", 60, "USD", "2024-02-25", splittingOption, "Food");
        expenseController.addExpense(personB, "Shopping", 100, "EUR", "2024-02-26", splittingOption, "Retail");

        List<Expense> filteredExpenses = expenseController.filterExpensesByPerson(personA);
        assertEquals(1, filteredExpenses.size());
        assertEquals("Dining", filteredExpenses.get(0).getCategory());
    }

    @Test
    void testFilterExpensesInvolvingSomeone() {
        Person personA = new Person("Steph", "Curry");
        Person personB = new Person("Lebron", "James");
        Person personC = new Person("Klay", "Thompson");
        List<Person> splittingOption1 = new ArrayList<>();
        splittingOption1.add(personA);
        splittingOption1.add(personB);
        List<Person> splittingOption2 = new ArrayList<>();
        splittingOption2.add(personB);
        splittingOption2.add(personC);
        expenseController.addExpense(personA, "Dining", 60, "USD", "2024-02-25", splittingOption1, "Food");
        expenseController.addExpense(personB, "Shopping", 100, "EUR", "2024-02-26", splittingOption2, "Retail");

        List<Expense> filteredExpenses = expenseController.filterExpensesInvolvingSomeone(personA);
        assertEquals(1, filteredExpenses.size());
        assertEquals("Dining", filteredExpenses.get(0).getCategory());
    }

    @Test
    void testGetExpenseDetails() {
        Person person = new Person("Jodie", "Foster");
        List<Person> splittingOption = new ArrayList<>();
        splittingOption.add(person);
        expenseController.addExpense(person, "Groceries", 50, "USD", "2024-02-25", splittingOption, "Food");

        List<Expense> expenses = expenseController.getAllExpenses();
        assertEquals(1, expenses.size());

        Expense addedExpense = expenses.get(0);
        String expectedDetails = "Expense: " + person + " paid 50 USDfor Groceries on 2024-02-25. The bill is split between " + person;
        assertEquals(expectedDetails, expenseController.getExpenseDetails(addedExpense));
    }
}


