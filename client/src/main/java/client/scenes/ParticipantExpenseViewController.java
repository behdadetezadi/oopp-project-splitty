package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.*;

import java.util.List;


public class ParticipantExpenseViewController
{
    @FXML
    private ListView<String> expensesListView;
    @FXML
    private Label sumOfExpensesLabel;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
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
    public ParticipantExpenseViewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    public void setEvent(Event event, long participantId) {
        this.event = event;
        this.selectedParticipantId = participantId;
    }

    /** Format the expense information for display
     * @param expense the expense need to be displayed
     */
    private String formatExpenseForDisplay(Expense expense)
    {
        return String.format("%s: %.2f",
                expense.getCategory(),
                expense.getAmount());
    }

    /**
     * Integrates the viewing of expenses for a selected participant.
     *
     * @param participantId The ID of the participant whose expenses you want to view.
     */
    public void initializeExpensesForParticipant(Long participantId) {
        System.out.println("Initializing expenses for participant ID: " + participantId);
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
        System.out.println("Number of expenses fetched: " + expenses.size());

        expensesListView.getItems().clear();
        double sumOfExpenses = 0;
        for (Expense expense : expenses) {
            String expenseDisplay = formatExpenseForDisplay(expense);
            expensesListView.getItems().add(expenseDisplay);
            sumOfExpenses += expense.getAmount();

        }
        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
        expensesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        }else {
                            setText(item);
                            Expense expense = getExpenseFromListView(getIndex(), participantId);
                            Button editButton = new Button("Edit");
                            Button deleteButton = new Button("Delete");
                            editButton.setOnAction(event -> handleEditButton(expense));
                            deleteButton.setOnAction(event -> handleDeleteButton(expense));
                            setGraphic(new HBox(editButton, deleteButton));
                        }
                    }
                };
            }
        });
    }

    private Expense getExpenseFromListView(int index, long participantId) {
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
        if (index >= 0 && index < expenses.size()) {
            return expenses.get(index);
        } else {
            return null;
        }
    }

    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(event);
    }

    /**
     * deletes current expense
     */
    @FXML
    private void handleDeleteButton(Expense expense) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to remove this expense? This action cannot be undone"
                , ButtonType.YES, ButtonType.NO);
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                ServerUtils.deleteExpense(expense.getId());
            }
        });
    }

    /**
     * edits the expense where the button is pressed
     */
    @FXML
    private void handleEditButton(Expense selectedExpense) {
        if (selectedExpense != null) {
            editExpense(selectedExpense, "Edit Expense", "Edit the details of the expense.",
                    this::updateExpense);
        }
    }

    /**
     * Displays a dialog for editing an expense's details and performs a specified action upon confirmation.
     * @param selectedExpense The {@link Expense} to edit.
     * @param title The title of the dialog window.
     * @param header The header text for the dialog.
     * @param action The action to perform with the edited participant.
     */
    private void editExpense(Expense selectedExpense, String title, String header, ExpenseConsumer action) {
        ExpenseDialog dialog = new ExpenseDialog(selectedExpense, title, header);
        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(action::accept);
    }

    /**
     * Updates the details of an existing expense in the event
     * @param expense The {@link Expense} whose details are to be updated.
     */
    private void updateExpense(Expense expense) {
        ServerUtils.updateExpense(expense.getId(), expense, event.getId());
        initializeExpensesForParticipant(selectedParticipantId);
        // TODO validate the input and throw errors if it goes wrong
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expense Saved");
        alert.setHeaderText(null);
        alert.setContentText("The expense has been successfully saved.");
        alert.showAndWait();
    }

    /**
     * A dialog for creating or editing an expense's details.
     */
    static class ExpenseDialog extends Dialog<Expense> {
        ExpenseDialog(Expense expense, String title, String header) {
            setTitle(title);
            setHeaderText(header);

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
            getDialogPane().setMinHeight(350);
            getDialogPane().setMinWidth(600);

            Pair<GridPane, Map<String, Control>> formPair = ExpenseForm.createExpenseForm(expense);
            GridPane grid = formPair.getKey();
            Map<String, Control> formFields = formPair.getValue();
            getDialogPane().setContent(grid);

            String cssPath = Objects.requireNonNull(this.getClass().getResource("/styles.css")).toExternalForm();
            getDialogPane().getStylesheets().add(cssPath);

            setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return ExpenseForm.extractExpenseFromForm(formFields, expense);
                }
                return null;
            });
        }
    }

    /**
     * A functional interface for consuming a {@link Expense} instance.
     */
    @FunctionalInterface
    interface ExpenseConsumer {
        void accept(Expense expense);
    }

    /**
     * Utility class for handling expense form creation and data extraction.
     */
    static class ExpenseForm {
        /**
         * Creates a form for entering or editing an expense's details.
         *
         * @param expense The {@link Expense} whose details are to be used as initial form values.
         * @return A {@link Pair} containing the form as a {@link GridPane} and a map of form fields.
         * TODO this method needs to be in the same controller as the expense overview
         */
        static Pair<GridPane, Map<String, Control>> createExpenseForm(Expense expense) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            TextField categoryField = createTextField(expense.getCategory(), "Category");
            TextField amountField = createTextField(String.valueOf(expense.getAmount()), "Amount");

            Map<String, Control> formFields = new HashMap<>();
            formFields.put("Category", categoryField);
            formFields.put("Amount", amountField);

            int row = 0;
            for (Map.Entry<String, Control> entry : formFields.entrySet()) {
                Label label = new Label(entry.getKey() + ": ");
                grid.add(label, 0, row);
                grid.add(entry.getValue(), 1, row++);
            }
            return new Pair<>(grid, formFields);
        }

        /**
         * Creates a text field with the specified initial value and prompt text.
         * @param value The initial value for the text field.
         * @param promptText The prompt text to display in the text field.
         * @return A {@link TextField} with the specified initial value and prompt text.
         */
        static TextField createTextField(String value, String promptText) {
            TextField textField = new TextField(value);
            textField.setPromptText(promptText);
            textField.setPrefWidth(300);
            return textField;
        }

        /**
         * Extracts the details of the expense from the form fields and creates a {@link Expense} instance.
         * @param formFields The map of form fields containing expense details.
         * @param expense The original participant.
         * @return A new {@link Expense} instance with details extracted from the form fields.
         */
        static Expense extractExpenseFromForm(Map<String, Control> formFields, Expense expense) {
            expense.setCategory(((TextField) formFields.get("Category")).getText());
            expense.setAmount(Double.parseDouble(((TextField) formFields.get("Amount")).getText()));
            return expense;
        }
    }

}

