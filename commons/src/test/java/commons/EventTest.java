package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;
    private List<Participant> people;
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
        Participant participant = new Participant("John", "Doe");
        assertTrue(event.addParticipant(participant));
        assertTrue(event.getPeople().contains(participant));
    }

    @Test
    void testRemovePerson() {
        Participant participant = new Participant("John", "Doe");
        event.addParticipant(participant);
        assertTrue(event.removeParticipant(participant));
        assertFalse(event.getPeople().contains(participant));
    }

    @Test
    void testAddExpense() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splitOption = new ArrayList<>();
        Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", splitOption, "food");
        assertTrue(event.addExpense(a));
        assertTrue(event.getExpenses().contains(a));
    }

    @Test
    void testRemoveExpense() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splitOption = new ArrayList<>();
        Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", splitOption, "food");
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
        assertFalse(event.addParticipant(null));
    }

    @Test
    void testRemoveNonExistingPerson() {
        Participant participant = new Participant("Jane", "Doe");
        assertFalse(event.removeParticipant(participant));
    }

    @Test
    void testAddNullExpense() {
        assertFalse(event.addExpense(null));
    }

    @Test
    void testRemoveNonExistingExpense() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splitOption = new ArrayList<>();
        Expense a = new Expense(participant, "lunch", 15, "USD", "01-02-2024", splitOption, "food");
        assertFalse(event.removeExpense(a));
    }

    //TODO Unit Tests for Setters
}

