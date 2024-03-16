package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import client.utils.ServerUtils;
import client.utils.ValidationUtils;
import client.utils.AlertUtils;

import commons.Expense;


public class ExpenseController {
    private ServerUtils server;
    private MainController mainController;
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

    /**
     * Expense controller
     * @param server ServerUtils type
     * @param mainController MainCtrl type
     */
    @Inject
    public ExpenseController(ServerUtils server, MainController mainController) {
        this.server = server;
        this.mainController = mainController;
    }

    /**
     * Initializer method
     */
    @FXML
    public void initialize() {
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
            // TODO show success message, navigate to previous scene etc
        } catch (Exception e) {
            // Catch exception
            AlertUtils.showErrorAlert("Unexpected Error",
                    "An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Cancel button handler
     * @param event button press
     */
    @FXML
    public void handleCancelAction(ActionEvent event) {
        // TODO navigate to previous scene
    }
}
