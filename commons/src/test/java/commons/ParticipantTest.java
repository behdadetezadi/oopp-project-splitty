package commons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class ParticipantTest {
    private Participant participant;
    private Event event1;
    private Event event2;
    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    void setUp() {
        participant = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        event1 = new Event(new ArrayList<>(), new ArrayList<>(), "Event 1", 12345L);
        event2 = new Event(new ArrayList<>(), new ArrayList<>(), "Event 2", 67890L);
        expense1 = new Expense(participant, "Food", 100,123);
        expense2 = new Expense(participant, "Transport", 50,456);
        event1.getExpenses().add(expense1);
        event2.getExpenses().add(expense2);
    }

    @Test
    void testFullConstructor() {
        assertEquals("username", participant.getUsername());
        assertEquals("John", participant.getFirstName());
        assertEquals("Doe", participant.getLastName());
        assertEquals("john.doe@example.com", participant.getEmail());
        assertEquals("IBAN12345", participant.getIban());
        assertEquals("BIC67890", participant.getBic());
        assertTrue(participant.getOwedAmount().isEmpty());
        assertTrue(participant.getPayedAmount().isEmpty());
        assertTrue(participant.getEventIds().isEmpty());
        assertEquals("EN", participant.getLanguageChoice());
    }

    @Test
    void testPartialConstructor() {
        Participant partialParticipant = new Participant("username", "John", "Doe",
                "john.doe@example.com", "IBAN12345", "BIC67890", "EN");
        assertEquals("username", partialParticipant.getUsername());
        assertEquals("John", partialParticipant.getFirstName());
        assertEquals("Doe", partialParticipant.getLastName());
        assertEquals("john.doe@example.com", partialParticipant.getEmail());
        assertEquals("IBAN12345", partialParticipant.getIban());
        assertEquals("BIC67890", partialParticipant.getBic());
        assertEquals("EN", partialParticipant.getLanguageChoice());
    }

    @Test
    void testPartialConstructor2() {
        Participant partialParticipant = new Participant("username", "John", "Doe",
                "john.doe@example.com", "IBAN12345", "BIC67890");
        assertEquals("username", partialParticipant.getUsername());
        assertEquals("John", partialParticipant.getFirstName());
        assertEquals("Doe", partialParticipant.getLastName());
        assertEquals("john.doe@example.com", partialParticipant.getEmail());
        assertEquals("IBAN12345", partialParticipant.getIban());
        assertEquals("BIC67890", partialParticipant.getBic());
    }

    @Test
    void testBasicConstructor2() {
        Participant basicParticipant = new Participant( "John", "Doe");
        assertEquals("John", basicParticipant.getFirstName());
        assertEquals("Doe", basicParticipant.getLastName());
    }

    @Test
    void testJacksonConstructor2() {
        Participant participant = new Participant(1L, "username", "firstName", "lastName", "email",
                "iban", "bic", new HashMap<>(), new HashMap<>(), new HashSet<>(), "languageChoice");
        assertEquals(1L, participant.getId());
        assertEquals("username", participant.getUsername());
        assertEquals("firstName", participant.getFirstName());
        assertEquals("lastName", participant.getLastName());
        assertEquals("email", participant.getEmail());
        assertEquals("iban", participant.getIban());
        assertEquals("bic", participant.getBic());
        assertEquals(new HashMap<>(), participant.getOwedAmount());
        assertEquals(new HashMap<>(), participant.getPayedAmount());
        assertEquals(new HashSet<>(), participant.getEventIds());
        assertEquals("languageChoice", participant.getLanguageChoice());
    }

    @Test
    public void testGetId() {
        long expectedId = 123;
        participant.setId(expectedId);
        assertEquals(expectedId, participant.getId());
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

    @Test
    void getEventIds() {
        assertTrue(participant.getEventIds().isEmpty());
        participant.getEventIds().add(event1.getInviteCode()); // ! Explicitly casting long to int to avoid an error, this should be fixed later !
        assertTrue(participant.getEventIds().contains(event1.getInviteCode()));
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
    void getOwedAmountForEventDefault(){
        assertEquals(0, participant.getOwedAmountForEvent(event1));
    }

    @Test
    void getPaidAmountForEventDefault(){
        assertEquals(0, participant.getPaidAmountForEvent(event1));
    }

    @Test
    void setOwedAmount() {
        Map<Event, Double> newOwed = new HashMap<>();
        newOwed.put(event1, 100.0);
        participant.setOwedAmount(newOwed);
        assertEquals(newOwed, participant.getOwedAmount());
    }

    @Test
    void setPayedAmount() {
        Map<Event, Double> newPaid = new HashMap<>();
        newPaid.put(event1, 100.0);
        participant.setPayedAmount(newPaid);
        assertEquals(newPaid, participant.getPayedAmount());
    }

    @Test
    void setEventIds() {
        Set<Long> newIds = new HashSet<>();
        newIds.add(event1.getInviteCode());
        participant.setEventIds(newIds);
        assertEquals(newIds, participant.getEventIds());
    }

    @Test
    void setLanguageChoice() {
        participant.setLanguageChoice("FR");
        assertEquals("FR", participant.getLanguageChoice());
    }

    @Test
    void testEqualsSameObject() {
        Participant participant = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertEquals(participant, participant);
    }

    @Test
    void testEqualsEqualObjects() {
        Participant participant1 = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        Participant participant2 = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertEquals(participant1, participant2);
        assertEquals(participant2, participant1);
    }

    @Test
    void testEqualsNullObject() {
        Participant participant = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertNotEquals(null, participant);
    }

    @Test
    void testEqualsDifferentClass() {
        Participant participant = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertNotEquals("participant", participant);
    }

    @Test
    void testEqualsDifferentUsername() {
        Participant participant1 = new Participant("username1", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        Participant participant2 = new Participant("username2", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertNotEquals(participant1, participant2);
        assertNotEquals(participant2, participant1);
    }

    @Test
    void testHashCodeConsistency() {
        Participant participant = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertEquals(participant.hashCode(), participant.hashCode());
    }

    @Test
    void testHashCodeEqualObjects() {
        Participant participant1 = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        Participant participant2 = new Participant("username", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    void testHashCodeDifferentObjects() {
        Participant participant1 = new Participant("username1", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        Participant participant2 = new Participant("username2", "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertNotEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    void testHashCodeNullFields() {
        Participant participant1 = new Participant(null, "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        Participant participant2 = new Participant(null, "John", "Doe", "john.doe@example.com",
                "IBAN12345", "BIC67890", new HashMap<>(), new HashMap<>(), new HashSet<>(), "EN");
        assertEquals(participant1.hashCode(), participant2.hashCode());
    }
    @Test
    void addOwedAmountForSpecificEventNegative() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> participant.addOwedAmountForSpecificEvent(event1,-10.0));
        assertEquals("Amount of money can't be negative!", exception.getMessage());
    }

    @Test
    void addPaidAmountForSpecificEventNegative() {
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> participant.addPaidAmountForSpecificEvent(event1,-10.0));
        assertEquals("Amount of money can't be negative!", exception.getMessage());
    }

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
    @Test
    void setFirstName() {
        participant.setFirstName("Jane");
        assertEquals("Jane", participant.getFirstName());
    }

    @Test
    void setLastName() {
        participant.setLastName("Smith");
        assertEquals("Smith", participant.getLastName());
    }

    @Test
    void setId() {
        long expectedId = 123;
        participant.setId(expectedId);
        assertEquals(expectedId, participant.getId());
    }

    @Test
    void testOwesForEventTrue() {
        Event testEvent = new Event();
        participant.addOwedAmountForSpecificEvent(testEvent, 100.0);
        assertTrue(participant.owesForEvent(testEvent));
    }

    @Test
    void testOwesForEventFalse() {
        Event testEvent = new Event();
        assertFalse(participant.owesForEvent(testEvent));
    }

    @Test
    void testHasPaidForEventTrue() {
        Event testEvent = new Event();
        participant.addPaidAmountForSpecificEvent(testEvent, 100.0);
        assertTrue(participant.hasPaidForEvent(testEvent));
    }

    @Test
    void testHasPaidForEventFalse() {
        Event testEvent = new Event();
        assertFalse(participant.hasPaidForEvent(testEvent));
    }

    @Test
    void testAddOwedAmountForSpecificEvent() {
        Event testEvent = new Event();
        participant.addOwedAmountForSpecificEvent(testEvent, 50.0);
        assertEquals(50.0, participant.getOwedAmount().get(testEvent));
    }

    @Test
    void testAddPaidAmountForSpecificEvent() {
        Event testEvent = new Event();
        participant.addPaidAmountForSpecificEvent(testEvent, 75.0);
        assertEquals(75.0, participant.getPayedAmount().get(testEvent));
    }

    @Test
    void testCalculateOwed() {
        Event event1 = new Event();
        Event event2 = new Event();
        participant.addOwedAmountForSpecificEvent(event1, 50.0);
        participant.addOwedAmountForSpecificEvent(event2, 100.0);
        double totalOwed = participant.calculateOwed();
        assertEquals(150.0, totalOwed);
    }

    @Test
    void testCalculatePaid() {
        Event event1 = new Event();
        Event event2 = new Event();
        participant.addPaidAmountForSpecificEvent(event1, 30.0);
        participant.addPaidAmountForSpecificEvent(event2, 70.0);
        double totalPaid = participant.calculatePaid();
        assertEquals(100.0, totalPaid);
    }

    @Test
    void testAddOwedAmountForSpecificEventNegative() {
        Event testEvent = new Event();
        assertThrows(IllegalArgumentException.class,
                () -> participant.addOwedAmountForSpecificEvent(testEvent, -10.0));
    }

    @Test
    void testAddPaidAmountForSpecificEventNegative() {
        Event testEvent = new Event();
        assertThrows(IllegalArgumentException.class,
                () -> participant.addPaidAmountForSpecificEvent(testEvent, -10.0));
    }

    @Test
    void testSetFirstName() {
        participant.setFirstName("Jane");
        assertEquals("Jane", participant.getFirstName());
    }

    @Test
    void testSetLastName() {
        participant.setLastName("Bobster");
        assertEquals("Bobster", participant.getLastName());
    }

    @Test
    void testSetId() {
        long expectedId = 123;
        participant.setId(expectedId);
        assertEquals(expectedId, participant.getId());
    }
}