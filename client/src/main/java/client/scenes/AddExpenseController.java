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

import java.util.Locale;
import java.util.ResourceBundle;


public class AddExpenseController {
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    @FXML
    private TextField payer;
    @FXML
    private Label expenseFor;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private TextField expenseDescription;
    @FXML
    private TextField amountPaid;
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
    public void setEvent(Event event, long participantId, Locale locale) {
        this.activeLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle("message", locale);
        updateUIElements();
        this.event = event;
        this.selectedParticipantId = participantId;
        participantLabel.setText(ServerUtils.getParticipant(selectedParticipantId).getFirstName()
                + " " + ServerUtils.getParticipant(selectedParticipantId).getLastName());
        initialize();
    }
    public void updateUIElements() {
        expenseFor.setText(resourceBundle.getString("Add_Expense_for"));
        participantLabel.setText(resourceBundle.getString("participant"));
        expenseDescription.setPromptText(resourceBundle.getString("Expense"));
        amountPaid.setPromptText(resourceBundle.getString("Amount_paid"));
        cancelButton.setText(resourceBundle.getString("Cancel"));
        addExpenseButton.setText(resourceBundle.getString("Add_expense"));
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
            addExpenseButton.getStyleClass().add("button-hover");
            cancelButton.getStyleClass().add("button-hover");

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
                    resourceBundle.getString("Please_enter_a_valid_number_for_the_amount."));
            return;
        }

        try {
            amountValue = Double.parseDouble(normalizedAmount);
        } catch (NumberFormatException e) {
            // Handle invalid number format
            AlertUtils.showErrorAlert("Invalid amount", "Error",
                    resourceBundle.getString("Please_enter_a_valid_number_for_the_amount."));
            return;
        }

        try {
            Expense newExpense = ServerUtils.addExpense(selectedParticipantId, description, amountValue, event.getId());
            Stage stage = (Stage) addExpenseButton.getScene().getWindow(); // Get the current stage
            if(newExpense!=null){
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, stage,
                        "Expense Added", resourceBundle.getString("The_expense_has_been_successfully_added."));
            }
            switchToEventOverviewScene();
        } catch (Exception e) {
            AlertUtils.showErrorAlert("Unexpected Error", "Error",
                    resourceBundle.getString("An_unexpected_error_occurred") + e.getMessage());
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
            alert.setContentText(resourceBundle.getString("Are_you_sure_you_want_to_cancel?"));
            if (alert.showAndWait().get() == ButtonType.OK) {
                // Switch to the EventOverview scene using MainController
                mainController.showEventOverview(this.event, activeLocale); // You may need to pass the event object if required
            }
        } else {
            throw new IllegalStateException();
        }
    }
    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event, activeLocale);

    }

    /**
     * clears the text field when you go to this page
     */
    public void clearTextFields() {
        expenseDescription.clear();
        amountPaid.clear();
    }

}
