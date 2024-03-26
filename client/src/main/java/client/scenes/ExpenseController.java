package client.scenes;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.ValidationUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.List;



public class ExpenseController {
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private TextField payer;
    @FXML
    private TextField expenseDescription;
    @FXML
    private TextField amountPaid;
    // Add a ListView for displaying participant expenses
    @FXML
    private ListView<String> expensesListView; // Assume this ListView is defined in your FXML
    @FXML
    private Label sumOfExpensesLabel;

    private Event event;


    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController mainController
     * @param event Event
     */
    @Inject
    public ExpenseController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    /**
     * default constructor that JavaFX can use to instantiate the controller.
     */
    public ExpenseController() {
        // Default constructor
    }

    /**
     * called by mainController
     * @param event event
     */
    public void setEvent(Event event) {
        this.event = event;
        initialize();
    }

    /**
     *
     *
     */
    @FXML
    public void initialize() {
        if(event != null) {
            cancelButton.setOnAction(this::handleCancelAction);
            addExpenseButton.setOnAction(this::handleAddExpenseAction);
            amountPaid.addEventFilter(KeyEvent.KEY_TYPED, this::validateAmountInput);
        }
    }

    /**
     * Integrates the viewing of expenses for a selected participant.
     *
     * @param participantId The ID of the participant whose expenses you want to view.
     */
    public void initializeExpensesForParticipant(Long participantId) {
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
        expensesListView.getItems().clear();
        double sumOfExpenses = 0;
        for (Expense expense : expenses) {
            expensesListView.getItems().add(expense.toString());
            sumOfExpenses += expense.getAmount(); 
        }
        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
    }


    /**
     * This method validates input for the amount
     * paid field to make sure it's a valid integer/double
     * @param event keyboard input event
     */
    @FXML
    private void validateAmountInput(KeyEvent event) {
        ValidationUtils.validateAmountInput(event, amountPaid.getText());
    }

    /**
     * Add expense button handler
     * @param event button press
     */
    @FXML
    private void handleAddExpenseAction(ActionEvent event) {
        String payer = this.payer.getText();
        String description = this.expenseDescription.getText();
        String amount = this.amountPaid.getText();

        // Convert amount to a number and handle potential exceptions
        double amountValue;

        // Check for a trailing period/comma
        String normalizedAmount = amount.replace(',', '.');
        if (normalizedAmount.endsWith(".")) {
            AlertUtils.showErrorAlert("Invalid amount", "Error",
                    "Please enter a valid number for the amount.");
            return;
        }

        try {
            amountValue = Double.parseDouble(normalizedAmount);
        } catch (NumberFormatException e) {
            // Handle invalid number format
            AlertUtils.showErrorAlert("Invalid amount", "Error",
                    "Please enter a valid number for the amount.");
            return;
        }

        // Create new expense in backend
        try {
            Expense newExpense = ServerUtils.addExpense(payer, description, amountValue);
            Stage stage = (Stage) addExpenseButton.getScene().getWindow(); // Get the current stage
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, stage,
                    "Expense Added", "The expense has been successfully added.");
            switchToEventOverviewScene();

            // TODO show success message, navigate to previous scene etc
        } catch (Exception e) {
            // Catch exception
            AlertUtils.showErrorAlert("Unexpected Error", "Error",
                    "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Cancel button handler
     * If you press the button, it will ask you for confirmation
     * and switch back to the EventOverview
     * @param event button press
     */
    @FXML
    public void handleCancelAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel add expense");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to cancel?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                // Switch to the EventOverview scene using MainController
                mainController.showEventOverview(this.event); // You may need to pass the event object if required
            }
        } else {
            throw new IllegalStateException();
        }
    }
    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event);

    }

}
