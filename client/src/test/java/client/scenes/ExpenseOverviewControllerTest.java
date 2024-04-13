package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseOverviewControllerTest {
    @Test
    void tagLanguageSwitch() {
        ExpenseOverviewController eoc = new ExpenseOverviewController(null, null, null, null);
        String expenseType = "testType";
        assertEquals(expenseType, eoc.tagLanguageSwitch(expenseType));


    }

//    @Test
//    void testFormatExpenseForDisplay() {
//        // Setup
//        ServerUtils serverMock = mock(ServerUtils.class);
//        MainController mainControllerMock = mock(MainController.class);
//        Stage stageMock = mock(Stage.class);
//        Event eventMock = mock(Event.class);
//
//        ExpenseOverviewController controller = new ExpenseOverviewController(stageMock, serverMock, mainControllerMock, eventMock);
//
//        Expense expense = new Expense();
//        expense.setParticipant(new Participant("John", "Doe")); // Assuming Participant has a constructor like this.
//        expense.setCategory("Food");
//        expense.setAmount(15.50);
//
//        // Action
//        String result = controller.formatExpenseForDisplay(expense);
//
//        // Assertion
//        assertEquals("John - Food: $15.50", result);
//    }
//    @Test
//    void testRefreshExpensesListWithExpenses() {
//        // Mock dependencies
//        ServerUtils serverMock = mock(ServerUtils.class);
//        Event event = new Event(); // Similar setup as before
//        event.setId(2);
//
//        // Assuming Expense has a suitable constructor or setters for this setup
//        List<Expense> expenses = Arrays.asList(
//                new Expense(new Participant("Jodie","Zhao"), "Food", 10.00),
//                new Expense(new Participant("Jodie","Zhao"), "Transport", 15.00)
//        );
//
//        when(serverMock.getExpensesForEvent(anyLong())).thenReturn(expenses);
//
//        // Initialize controller
//        ExpenseOverviewController controller = new ExpenseOverviewController(null, serverMock, null, event);
//        controller.setExpensesListView(new ListView<>());
//        controller.setSumOfExpensesLabel(new Label());
//
//        // Action
//        controller.refreshExpensesList(event);
//
//        // Assertions
//        assertEquals(2, controller.getExpensesListView().getItems().size()); // Expecting two items in the list
////        assertTrue(controller.getExpensesListView().getItems().containsAll(Arrays.asList("John - Food: $10.00", "Jane - Transport: $15.00")));
////        assertEquals("Total: $25.00", controller.getSumOfExpensesLabel().getText());
//    }

}

