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


public class AddExpenseController {
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
    private Label participantLabel;
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
    public AddExpenseController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    /**
     * default constructor that JavaFX can use to instantiate the controller.
     */
    public AddExpenseController() {
        // Default constructor
    }

    /**
     * called by mainController
     * @param event event
     */
    public void setEvent(Event event, long participantId) {
        this.event = event;
        this.selectedParticipantId = participantId;
        participantLabel.setText(ServerUtils.getParticipant(selectedParticipantId).getFirstName()
                + " " + ServerUtils.getParticipant(selectedParticipantId).getLastName());
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
     * @param actionEvent button press
     */
    @FXML
    private void handleAddExpenseAction(ActionEvent actionEvent) {
        String description = this.expenseDescription.getText();
        String amount = this.amountPaid.getText();
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

        try {
            Expense newExpense = ServerUtils.addExpense(selectedParticipantId, description, amountValue, event.getId());
            Stage stage = (Stage) addExpenseButton.getScene().getWindow(); // Get the current stage
            if(newExpense!=null){
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, stage,
                        "Expense Added", "The expense has been successfully added.");
                mainController.refreshExpensesOverview(event);
            }
            switchToEventOverviewScene();
        } catch (Exception e) {
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
