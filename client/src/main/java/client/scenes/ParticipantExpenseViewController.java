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



public class ParticipantExpenseViewController
{
    @FXML
    private ListView<String> expensesListView; // Assume this ListView is defined in your FXML
    @FXML
    private Label sumOfExpensesLabel;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;
    private long selectedParticipantId;
    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController mainController
     * @param event Event
     */
    @Inject
    public ParticipantExpenseViewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    public void setEvent(Event event, long participantId) {
        this.event = event;
        this.selectedParticipantId = participantId;}
    /** Format the expense information for display
     * @param expense the expense need to be displayed
     */
    private String formatExpenseForDisplay(Expense expense)
    {
        return String.format("%s: %.2f",
                expense.getCategory(),
                expense.getAmount());
    }
    /**
     * Integrates the viewing of expenses for a selected participant.
     *
     * @param participantId The ID of the participant whose expenses you want to view.
     */
//    public void initializeExpensesForParticipant(Long participantId) {
//        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
//        expensesListView.getItems().clear();
//        double sumOfExpenses = 0;
//        for (Expense expense : expenses) {
//            expensesListView.getItems().add(formatExpenseForDisplay(expense));
//            sumOfExpenses += expense.getAmount();
//        }
//        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
//    }
    public void initializeExpensesForParticipant(Long participantId) {
        System.out.println("Initializing expenses for participant ID: " + participantId);
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
        System.out.println("Number of expenses fetched: " + expenses.size());

        expensesListView.getItems().clear();
        double sumOfExpenses = 0;
        for (Expense expense : expenses) {
            String expenseDisplay = formatExpenseForDisplay(expense);
            System.out.println("Adding expense to ListView: " + expenseDisplay);
            expensesListView.getItems().add(expenseDisplay);
            sumOfExpenses += expense.getAmount();
        }
        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
    }


    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(event);

    }
}
