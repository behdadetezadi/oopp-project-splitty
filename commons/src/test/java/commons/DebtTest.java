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


    @Test
    void testSetAmountOfMoneyPositive() {
        Debt debt = new Debt();
        debt.setAmountOfMoney(100.0);
        assertEquals(100.0, debt.getAmountOfMoney(),
                "The amount of money should be set to 100.0.");
    }

    @Test
    void testSetDebtor() {
        Debt debt = new Debt();
        debt.setDebtor(participant1);
        assertEquals(participant1, debt.getDebtor(),
                "The debtor should be set.");
    }

    @Test
    void testSetLender() {
        Debt debt = new Debt();
        debt.setLender(participant2);
        assertEquals(participant2, debt.getLender(),
                "The lender should be set.");
    }

    @Test
    void testSetDebtCollective() {
        Debt debt = new Debt();
        debt.setDebtCollective(true);
        assertTrue(debt.isDebtCollective(),
                "The debt should be collective.");
    }

    @Test
    void testSetDescription() {
        Debt debt = new Debt();
        String description = "Lunch";
        debt.setDescription(description);
        assertEquals(description, debt.getDescription(),
                "The description needs to be set.");
    }

    @Test
    void testEmptyConstructorAndSetters() {
        Debt debt = new Debt();
        debt.setDebtor(participant1);
        debt.setLender(participant2);
        debt.setAmountOfMoney(100.0);
        debt.setDebtCollective(true);
        debt.setDescription("Coffee");

        assertEquals(participant1, debt.getDebtor());
        assertEquals(participant2, debt.getLender());
        assertEquals(100.0, debt.getAmountOfMoney());
        assertTrue(debt.isDebtCollective());
        assertEquals("Coffee", debt.getDescription());
    }

    @Test
    void testToStringNoDescription() {
        Debt debt = new Debt(participant1, participant2, 20.0, false, "");
        String expectedToStringStart = "Debt Details:";
        assertTrue(debt.toString().startsWith(expectedToStringStart));
        assertTrue(debt.toString().contains("No description provided"));
    }

    @Test
    void testSetAmountOfMoneyEdgeCase() {
        Debt debt = new Debt();
        assertThrows(IllegalArgumentException.class, () ->
                debt.setAmountOfMoney(-1),
                "Setting a negative amount of money should throw IllegalArgumentException().");
    }
}
