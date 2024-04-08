package client.utils.undoable;


import client.utils.AlertUtils;
import client.utils.ServerUtils;
import commons.Expense;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EditExpenseCommand implements UndoableCommand {
    private Expense originalExpense;
    private Expense editedExpense;
    private long eventId;
    private Consumer<Boolean> onCompleteCallback;
    private ResourceBundle resourceBundle;

    public EditExpenseCommand(Expense originalExpense, Expense editedExpense, long eventId,
                              Consumer<Boolean> onCompleteCallback,
                              ResourceBundle resourceBundle) {
        this.originalExpense = originalExpense;
        this.editedExpense = editedExpense;
        this.eventId = eventId;
        this.onCompleteCallback = onCompleteCallback;
        this.resourceBundle = resourceBundle;
    }

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


    @Override
    public void undo() {
        try {
            ServerUtils.updateExpense(originalExpense.getId(),originalExpense,eventId);
            Platform.runLater(() -> {
                String successMessageKey ="undoSuccessTitle";
                String successMessage = resourceBundle.getString(successMessageKey);
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, successMessage, null, successMessage);
            });
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            Platform.runLater(() -> AlertUtils.showErrorAlert(resourceBundle.getString("undoFailedTitle"), null, resourceBundle.getString("undoExpenseFailure")));
        }
    }
}

