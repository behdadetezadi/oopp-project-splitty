package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    Participant participant1 = new Participant("Lewis", "Hamilton");
    Participant participant2 = new Participant("Max", "Verstappen");


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
    void testAmountOfMoney() {
        Debt debt = new Debt(participant1, participant2, 50.0, true, "Expense");

        debt.setAmountOfMoney(60.0);
        assertEquals(60.0, debt.getAmountOfMoney());
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

}
