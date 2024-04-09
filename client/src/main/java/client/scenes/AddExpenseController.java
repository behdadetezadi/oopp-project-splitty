package client.scenes;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.ValidationUtils;
import client.utils.undoable.AddExpenseCommand;
import client.utils.undoable.UndoManager;
import client.utils.undoable.UndoableCommand;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddExpenseController {
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    @FXML
    private Label expenseFor;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private TextField expenseDescription;
    @FXML
    private TextField amountPaid;
    @FXML
    private Label participantLabel;
    @FXML
    private ComboBox<String> comboBox;
    private String[] tags = {"Food", "Entrance fees", "Travel", "Other"};
    @FXML
    private Button undoButton;
    private Event event;
    private long selectedParticipantId;
    private AddExpenseCommand addedExpenseCommand;
    private UndoManager undoManager;


    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController mainController
     * @param event Event
     */
    @Inject
    public AddExpenseController(Stage primaryStage, ServerUtils server, MainController mainController, Event event, UndoManager undoManager) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
        this.undoManager = undoManager;
    }


    /**
     * default constructor that JavaFX can use to instantiate the controller.
     */
    public AddExpenseController() {
        // Default constructor
    }

    /**
     * called by mainController
     * @param event event
     */
    public void setEvent(Event event, long participantId, Locale locale) {
        this.activeLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle("message", locale);
        updateUIElements();
        this.event = event;
        this.selectedParticipantId = participantId;
        participantLabel.setText(ServerUtils.getParticipant(selectedParticipantId).getFirstName()
                + " " + ServerUtils.getParticipant(selectedParticipantId).getLastName());
        initialize();
    }

    /**
     * updates the ui elements, this is necessary for the language switch
     */
    public void updateUIElements() {
        expenseFor.setText(resourceBundle.getString("Add_Expense_for"));
        participantLabel.setText(resourceBundle.getString("participant"));
        expenseDescription.setPromptText(resourceBundle.getString("Expense"));
        amountPaid.setPromptText(resourceBundle.getString("Amount_paid"));
        cancelButton.setText(resourceBundle.getString("Cancel"));
        addExpenseButton.setText(resourceBundle.getString("Add_expense"));
    }


    /**
     * Initialises the UI components and event handlers if an event is provided.
     * This method is called automatically by JavaFX after loading the FXML file.
     */
    @FXML
    public void initialize() {
        if(event != null) {
            cancelButton.setOnAction(this::handleCancelAction);
            addExpenseButton.setOnAction(this::handleAddExpenseAction);
            amountPaid.addEventFilter(KeyEvent.KEY_TYPED, this::validateAmountInput);
            addExpenseButton.getStyleClass().add("button-hover");
            cancelButton.getStyleClass().add("button-hover");
            comboBox.getItems().clear();
            for (String tag : tags) {
                if (!comboBox.getItems().contains(tag)) {
                    comboBox.getItems().add(tag);
                }
            }
            comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if ("Other".equals(newValue)) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("New Tag");
                    dialog.setHeaderText("Enter a new tag:");
                    dialog.setContentText("Tag:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(tag -> {
                        if (!tag.isEmpty() && !comboBox.getItems().contains(tag)) {
                            comboBox.getItems().add(tag);
                            comboBox.getSelectionModel().select(tag);
                        }
                    });
                }
            });
        }
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
     * @param actionEvent button press
     */
    @FXML
    private void handleAddExpenseAction(ActionEvent actionEvent) {
        String category = this.expenseDescription.getText();
        String amount = this.amountPaid.getText();
        String selectedTag = comboBox.getValue();
        double amountValue;

        if (selectedTag == null || selectedTag.isEmpty()) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_tag"), resourceBundle.getString("Error"),
                    resourceBundle.getString("Please_select_a_tag."));
            return;
        }

        if (category == null || category.isEmpty()) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_description"),
                    resourceBundle.getString("Error"), resourceBundle.getString("The_category_cannot_be_empty."));
            return;
        }
        String normalizedAmount = amount.replace(',', '.');
        if (normalizedAmount.endsWith(".")) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_amount"), resourceBundle.getString("Error"),
                    resourceBundle.getString("Please_enter_a_valid_number_for_the_amount."));
            return;
        }

        try {
            amountValue = Double.parseDouble(normalizedAmount);
            Expense newExpense = new Expense(ServerUtils.findParticipantById(selectedParticipantId), category,
                    amountValue, event.getId());
            newExpense.setExpenseType(selectedTag);
            addedExpenseCommand = new AddExpenseCommand(newExpense, event.getId(), expense -> {
                Platform.runLater(() -> {
                    if (expense != null) {
                        Platform.runLater(() -> undoButton.setDisable(false));
                        AlertUtils.showInformationAlert(resourceBundle.getString("Expense_Added"), "Information",
                                resourceBundle.getString("The_expense_has_been_successfully_added."));
                        Platform.runLater(() -> {
                            undoButton.setDisable(false);
                        });
                    } else {
                        AlertUtils.showErrorAlert(resourceBundle.getString("error"), resourceBundle.getString("Unexpected_Error"),
                                resourceBundle.getString("An_unexpected_error_occurred"));
                    }
                });
            }, resourceBundle);
            undoManager.executeCommand(addedExpenseCommand);
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_Amount"), resourceBundle.getString("error"), resourceBundle.getString("Please_enter_a_valid_amount"));
        } catch (RuntimeException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Unexpected_Error"), resourceBundle.getString("error"), resourceBundle.getString("An_unexpected_error_occurred") + e.getMessage());
        }
    }
    /**
     * Handles the action to undo the last added expense.
     * @param event The action event triggered by clicking the Undo button.
     */
    @FXML
    private void handleUndoAction(ActionEvent event) {
        UndoableCommand undoneCommand = undoManager.undoLastCommand();

        if (undoManager.getExecutedCommands().isEmpty()) {
            expenseDescription.clear();
            amountPaid.clear();
            undoButton.setDisable(true);
            return;
        }
        UndoableCommand lastCommand = undoManager.getExecutedCommands().peek();
        if (lastCommand instanceof AddExpenseCommand addExpenseCommand) {
            Expense lastExpense = addExpenseCommand.getAddedExpense();
            if (lastExpense != null) {
                expenseDescription.setText(lastExpense.getCategory());
                amountPaid.setText(String.valueOf(lastExpense.getAmount()));
            }
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
            alert.setContentText(resourceBundle.getString("Are_you_sure_you_want_to_cancel?"));
            if (alert.showAndWait().get() == ButtonType.OK) {
                mainController.showEventOverview(this.event, activeLocale);
            }
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * switches to the event overview
     */
    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event, activeLocale);

    }

    /**
     * clears the text field when you go to this page
     */
    public void clearTextFields() {
        expenseDescription.clear();
        amountPaid.clear();
    }

}
