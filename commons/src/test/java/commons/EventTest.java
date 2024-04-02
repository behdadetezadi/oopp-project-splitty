package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
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
        void testConstructor() {
            assertNotNull(event);
            assertEquals(people, event.getPeople());
            assertEquals(expenses, event.getExpenses());
            assertEquals("Test Event", event.getTitle());
            assertEquals(123456, event.getInviteCode());
        }

    @Test
    void testConstructorWithNullTitle() {
        List<Participant> people = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        Event event = new Event(people, expenses, null);
        assertNotNull(event);
        assertEquals(people, event.getPeople());
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void testConstructorWithJustTitle() {
        String title = "Test Event";
        Event event = new Event(title);
        assertNotNull(event);
        assertEquals(title, event.getTitle());
        assertNotNull(event.getPeople());
        assertTrue(event.getPeople().isEmpty());
        assertNotNull(event.getExpenses());
        assertTrue(event.getExpenses().isEmpty());
        assertNotEquals(0, event.getInviteCode());
    }

    @Test
    void testConstructor3() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("John", "Doe"));
        participants.add(new Participant("Jane", "Smith"));
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(new Participant("John", "Foo"), "food", 12));
        expenses.add(new Expense(new Participant("Jane", "Doe"), "food", 13));

        Event event = new Event(1L, "Test Event", 123456, participants, expenses);

        assertNotNull(event);
        assertEquals(1L, event.getId());
        assertEquals("Test Event", event.getTitle());
        assertEquals(123456, event.getInviteCode());
        assertEquals(participants, event.getPeople());
        assertEquals(expenses, event.getExpenses());
    }
    @Test
    void testConstructor4() {
        List<Participant> people = new ArrayList<>();
        people.add(new Participant("John", "Foo"));
        people.add(new Participant("Jane", "Doe"));
        Event event = new Event("test", people);

        assertNotNull(event);
        assertEquals("test", event.getTitle());
        assertEquals(people, event.getPeople());
        assertNotNull(event.getExpenses());
        assertNotEquals(0L, event.getInviteCode());
    }

    @Test
    void testSetId() {
        event.setId(12345L);
        assertEquals(12345L, event.getId());
    }

    @Test
    void testSetTitle() {
        event.setTitle("new title");
        assertEquals("new title", event.getTitle());
    }

    @Test
    void testSetInviteCode() {
        event.setInviteCode(987654321L);
        assertEquals(987654321L, event.getInviteCode());
    }

    @Test
    void testSetPeople() {
        List<Participant> people = new ArrayList<>();
        people.add(new Participant("John", "Foo"));
        event.setPeople(people);
        assertEquals(people, event.getPeople());
    }

    @Test
    void testSetExpenses() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(new Participant("John", "Foo"), "food", 12));
        expenses.add(new Expense(new Participant("Jane", "Doe"), "food", 13));
        event.setExpenses(expenses);
        assertEquals(expenses, event.getExpenses());
    }


    @Test
    void testGetId() {
        assertEquals(0, event.getId());
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
    void testAddParticipant() {
        Participant participant = new Participant("John", "Doe");
        assertTrue(event.addParticipant(participant));
        assertTrue(event.getPeople().contains(participant));
    }

    @Test
    void testRemoveParticipant() {
        Participant participant = new Participant("John", "Doe");
        event.addParticipant(participant);
        assertTrue(event.removeParticipant(participant));
        assertFalse(event.getPeople().contains(participant));
    }

    @Test
    void testAddExpense() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splitOption = new ArrayList<>();
        Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", splitOption, "food", new HashSet<>());
        assertTrue(event.addExpense(a));
        assertTrue(event.getExpenses().contains(a));
    }

    @Test
    void testRemoveExpense() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splitOption = new ArrayList<>();
        Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", splitOption, "food", new HashSet<>());
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

    @Test
    void testAddNullParticipant() {
        assertFalse(event.addParticipant(null));
    }

    @Test
    void testRemoveNonExistingParticipant() {
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
        Expense a = new Expense(participant, "lunch", 15, "USD", "01-02-2024", splitOption, "food", new HashSet<>());
        assertFalse(event.removeExpense(a));
    }
}

