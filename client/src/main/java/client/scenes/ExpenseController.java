package client.scenes;

import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import client.utils.ServerUtils;
import client.utils.ValidationUtils;
import client.utils.AlertUtils;

import commons.Expense;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;


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

    private Event event;



    /**
     * Expense controller
     * @param server ServerUtils type
     * @param mainController MainCtrl type
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
     * Initializer method
     */
    @FXML
    public void initialize(Event event) {
        this.event = event;
        cancelButton.setOnAction(this::handleCancelAction);
        addExpenseButton.setOnAction(this::handleAddExpenseAction);
        amountPaid.addEventFilter(KeyEvent.KEY_TYPED, this::validateAmountInput);
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
            AlertUtils.showErrorAlert("Invalid amount",
                    "Please enter a valid number for the amount.");
            return;
        }

        try {
            amountValue = Double.parseDouble(normalizedAmount);
        } catch (NumberFormatException e) {
            // Handle invalid number format
            AlertUtils.showErrorAlert("Invalid amount",
                    "Please enter a valid number for the amount.");
            return;
        }

        // Create new expense in backend
        try {
            Expense newExpense = ServerUtils.addExpense(payer, description, amountValue);
            Stage stage = (Stage) addExpenseButton.getScene().getWindow(); // Get the current stage
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, stage, "Expense Added", "The expense has been successfully added.");
            switchToEventOverviewScene();

            // TODO show success message, navigate to previous scene etc
        } catch (Exception e) {
            // Catch exception
            AlertUtils.showErrorAlert("Unexpected Error",
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




    private void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event);

    }

}
