package client.scenes;


import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ExpenseOverviewControllerTest {
    @Test
    void tagLanguageSwitch() {
        ExpenseOverviewController eoc = new ExpenseOverviewController(null, null, null, null);
        String expenseType = "testType";
        assertEquals(expenseType, eoc.tagLanguageSwitch(expenseType));


    }
}