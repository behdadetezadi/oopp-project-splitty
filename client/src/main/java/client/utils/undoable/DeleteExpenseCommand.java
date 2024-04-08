package client.utils.undoable;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import commons.Expense;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DeleteExpenseCommand implements UndoableCommand {
    private Expense expense;
    private long eventId;
    private ResourceBundle resourceBundle;
    private Expense deletedExpense;
    private BiConsumer<Expense, String> updateUI;

    public DeleteExpenseCommand(Expense expense, long eventId, BiConsumer<Expense, String> updateUI, ResourceBundle resourceBundle) {
        this.expense = expense;
        this.eventId = eventId;
        this.updateUI = updateUI;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void execute() {
        Platform.runLater(() -> {
            try {
                Expense deletedExpense1 = ServerUtils.deleteExpense(expense.getId(), eventId);
                if (deletedExpense1 != null) {
                    this.deletedExpense = deletedExpense1;
                    updateUI.accept(deletedExpense, "deleted");
                } else {
                    System.err.println("Failed to delete expense, server returned null");
                    Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("Unexpected_Error"), resourceBundle.getString("error"), resourceBundle.getString("Failed_to_delete_expense")));
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("Unexpected_Error"), resourceBundle.getString("error"), resourceBundle.getString("An_unexpected_error_occurred") + ": " + e.getMessage()));
            }
        });
    }

    @Override
    public void undo() {
        Platform.runLater(() -> {
            try {
                Expense addedExpense = ServerUtils.addExpense(expense.getParticipant().getId(), expense.getCategory(), expense.getAmount(), eventId, expense.getExpenseType());
                if (addedExpense != null) {
                    updateUI.accept(addedExpense, "undone");
                }
            } catch (Exception e) {
                Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("undoFailedTitle"), null, resourceBundle.getString("undoExpenseFailure")));
            }
        });
    }
}
