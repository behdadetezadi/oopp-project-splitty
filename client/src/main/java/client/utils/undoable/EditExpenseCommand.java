package client.utils.undoable;


import client.utils.AlertUtils;
import client.utils.ServerUtils;
import commons.Expense;
import javafx.application.Platform;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EditExpenseCommand implements UndoableCommand {
    private Expense originalExpense;
    private Expense editedExpense;
    private long eventId;
    private Consumer<Boolean> onCompleteCallback;
    private ResourceBundle resourceBundle;

    /**
     * Constructor for EditExpenseCommand
     * @param originalExpense The original Expense
     * @param editedExpense The edited expense
     * @param eventId ID of the event
     * @param onCompleteCallback Completion callback
     * @param resourceBundle Local resource bundle
     */
    public EditExpenseCommand(Expense originalExpense, Expense editedExpense, long eventId,
                              Consumer<Boolean> onCompleteCallback,
                              ResourceBundle resourceBundle) {
        this.originalExpense = originalExpense;
        this.editedExpense = editedExpense;
        this.eventId = eventId;
        this.onCompleteCallback = onCompleteCallback;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Handles updating the expense
     */
    @Override
    public void execute() {
        try {
            ServerUtils.updateExpense(originalExpense.getId(),editedExpense,eventId);
            onCompleteCallback.accept(true);
        } catch (Exception e) {
            e.printStackTrace();
            onCompleteCallback.accept(false);
        }
    }

    /**
     * Undoes updating the expense
     */
    @Override
    public void undo() {
        try {
            ServerUtils.updateExpense(originalExpense.getId(),originalExpense,eventId);
            Platform.runLater(() -> {
                AlertUtils.showSuccessAlert(resourceBundle.getString("undoSuccessTitle"),
                        null, resourceBundle.getString("undoSuccessTitle"));});
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("undoFailedTitle"),
                    null, resourceBundle.getString("undoExpenseFailure")));
        }
    }
}

