package commons;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Participant participant = new Participant("John", "Foo");
    Participant participant2 = new Participant("John2", "Foo");
    List<Participant> spiltOption = new ArrayList<>();
    List<Participant> spiltOption2 = new ArrayList<>();
    Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", spiltOption, "food",(long)2305);
    Expense b = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", spiltOption, "food",(long)2306);
    Expense c = new Expense(participant, "dinner", 13, "Euros", "01-01-2024", spiltOption, "food", (long)2307);


    @Test
    void testFullExpenseConstructor() {
        Participant participant = new Participant("John", "Foo");
        List<Participant> splittingOption = new ArrayList<>();
        splittingOption.add(participant);
        long eventId=1234;
        String category = "dinner";
        double amount = 12;
        String currency = "Euros";
        String date = "01-01-2024";
        String expenseType = "food";

        Expense expense = new Expense(participant, category, amount, currency,
                date, splittingOption, expenseType, eventId);
        assertEquals(participant, expense.getParticipant());
        assertEquals(category, expense.getCategory());
        assertEquals(amount, expense.getAmount());
        assertEquals(currency, expense.getCurrency());
        assertEquals(date, expense.getDate());
        assertEquals(splittingOption, expense.getSplittingOption());
        assertEquals(expenseType, expense.getExpenseType());
        assertEquals(eventId, expense.getEventId());
    }

    @Test
    void testJacksonExpenseConstructor() {
        List<Participant> splittingOption = new ArrayList<>();
        splittingOption.add(new Participant("Jane", "Smith"));
        long eventIds = 3456;

        Expense expense = new Expense(1L, participant, "Food", 50.0, "USD", "2024-03-19", splittingOption, "Personal", eventIds);

        assertEquals(1L, expense.getId());
        assertEquals(participant, expense.getParticipant());
        assertEquals("Food", expense.getCategory());
        assertEquals(50.0, expense.getAmount(), 0.001);
        assertEquals("USD", expense.getCurrency());
        assertEquals("2024-03-19", expense.getDate());
        assertEquals(splittingOption, expense.getSplittingOption());
        assertEquals("Personal", expense.getExpenseType());
        assertEquals(eventIds, expense.getEventId());
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
    void testDefaultConstructor() {
        Expense expense = new Expense();
        assertNull(expense.getParticipant());
        assertNull(expense.getCategory());
        assertEquals(0.0, expense.getAmount(), 0.001);
        assertNull(expense.getCurrency());
        assertNull(expense.getDate());
        assertNull(expense.getSplittingOption());
        assertNull(expense.getExpenseType());
        assertNull(expense.getEventId());
    }

    @Test
    void testEqualityWithDifferentEventId() {
        Expense expense1 = new Expense(participant, "Lunch", 20,
                "USD", "02-02-2024", new ArrayList<>(), "Food", 1L);
        Expense expense2 = new Expense(participant, "Lunch", 20,
                "USD", "02-02-2024", new ArrayList<>(), "Food", 2L);
        assertNotEquals(expense1, expense2);
    }

    @Test
    void testEqualityWithNullFields() {
        Expense expense1 = new Expense();
        Expense expense2 = new Expense();
        assertEquals(expense1, expense2);
    }

    @Test
    void testHashCodeConsistency() {
        Expense expense1 = new Expense(participant, "Coffee", 5, "EUR",
                "03-03-2024", new ArrayList<>(), "Beverage", 10L);
        Expense expense2 = new Expense(participant, "Coffee", 5, "EUR",
                "03-03-2024", new ArrayList<>(), "Beverage", 10L);
        assertTrue(expense1.equals(expense2) && expense2.equals(expense1));
        assertEquals(expense1.hashCode(), expense2.hashCode());
    }

    @Test
    void testSetAndGetEventId() {
        Expense expense = new Expense();
        Long eventId = 9999L;
        expense.setEventId(eventId);
        assertEquals(eventId, expense.getEventId());
    }

    @Test
    void testEqualityWithDifferentAmounts() {
        Expense expense1 = new Expense(participant, "Lunch", 15.0, "USD",
                "02-02-2024", new ArrayList<>(), "Food", 1L);
        Expense expense2 = new Expense(participant, "Lunch", 20.0, "USD",
                "02-02-2024", new ArrayList<>(), "Food", 1L);
        assertNotEquals(expense1, expense2);
    }

    @Test
    void testHashCodeVariance() {
        Expense expense1 = new Expense(participant, "Lunch", 20.0, "USD",
                "02-02-2024", new ArrayList<>(), "Food", 1L);
        Expense expense2 = new Expense(participant2, "Dinner", 25.0, "EUR",
                "03-03-2024", new ArrayList<>(), "Entertainment", 2L);
        assertNotEquals(expense1.hashCode(), expense2.hashCode());
    }

    @Test
    void testConstructionWithNullValues() {
        assertDoesNotThrow(() -> new Expense(null, null, 0.0,
                null, null, null, null, null));
    }

    @Test
    void testEqualityWithSelfAndNull() {
        Expense expense = new Expense(participant, "Coffee", 3.0, "EUR",
                "05-05-2024", new ArrayList<>(), "Beverage", 4L);
        assertEquals(expense, expense);
        assertNotEquals(expense, null);
    }

    @Test
    void testEmptySplittingOptionBehavior() {
        Expense expense = new Expense(participant, "Tea", 2.5, "EUR",
                "06-06-2024", Collections.emptyList(), "Beverage", 5L);
        assertTrue(expense.getSplittingOption().isEmpty());
    }

    @Test
    void testConstructorWithId() {
        long expectedId = 5L;
        Participant expectedParticipant = new Participant("TestName", "TestSurname");
        String expectedCategory = "TestCategory";
        double expectedAmount = 100.0;
        String expectedCurrency = "USD";
        String expectedDate = "2022-10-10";
        List<Participant> expectedSplitOption = new ArrayList<>();
        String expectedExpenseType = "TestType";
        long expectedEventId = 10L;

        Expense expenseWithId = new Expense(expectedId, expectedParticipant, expectedCategory, expectedAmount,
                expectedCurrency, expectedDate, expectedSplitOption,
                expectedExpenseType, expectedEventId);

        assertEquals(expectedId, expenseWithId.getId());
        assertEquals(expectedEventId, expenseWithId.getEventId());
    }

    @Test
    void setIdTest() {
        Expense e = new Expense();
        e.setId(12);
        assertEquals(12, e.getId());
    }

    @Test
    void toStringTest() {
        Expense e = new Expense();

        String expected = """ 
                [
                  amount=0.0
                  category=<null>
                  currency=<null>
                  date=<null>
                  eventId=<null>
                  expenseType=<null>
                  id=0
                  participant=<null>
                  splittingOption=<null>
                ]""";
        assertEquals(expected.replace("\n", "\r\n"), e.toString().substring(24));
    }



}