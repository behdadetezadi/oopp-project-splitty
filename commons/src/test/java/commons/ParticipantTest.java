package commons;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {
    private Participant participant;
    private Event event1;
    private Event event2;
    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    void setUp() {
        participant = new Participant("username", "John", "Doe", "john.doe@example.com", "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");

        event1 = new Event(new ArrayList<>(), new ArrayList<>(), "Event 1", 12345L);
        event2 = new Event(new ArrayList<>(), new ArrayList<>(), "Event 2", 67890L);

        // The expense class is requiring a Person type, that's why this errors, we will have to change the expense class to fix this
        expense1 = new Expense(participant, "Food", 100, "USD", "2024-02-25", new ArrayList<>(), "Equal");
        expense2 = new Expense(participant, "Transport", 50, "USD", "2024-02-26", new ArrayList<>(), "Equal");

        event1.getExpenses().add(expense1);
        event2.getExpenses().add(expense2);
    }

    @Test
    void getUsername() {
        assertEquals("username", participant.getUsername());
    }

    @Test
    void getFirstName() {
        assertEquals("John", participant.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Doe", participant.getLastName());
    }

    @Test
    void getEmail() {
        assertEquals("john.doe@example.com", participant.getEmail());
    }

    @Test
    void getIban() {
        assertEquals("IBAN12345", participant.getIban());
    }

    @Test
    void getBic() {
        assertEquals("BIC67890", participant.getBic());
    }

  /*
    @Test
    void getOwedAmount() {
        assertTrue(participant.getOwedAmount().isEmpty());
        participant.getOwedAmount().put(event1, 100);
        assertEquals(100, participant.getOwedAmount().get(event1));
    }

    @Test
    void getPayedAmount() {
        assertTrue(participant.getPayedAmount().isEmpty());
        participant.getPayedAmount().put(event1, 50);
        assertEquals(50, participant.getPayedAmount().get(event1));
    }
*/
    @Test
    void getEventIds() {
        assertTrue(participant.getEventIds().isEmpty());
        participant.getEventIds().add((int) event1.getInviteCode()); // ! Explicitly casting long to int to avoid an error, this should be fixed later !
        assertTrue(participant.getEventIds().contains((int) event1.getInviteCode()));
    }

    @Test
    void getLanguageChoice() {
        assertEquals("EN", participant.getLanguageChoice());
    }

    @Test
    void setUsername() {
        participant.setUsername("newUsername");
        assertEquals("newUsername", participant.getUsername());
    }

    @Test
    void setEmail() {
        participant.setEmail("new.email@example.com");
        assertEquals("new.email@example.com", participant.getEmail());
    }

    @Test
    void setIban() {
        participant.setIban("NewIban");
        assertEquals("NewIban", participant.getIban());
    }

    @Test
    void setBic() {
        participant.setBic("NewBic");
        assertEquals("NewBic", participant.getBic());
    }

    @Test
    void setOwedAmount() {
        Map<Event, Integer> newOwed = new HashMap<>();
        newOwed.put(event1, 100);
        participant.setOwedAmount(newOwed);
        assertEquals(newOwed, participant.getOwedAmount());
    }

    @Test
    void setPayedAmount() {
        Map<Event, Integer> newPaid = new HashMap<>();
        newPaid.put(event1, 100);
        participant.setPayedAmount(newPaid);
        assertEquals(newPaid, participant.getPayedAmount());
    }

    @Test
    void setEventIds() {
        Set<Integer> newIds = new HashSet<>();
        newIds.add((int) event1.getInviteCode()); // ! Cast long to int before adding, this should be looked at and changed in the future !
        participant.setEventIds(newIds);
        assertEquals(newIds, participant.getEventIds());
    }

    @Test
    void setLanguageChoice() {
        participant.setLanguageChoice("FR");
        assertEquals("FR", participant.getLanguageChoice());
    }
/*
    @Test
    void addOwedAmountForSpecificEvent() {
        participant.addOwedAmountForSpecificEvent(event1, 100);
        assertEquals(100, participant.getOwedAmount().get(event1));
    }

    @Test
    void addPaidAmountForSpecificEvent() {
        participant.addPaidAmountForSpecificEvent(event1, 100);
        assertEquals(100, participant.getPayedAmount().get(event1));
    }

    @Test
    void owesForEvent() {
        assertFalse(participant.owesForEvent(event1));
        participant.addOwedAmountForSpecificEvent(event1, 100);
        assertTrue(participant.owesForEvent(event1));
    }

    @Test
    void hasPaidForEvent() {
        assertFalse(participant.hasPaidForEvent(event1));
        participant.addPaidAmountForSpecificEvent(event1, 100);
        assertTrue(participant.hasPaidForEvent(event1));
    }
*/
    @Test
    void calculateOwed() {
        participant.addOwedAmountForSpecificEvent(event1, 50);
        participant.addOwedAmountForSpecificEvent(event2, 100);
        assertEquals(150, participant.calculateOwed());
    }

    @Test
    void calculatePaid() {
        participant.addPaidAmountForSpecificEvent(event1, 30);
        participant.addPaidAmountForSpecificEvent(event2, 70);
        assertEquals(100, participant.calculatePaid());
    }
}