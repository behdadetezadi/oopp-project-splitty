package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    Participant participant = new Participant("John", "Foo");
    Participant participant2 = new Participant("John2", "Foo");
    List<Participant> spiltOption = new ArrayList<>();
    List<Participant> spiltOption2 = new ArrayList<>();
    Expense a = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", spiltOption, "food");
    Expense b = new Expense(participant, "dinner", 12, "Euros", "01-01-2024", spiltOption, "food");
    Expense c = new Expense(participant, "dinner", 13, "Euros", "01-01-2024", spiltOption, "food");


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
}