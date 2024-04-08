package client.utils.undoable;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import commons.Expense;
import javafx.application.Platform;

import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Command for adding an expense to an event.
 */
/**
 * Command for adding an expense to an event.
 */
public class AddExpenseCommand implements UndoableCommand {
    private long eventId;
    private Expense addedExpense;
    private Consumer<Expense> updateUI;

    public Expense getAddedExpense() {
        return addedExpense;
    }

    private ResourceBundle resourceBundle;

    /**
     * Constructs an {@code AddExpenseCommand} with the necessary information to add an expense.
     *
     * @param newExpense the newly added expense
     * @param eventId The ID of the event to which the expense is being added.
     * @param updateUI A {@code Consumer<Expense>} that updates the UI based on the result of the add operation.
     * @param resourceBundle The resource bundle used for localizing messages displayed to the user.
     */
    public AddExpenseCommand(Expense newExpense, long eventId, Consumer<Expense> updateUI, ResourceBundle resourceBundle) {
        this.addedExpense=newExpense;
        this.eventId = eventId;
        this.updateUI = updateUI;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Executes the add expense operation. This involves sending a request to the server
     * to add a new expense and updating the UI based on the operation's success or failure.
     */
    @Override
    public void execute() {
        Platform.runLater(() -> {
            try {
                // Save the expense and receive the saved instance including the generated ID
                Expense savedExpense = ServerUtils.addExpense(
                        addedExpense.getParticipant().getId(),
                        addedExpense.getCategory(),
                        addedExpense.getAmount(),
                        eventId,
                        addedExpense.getExpenseType()
                );

                if (savedExpense != null && savedExpense.getId() != 0) {

                    this.addedExpense = savedExpense;
                    updateUI.accept(addedExpense);

                } else {
                    updateUI.accept(null);
                }
            } catch (Exception e) {
                updateUI.accept(null);
            }
        });
    }



    /**
     * Undoes the add expense operation by removing the previously added expense from the server,
     * and updates the UI to reflect this change.
     */
    @Override
    public void undo() {
        if (addedExpense != null && addedExpense.getId() != 0) {
            try {
                ServerUtils.deleteExpense(addedExpense.getId(), eventId);

                // Execute this on the JavaFX Application Thread
                Platform.runLater(() -> {
                    // Show success alert
                    AlertUtils.showSuccessAlert(resourceBundle.getString("undoSuccessTitle"), null, resourceBundle.getString("undoExpenseSuccess"));
                });

            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
                Platform.runLater(() ->
                {
                    // Show error alert if undo operation fails
                    AlertUtils.showErrorAlert(resourceBundle.getString("undoFailedTitle"), null, resourceBundle.getString("undoExpenseFailure"));
                });

            }
        }
    }
}


