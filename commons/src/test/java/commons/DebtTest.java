package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    Participant participant1 = new Participant("Lewis", "Hamilton");
    Participant participant2 = new Participant("Max", "Verstappen");

    @Test
    void testFullConstructor() {
        Debt debt = new Debt(participant1, participant2, 100.0, false, "Loan");
        assertEquals(participant1, debt.getDebtor());
        assertEquals(participant2, debt.getLender());
        assertEquals(100.0, debt.getAmountOfMoney());
        assertFalse(debt.isDebtCollective());
        assertEquals("Loan", debt.getDescription());
    }

    @Test
    void testPartialConstructor() {
        Debt debt = new Debt(participant1, participant2, 100.0);
        assertEquals(participant1, debt.getDebtor());
        assertEquals(participant2, debt.getLender());
        assertEquals(100.0, debt.getAmountOfMoney());
        assertFalse(debt.isDebtCollective());
        assertEquals("", debt.getDescription());
    }

    @Test
    void testJacksonDebtConstructor() {

        Debt debt = new Debt(1L, participant1, participant2, 100.0, true, "Test Debt Description");
        assertEquals(1L, debt.getId());
        assertEquals(participant1, debt.getDebtor());
        assertEquals(participant2, debt.getLender());
        assertEquals(100.0, debt.getAmountOfMoney());
        assertTrue(debt.isDebtCollective());
        assertEquals("Test Debt Description", debt.getDescription());
    }

    @Test
    void testEquals() {
        Debt debt1 = new Debt(participant1, participant2, 50.0, true, "Expense");
        Debt debt2 = new Debt(participant1, participant2, 50.0, true, "Expense");
        assertEquals(debt1, debt2);
    }

    @Test
    void testNotEquals() {
        Debt debt1 = new Debt(participant1, participant2, 50.0, true, "Expense");
        Debt debt2 = new Debt(participant1, participant2, 50.1, true, "Expense");
        assertNotEquals(debt1, debt2);
    }
    @Test
    void testHashCode() {
        Debt debt1 = new Debt(participant1, participant2, 50.0, true, "Expense");
        Debt debt2 = new Debt(participant1, participant2, 50.0, true, "Expense");
        assertEquals(debt1.hashCode(), debt2.hashCode());
    }

    @Test
    void testSetAmountOfMoneyWithNegativeValue() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> debt.setAmountOfMoney(-10.0));
        assertEquals("Amount of money can't be negative!", exception.getMessage());
    }


    @Test
    void testDebtCollective() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");

        debt.setDebtCollective(false);
        assertFalse(debt.isDebtCollective());
    }

    @Test
    void testDescription() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");

        debt.setDescription("Dinner");
        assertEquals("Dinner", debt.getDescription());
    }

    @Test
    void testDebtor() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");

        Participant debtor = new Participant("Charles", "Leclerc");
        debt.setDebtor(debtor);
        assertEquals(debtor, debt.getDebtor());
    }

    @Test
    void testLender() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");

        Participant lender = new Participant("Lando", "Norris");
        debt.setLender(lender);
        assertEquals(lender, debt.getLender());
    }

    @Test
    void testToString() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");
        String expected = "Debt Details:\n" +
                "  Debtor: " + "Lewis"+ "\n" +
                "  Creditor: " + "Max" + "\n" +
                "  Amount: $50.0\n" +
                "  Debt Type: Collective\n" +
                "  Description: Expense";
        assertEquals(expected, debt.toString());
    }
}
