package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;
    private List<Person> people;
    private List<Expense> expenses;

    @BeforeEach
    void setUp() {
        people = new ArrayList<>();
        expenses = new ArrayList<>();
        event = new Event(people, expenses, "Test Event", 123456);
    }


    @Test
    void testGetId() {
        assertEquals(0, event.getId()); // Assuming default ID is 0
    }

    @Test
    void testGetTitle() {
        assertEquals("Test Event", event.getTitle());
    }

    @Test
    void testGetInviteCode() {
        assertEquals(123456, event.getInviteCode());
    }

    @Test
    void testGetPeople() {
        assertEquals(people, event.getPeople());
    }

    @Test
    void testGetExpenses() {
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void testAddPerson() {
        Person person = new Person("John", "Doe");
        assertTrue(event.addPerson(person));
        assertTrue(event.getPeople().contains(person));
    }

    @Test
    void testRemovePerson() {
        Person person = new Person("John", "Doe");
        event.addPerson(person);
        assertTrue(event.removePerson(person));
        assertFalse(event.getPeople().contains(person));
    }

    @Test
    void testAddExpense() {
        Person person = new Person("John", "Foo");
        List<Person> splitOption = new ArrayList<>();
        Expense a = new Expense(person, "dinner", 12, "Euros", "01-01-2024", splitOption, "food");
        assertTrue(event.addExpense(a));
        assertTrue(event.getExpenses().contains(a));
    }

    @Test
    void testRemoveExpense() {
        Person person = new Person("John", "Foo");
        List<Person> splitOption = new ArrayList<>();
        Expense a = new Expense(person, "dinner", 12, "Euros", "01-01-2024", splitOption, "food");
        event.addExpense(a);
        assertTrue(event.removeExpense(a));
        assertFalse(event.getExpenses().contains(a));
    }

    @Test
    void testEqualsAndHashCode() {
        Event event1 = new Event(people, expenses, "Test Event", 123456);
        Event event2 = new Event(people, expenses, "Test Event", 123456);
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());

        // Test inequality
        Event event3 = new Event(people, expenses, "Different Event", 789101);
        assertNotEquals(event1, event3);
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(event.toString());
    }

    // Additional test cases for boundary conditions and edge cases

    @Test
    void testAddNullPerson() {
        assertFalse(event.addPerson(null));
    }

    @Test
    void testRemoveNonExistingPerson() {
        Person person = new Person("Jane", "Doe");
        assertFalse(event.removePerson(person));
    }

    @Test
    void testAddNullExpense() {
        assertFalse(event.addExpense(null));
    }

    @Test
    void testRemoveNonExistingExpense() {
        Person person = new Person("John", "Foo");
        List<Person> splitOption = new ArrayList<>();
        Expense a = new Expense(person, "lunch", 15, "USD", "01-02-2024", splitOption, "food");
        assertFalse(event.removeExpense(a));
    }
}

