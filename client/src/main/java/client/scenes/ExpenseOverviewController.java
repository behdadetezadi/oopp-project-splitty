package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExpenseOverviewController {
    @FXML
    private ListView<String> expensesListView; // Assuming this ListView is defined in your FXML
    @FXML
    private Label sumOfExpensesLabel;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;
    private ResourceBundle resourceBundle;
    private Locale activeLocale;

    @Inject
    public ExpenseOverviewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    /**
     * Set the event and initialize expenses
     * @param
     */
    public void setEvent(Event event, Locale locale) {
        activeLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle("message", locale);
        this.event = event;
        initializeExpensesForEvent();
    }

    /**
     * Format the expense information for display
     *
     * @param expense the expense need to be displayed
     */
    public String formatExpenseForDisplay(Expense expense) {
        return String.format("%s - %s: $%.2f",
                expense.getParticipant().getFirstName(),
                expense.getCategory(),
                expense.getAmount());
    }

    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(event, activeLocale);

    }


//    public void initializeExpensesForEvent() {
//        if (this.event != null) {
//            try {
//                List<Expense> expenses = server.getExpensesForEvent(this.event.getId());
//                expensesListView.getItems().clear(); // Clear existing items
//                double sumOfExpenses = 0;
//                for (Expense expense : expenses) {
//                    String expenseDisplay = formatExpenseForDisplay(expense);
//                    expensesListView.getItems().add(expenseDisplay);
//                    sumOfExpenses += expense.getAmount();
//                }
//                sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
//            } catch (RuntimeException ex) {
//                // Handle case where no expenses are found
//                expensesListView.getItems().clear();
//                expensesListView.getItems().add("No expenses recorded yet.");
//                sumOfExpensesLabel.setText("Total: $0.00");
//            }
//        }
//    }
public void initializeExpensesForEvent() {
    if (this.event != null) {
        List<Expense> expenses = server.getExpensesForEvent(this.event.getId());
        expensesListView.getItems().clear(); // Clear existing items
        if (expenses.isEmpty()) {
            expensesListView.getItems().add("No expenses recorded yet.");
            sumOfExpensesLabel.setText("Total: $0.00");
        }
    }
}


    public void refreshExpensesList(Event event)
    {
        List<Expense> expenses = server.getExpensesForEvent(event.getId());
        expensesListView.getItems().clear(); // Clear existing items
        double sumOfExpenses = 0;
        for (Expense expense : expenses) {
            if (expense != null) {
                String expenseDisplay = formatExpenseForDisplay(expense);
                expensesListView.getItems().add(expenseDisplay);
                sumOfExpenses += expense.getAmount();
            }
        }
        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
    }

    public ListView<String> getExpensesListView() {
        return expensesListView;
    }

    public Label getSumOfExpensesLabel() {
        return sumOfExpensesLabel;
    }

    public void setExpensesListView(ListView<String> expensesListView) {
        this.expensesListView = expensesListView;
    }

    public void setSumOfExpensesLabel(Label sumOfExpensesLabel) {
        this.sumOfExpensesLabel = sumOfExpensesLabel;
    }
}






