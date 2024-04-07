package client.utils.undoable;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import commons.Expense;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class DeleteExpenseCommand implements UndoableCommand {
    private Expense expense;
    private long eventId;
    private Consumer<Expense> updateUI;
    private ResourceBundle resourceBundle;
    private Expense deletedExpense;

    public DeleteExpenseCommand(Expense expense, long eventId, Consumer<Expense> updateUI, ResourceBundle resourceBundle) {
        this.expense = expense;
        this.eventId = eventId;
        this.updateUI = updateUI;
        this.resourceBundle = resourceBundle;
    }


    @Override
    public void execute() {
        Platform.runLater(() -> {
            try {
                // Attempt to delete the expense
                Expense deletedExpense1 = ServerUtils.deleteExpense(expense.getId(), eventId);
                // Assuming deleteExpense returns null on failure instead of throwing an exception
                if (deletedExpense1 != null)
                {
                    // If deletion was successful, inform the UI
                    this.deletedExpense=deletedExpense1;
                    updateUI.accept(deletedExpense);
                }
                else {
                    // If deletion was unsuccessful, inform the UI of failure in a specific way
                    // This assumes updateUI is designed to handle a specific error signal; adjust as necessary
                    System.err.println("Failed to delete expense, server returned null");
                    Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("Unexpected_Error"), resourceBundle.getString("error"), resourceBundle.getString("Failed_to_delete_expense")));
                }
            } catch (RuntimeException e) {
                // Log the exception and show an error alert with more detail
                e.printStackTrace();
                Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("Unexpected_Error"), resourceBundle.getString("error"), resourceBundle.getString("An_unexpected_error_occurred") + ": " + e.getMessage()));
            }
        });
    }




    @Override
    public void undo() {
        Platform.runLater(() -> {
            try {
                Expense addedExpense = ServerUtils.addExpense(expense.getParticipant().getId(), expense.getCategory(), expense.getAmount(), eventId);
                if (addedExpense != null) {
                    // If adding the expense back was successful, inform the UI
                    updateUI.accept(addedExpense);
                Platform.runLater(() -> {
                    // Show success alert
                    AlertUtils.showSuccessAlert(resourceBundle.getString("undoSuccessTitle"), null, resourceBundle.getString("undoExpenseSuccess"));
                });}
            } catch (Exception e) {
                Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("undoFailedTitle"), null, resourceBundle.getString("undoExpenseFailure")));
            }
        });
    }
}
