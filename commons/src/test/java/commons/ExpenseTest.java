package commons;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Participant participant = new Participant("John", "Foo");
    Participant participant2 = new Participant("John2", "Foo");
    List<Participant> spiltOption = new ArrayList<>();
    List<Participant> spiltOption2 = new ArrayList<>();
    Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", spiltOption, "food", new HashSet<>());
    Expense b = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", spiltOption, "food", new HashSet<>());
    Expense c = new Expense(participant, "dinner", 13, "Euros", "01-01-2024", spiltOption, "food", new HashSet<>());


    @Test
    void testFullExpenseConstructor() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splittingOption = new ArrayList<>();
        splittingOption.add(participant);
        Set<Integer> eventIds = new HashSet<>();
        String category = "dinner";
        double amount = 12;
        String currency = "Euros";
        String date = "01-01-2024";
        String expenseType = "food";

        Expense expense = new Expense(participant, category, amount, currency,
                date, splittingOption, expenseType, eventIds);
        assertEquals(participant, expense.getParticipant());
        assertEquals(category, expense.getCategory());
        assertEquals(amount, expense.getAmount());
        assertEquals(currency, expense.getCurrency());
        assertEquals(date, expense.getDate());
        assertEquals(splittingOption, expense.getSplittingOption());
        assertEquals(expenseType, expense.getExpenseType());
        assertEquals(eventIds, expense.getEventIds());
    }

    @Test
    public void equalsTest(){
        spiltOption.add(participant);
        assertEquals(a, b);
    }

    @Test
    public void notEqualsTest(){
        spiltOption.add(participant);
        assertNotEquals(a, c);
    }

    @Test
    public void sameTest(){
        assertNotSame(a, b);
    }

    @Test
    public void hashcodeTest(){
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void getPersonTest(){
        assertEquals(participant, a.getParticipant());
    }

    @Test
    public void setPersonTest(){
        a.setParticipant(participant2);
        assertEquals(participant2, a.getParticipant());
    }

    @Test
    public void getSplittingOptionTest(){
        assertEquals(spiltOption, a.getSplittingOption());
    }

    @Test
    public void setSplittingOptionTest(){
        a.setSplittingOption(spiltOption2);
        assertEquals(spiltOption2, a.getSplittingOption());
    }

    @Test
    public void getCategoryTest(){
        assertEquals("dinner", a.getCategory());
    }

    @Test
    public void setCategoryTest(){
        a.setCategory("lunch");
        assertEquals("lunch", a.getCategory());
    }

    @Test
    public void getAmountTest(){
        assertEquals(12, a.getAmount());
    }

    @Test
    public void setAmountTest(){
        a.setAmount(20);
        assertEquals(20, a.getAmount());
    }

    @Test
    public void getCurrencyTest(){
        assertEquals("Euros", a.getCurrency());
    }

    @Test
    public void setCurrencyTest(){
        a.setCurrency("USD");
        assertEquals("USD", a.getCurrency());
    }

    @Test
    public void getDateTest(){
        assertEquals("01-01-2024", a.getDate());
    }

    @Test
    public void setDateTest(){
        a.setDate("01-02-2024");
        assertEquals("01-02-2024", a.getDate());
    }

    @Test
    public void getExpenseTypeTest(){
        assertEquals("food", a.getExpenseType());
    }

    @Test
    public void setExpenseTypeTest(){
        a.setExpenseType("drinks");
        assertEquals("drinks", a.getExpenseType());
    }

    @Test
    void testGetId() {
        assertEquals(0, participant.getId());
    }

    @Test
    void testSetId() {
        participant.setId(12345);
        assertEquals(12345, participant.getId());
    }

    @Test
    void testSetEventIds() {
        Set<Integer> eventIds = new HashSet<>();
        eventIds.add(1);
        eventIds.add(2);
        eventIds.add(3);
        a.setEventIds(eventIds);

        assertEquals(eventIds, a.getEventIds());
        assertTrue(a.getEventIds().contains(1));
        assertTrue(a.getEventIds().contains(2));
        assertTrue(a.getEventIds().contains(3));
    }
}