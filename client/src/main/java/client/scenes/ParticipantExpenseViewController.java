package client.scenes;

import client.utils.LanguageChangeListener;
import client.utils.LanguageUtils;
import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.undoable.DeleteExpenseCommand;
import client.utils.undoable.EditExpenseCommand;
import client.utils.undoable.UndoManager;
import client.utils.undoable.UndoableCommand;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;

import static client.scenes.TableOfParticipantsController.ParticipantForm.createComboBox;
import static client.utils.ValidationUtils.isValidDouble;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class ParticipantExpenseViewController implements LanguageChangeListener {
    @FXML
    private ListView<String> expensesListView;
    @FXML
    private Label sumOfExpensesLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button undoButton;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;
    private long selectedParticipantId;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    private DeleteExpenseCommand deleteExpenseCommand;
    private EditExpenseCommand editExpenseCommand;
    private UndoManager undoManager;
    private BiConsumer<Expense, String> updateUI;

    @Inject
    public ParticipantExpenseViewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event,UndoManager undoManager) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
        this.undoManager = undoManager;
    }

    /**
     * Initialize method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);
        undoButton.setMnemonicParsing(true);
        undoButton.setOnAction(this::handleUndoAction);
    }

    public void setEvent(Event event, long participantId) {
        this.event = event;
        this.selectedParticipantId = participantId;
    }

    /**
     * sets the resource bundle
     * @param resourceBundle The resource bundle to set.
     */
    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * sets the active locale
     * @param locale The new locale to set as active.
     */
    @Override
    public void setActiveLocale(Locale locale) {
        this.activeLocale = locale;
    }

    /**
     * gets the main controller
     * @return main controller
     */
    @Override
    public MainController getMainController() {
        return mainController;
    }

    /**
     * updates the UI elements with the selected language
     */
    public void updateUIElements() {
        backButton.setText(resourceBundle.getString("back"));
    }

    private String formatExpenseForDisplay(Expense expense) {
        return String.format("%s: %.2f", expense.getCategory(), expense.getAmount());
    }

    public void initializeExpensesForParticipant(Long participantId) {
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);

        expensesListView.getItems().clear();
        if (expenses.isEmpty()) {
            System.out.println("Setting no expense text to: " + resourceBundle.getString("noExpensesRecorded"));
            expensesListView.getItems().add(resourceBundle.getString("noExpensesRecorded"));
            sumOfExpensesLabel.setText(String.format(resourceBundle.getString("total"), "0.00"));
        } else {
            double sumOfExpenses = 0;
            for (Expense expense : expenses) {
                String expenseDisplay = formatExpenseForDisplay(expense);
                expensesListView.getItems().add(expenseDisplay);
                sumOfExpenses += expense.getAmount();
            }
            sumOfExpensesLabel.setText(String.format(resourceBundle.getString("total"), String.format("%.2f", sumOfExpenses)));
        }

        expensesListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    Expense expense = getExpenseFromListView(getIndex(), participantId);
                    if (expense != null && !item.equals(resourceBundle.getString("noExpensesRecorded"))) {
                        Button editButton = new Button(resourceBundle.getString("edit"));
                        Button deleteButton = new Button(resourceBundle.getString("delete"));
                        editButton.setOnAction(event -> handleEditButton(expense));
                        deleteButton.setOnAction(event -> handleDeleteButton(expense));
                        HBox buttonsBox = new HBox(editButton, deleteButton);
                        buttonsBox.setSpacing(10);
                        setGraphic(buttonsBox);
                    } else {
                        setGraphic(null);
                    }
                }
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
     * Handles the action when the delete button is clicked for a specific expense.
     * Displays a confirmation dialog before deleting the expense.
     * Executes the DeleteExpenseCommand to delete the expense.
     * Shows appropriate alerts based on the result of the deletion.
     * @param expense The expense to be deleted.
     */
    @FXML
    private void handleDeleteButton(Expense expense) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                resourceBundle.getString("confirmDeleteExpense"),
                ButtonType.YES, ButtonType.NO);
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    deleteExpenseCommand = new DeleteExpenseCommand(expense, event.getId(), (result, actionType) -> {
                        if (result != null) {

                            updateUI(result, actionType);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(resourceBundle.getString("errorTitle"));
                            alert.setHeaderText(null);
                            alert.setContentText(resourceBundle.getString("deleteExpenseFail"));
                            alert.showAndWait();
                        }
                    }, resourceBundle);
                    undoManager.executeCommand(deleteExpenseCommand);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(resourceBundle.getString("errorTitle"));
                    alert.setHeaderText(null);
                    alert.setContentText(resourceBundle.getString("deleteExpenseFail") + e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

     /**
     * Updates the UI based on the action performed (delete or undo) on an expense.
     * Shows appropriate success messages for deletion or undo operations.
     * @param expense The expense that was deleted or undone.
     * @param actionType The type of action performed ("deleted" or "undone").
     */
    public void updateUI(Expense expense, String actionType) {
        Platform.runLater(() -> {
            if (actionType.equals("deleted")) {
                AlertUtils.showSuccessAlert(resourceBundle.getString("expenseDeletedTitle"), null, resourceBundle.getString("expenseDeletedSuccess"));
            } else if (actionType.equals("undone")) {
                AlertUtils.showSuccessAlert(resourceBundle.getString("undoSuccessTitle"), null, resourceBundle.getString("undoExpenseSuccess"));
            }
            initializeExpensesForParticipant(selectedParticipantId);
            undoButton.setDisable(false);
        });
    }



    /**
     * Handles the action to undo the last deleted expense.
     * @param event The action event triggered by clicking the Undo button.
     */
    @FXML
    private void handleUndoAction(ActionEvent event) {
        UndoableCommand undoneCommand = undoManager.undoLastCommand();
        initializeExpensesForParticipant(selectedParticipantId);

        if (undoneCommand == null) {
          undoButton.setDisable(true);
        }
    }



    @FXML
    private void handleEditButton(Expense selectedExpense) {
        if (selectedExpense != null) {
            String title = resourceBundle.getString("editExpense");
            String header = resourceBundle.getString("editExpenseDetails");
            editExpense(selectedExpense, title, header, this::updateExpense);
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
        ExpenseDialog dialog = new ExpenseDialog(selectedExpense, title, header, this::validateExpenseData);
        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(action::accept);
    }

    /**
     * Updates the details of an existing expense in the event
     * @param expense The {@link Expense} whose details are to be updated.
     */
    private void updateExpense(Expense expense) {
        Expense originalExpense = getOriginalExpense(expense.getId(),event.getId());
         editExpenseCommand = new EditExpenseCommand(originalExpense, expense, event.getId(), result -> {
            // This callback will be executed upon successful edit
            Platform.runLater(() -> {
                initializeExpensesForParticipant(selectedParticipantId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resourceBundle.getString("expenseSaved"));
                alert.setHeaderText(null);
                alert.setContentText(resourceBundle.getString("expenseSavedSuccess"));
                alert.showAndWait();
            });
        }, resourceBundle);
        undoManager.executeCommand(editExpenseCommand);
        undoButton.setDisable(false);
    }
    private Expense getOriginalExpense(long expenseId,long eventId)
    {
        return ServerUtils.findSpecificExpenseByEventId(expenseId,eventId);
    }

    /**
     * A dialog for creating or editing an expense's details.
     */
    static class ExpenseDialog extends Dialog<Expense> {
        ExpenseDialog(Expense expense, String title, String header, Validator validator) {
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

            Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
            saveButton.addEventFilter(ActionEvent.ACTION, event -> {
                String amount = ((TextField) formFields.get("Amount")).getText();
                String category = ((TextField) formFields.get("Category")).getText();
                List<String> validationErrors = validator.validate(amount, category);
                if (!validationErrors.isEmpty()) {
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", validationErrors), ButtonType.OK);
                    alert.setHeaderText("Validation Error");
                    alert.showAndWait();
                }
            });
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
         */
        static Pair<GridPane, Map<String, Control>> createExpenseForm(Expense expense) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            TextField categoryField = createTextField(expense.getCategory(), "Category");
            TextField amountField = createTextField(String.valueOf(expense.getAmount()), "Amount");
            ComboBox<String> tagComboBox = createComboBox(expense.getExpenseType(), "Tag",
                    "Food", "Entrance fees", "Travel", "Other");
            tagComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if ("Other".equals(newValue)) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("New Tag");
                    dialog.setHeaderText("Enter a new tag:");
                    dialog.setContentText("Tag:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(tag -> {
                        if (!tag.isEmpty() && !tagComboBox.getItems().contains(tag)) {
                            tagComboBox.getItems().add(tag);
                            tagComboBox.getSelectionModel().select(tag);
                        }
                    });
                }
            });

            Map<String, Control> formFields = new HashMap<>();
            formFields.put("Category", categoryField);
            formFields.put("Amount", amountField);
            formFields.put("Tag", tagComboBox);

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
            String category = ((TextField) formFields.get("Category")).getText();
            String amount = ((TextField) formFields.get("Amount")).getText();
            String tag = ((ComboBox<String>) formFields.get("Tag")).getValue();
            expense.setCategory(category);
            expense.setAmount(Double.parseDouble(amount));
            expense.setExpenseType(tag);
            return expense;
        }
    }

    @FunctionalInterface
    public interface Validator {
        List<String> validate(String amount, String category);
    }

    /**
     * method to validate expense data by checking the fields entered
     * @param amount the entered amount
     * @param category the entered category
     * @return a list of strings
     */
    private List<String> validateExpenseData(String amount, String category) {
        List<String> errors = new ArrayList<>();
        if (!isValidDouble(amount)) {
            errors.add(resourceBundle.getString("amountFormatError"));
        }
        if (category == null || category.isEmpty()) {
            errors.add(resourceBundle.getString("categoryEmptyError="));
        }
        return errors;
    }
}