package client.scenes;

import client.utils.*;
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
    private final ServerUtils server;
    private final MainController mainController;
    private final Stage primaryStage;
    private Event event;
    private long selectedParticipantId;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    private DeleteExpenseCommand deleteExpenseCommand;
    private EditExpenseCommand editExpenseCommand;
    private UndoManager undoManager;
    private BiConsumer<Expense, String> updateUI;
    private Map<String, String> tagKeysToLocalized = new HashMap<>();

    @Inject
    public ParticipantExpenseViewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event,UndoManager undoManager) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
        this.undoManager = undoManager;
        server.registerForExpenses("/topic/expense", event.getId(), this::addExpenseToUI);

    }
    private void addExpenseToUI(Expense expense) {
        Platform.runLater(() -> {
            if (!expensesListView.getItems().isEmpty() && expensesListView.getItems().get(0).equals(resourceBundle.getString("noExpensesRecorded"))) {
                expensesListView.getItems().clear();
            }
            updateExpenseListView(expense);
            updateSumOfExpenses(expense.getAmount());
        });
    }


    private void updateExpenseListView(Expense expense) {
        String expenseDisplay = formatExpenseForDisplay(expense);
        expensesListView.getItems().add(expenseDisplay);
        expensesListView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    Button editButton = new Button("Edit");
                    Button deleteButton = new Button("Delete");
                    editButton.setOnAction(e -> handleEditButton(expense));
                    deleteButton.setOnAction(e -> handleDeleteButton(expense));
                    HBox buttonsBox = new HBox(10, editButton, deleteButton);
                    setGraphic(buttonsBox);
                }
            }
        });
    }


    private void updateSumOfExpenses(double amount) {
        try {
            String text = sumOfExpensesLabel.getText();
            double total = 0.0;
            if (text != null && !text.isEmpty()) {
                total = Double.parseDouble(text.split(": ")[1].trim());
            }
            sumOfExpensesLabel.setText("Total: " + String.format("%.2f", total + amount));
        } catch (NumberFormatException e) {
            System.err.println("Error updating expenses total: " + e.getMessage());
        }
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
        initializeExpensesForParticipant(this.selectedParticipantId);
    }

    public void setEvent(Event event, long participantId) {
        this.event = event;
        this.selectedParticipantId = participantId;
        initializeExpensesForParticipant(participantId);

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
        TagUtils.initializeTagLanguageMapping(resourceBundle, tagKeysToLocalized);
        AnimationUtil.animateText(backButton, resourceBundle.getString("back"));
        AnimationUtil.animateText(undoButton, resourceBundle.getString("Undo"));
    }

    private String formatExpenseForDisplay(Expense expense) {
        return String.format("%s: %.2f", expense.getCategory(), expense.getAmount());
    }

    public void initializeExpensesForParticipant(Long participantId) {
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);

        expensesListView.getItems().clear();
        if (expenses.isEmpty()) {
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

        expensesListView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    if (item.equals(resourceBundle.getString("noExpensesRecorded"))) {
                        setGraphic(null);
                    } else {
                        Button editButton = new Button(resourceBundle.getString("edit"));
                        Button deleteButton = new Button(resourceBundle.getString("delete"));
                        editButton.setOnAction(e -> handleEditButton(getExpenseFromListView(getIndex(), selectedParticipantId)));
                        deleteButton.setOnAction(e -> handleDeleteButton(getExpenseFromListView(getIndex(), selectedParticipantId)));
                        HBox buttonsBox = new HBox(editButton, deleteButton);
                        buttonsBox.setSpacing(10);
                        setGraphic(buttonsBox);
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
    void switchToEventOverviewScene() {
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
        if (AlertUtils.showConfirmationAlert(resourceBundle.getString("deleteExpense"),
                resourceBundle.getString("confirmDeleteExpense"))) {
                try {
                    deleteExpenseCommand = new DeleteExpenseCommand(expense, event.getId(), (result, actionType) -> {
                        if (result != null) {

                            updateUI(result, actionType);
                        } else {
                            AlertUtils.showErrorAlert(resourceBundle.getString("errorTitle"),
                                    resourceBundle.getString("errorTitle"), resourceBundle.getString("deleteExpenseFail"));
                        }
                    }, resourceBundle);
                    undoManager.executeCommand(deleteExpenseCommand);
                } catch (Exception e) {
                    AlertUtils.showErrorAlert(resourceBundle.getString("errorTitle"), resourceBundle.getString("errorTitle"),
                            resourceBundle.getString("deleteExpenseFail") + e.getMessage());
                }
        }
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


    /**
     * overloaded handleUndoAction method to be used by keyboard shortcuts
     */
    @FXML
    void handleUndoAction() {
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
        ExpenseDialog dialog = new ExpenseDialog(selectedExpense, title, header,
                this::validateExpenseData, resourceBundle, tagKeysToLocalized);
        Optional<Expense> result = dialog.showAndWait();
        result.ifPresent(action::accept);
    }

    /**
     * Updates the details of an existing expense in the event
     * @param expense The {@link Expense} whose details are to be updated.
     */
    private void updateExpense(Expense expense) {
        Expense originalExpense = getOriginalExpense(expense.getId(),event.getId());
         editExpenseCommand = new EditExpenseCommand(originalExpense, expense, event.getId(), result -> Platform.runLater(() -> {
             initializeExpensesForParticipant(selectedParticipantId);
             AlertUtils.showInformationAlert(resourceBundle.getString("expenseSaved"),
                     resourceBundle.getString("expenseSaved"), resourceBundle.getString("expenseSavedSuccess"));
         }), resourceBundle);
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
        ExpenseDialog(Expense expense, String title, String header, Validator validator,
                      ResourceBundle resourceBundle, Map<String, String> tagKeysToLocalized) {
            setTitle(title);
            setHeaderText(header);

            ButtonType saveButtonType = new ButtonType(resourceBundle.getString("Save"),
                    ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType(resourceBundle.getString("Cancel"),
                    ButtonBar.ButtonData.CANCEL_CLOSE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
            getDialogPane().setMinHeight(350);
            getDialogPane().setMinWidth(600);

            Pair<GridPane, Map<String, Control>> formPair = ExpenseForm.createExpenseForm(expense, resourceBundle,
                    tagKeysToLocalized);
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
                    return ExpenseForm.extractExpenseFromForm(formFields, expense, tagKeysToLocalized);
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
        static Pair<GridPane, Map<String, Control>> createExpenseForm(Expense expense, ResourceBundle resourceBundle, Map<String, String> tagKeysToLocalized) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            TextField categoryField = createTextField(expense.getCategory(), "Category");
            TextField amountField = createTextField(String.valueOf(expense.getAmount()), "Amount");

            // set the tags in the combobox in this order
            ComboBox<String> tagComboBox = new ComboBox<>();
            TagUtils.initializeTagsComboBox(resourceBundle, tagComboBox, tagKeysToLocalized);

            // check if current tag is custom
            String currentTag = expense.getExpenseType();
            String localizedCurrentTag = tagKeysToLocalized.get(currentTag);
            if (localizedCurrentTag == null) {
                tagComboBox.getItems().add(tagComboBox.getItems().size() - 1, currentTag); // puts custom tag before 'Other'
                tagComboBox.setValue(currentTag);
            } else {
                tagComboBox.setValue(localizedCurrentTag);
            }

            // handle custom tag creation
            DialogUtils.otherTagInputDialog(tagComboBox, resourceBundle);

            Map<String, Control> formFields = new HashMap<>();

            formFields.put("Category", categoryField);
            formFields.put("Amount", amountField);
            formFields.put("Tag", tagComboBox);

            int row = 0;
            for (String fieldKey : Arrays.asList("Category", "Amount", "Tag")) {
                Label label = new Label(resourceBundle.getString(fieldKey) + ": ");
                grid.add(label, 0, row);
                grid.add(formFields.get(fieldKey), 1, row++);
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
        static Expense extractExpenseFromForm(Map<String, Control> formFields, Expense expense, Map<String, String> tagKeysToLocalized) {
            String category = ((TextField) formFields.get("Category")).getText();
            String amount = ((TextField) formFields.get("Amount")).getText();
            String tag = ((ComboBox<String>) formFields.get("Tag")).getValue();
            String tagKey = tagKeysToLocalized.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(tag))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(tag);

            expense.setCategory(category);
            expense.setAmount(Double.parseDouble(amount));
            expense.setExpenseType(tagKey);

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