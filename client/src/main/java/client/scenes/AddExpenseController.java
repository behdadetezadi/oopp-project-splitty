package client.scenes;

import client.utils.*;
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

import java.util.*;

public class AddExpenseController implements LanguageChangeListener{
    private final ServerUtils server;
    private final MainController mainController;
    private final Stage primaryStage;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    @FXML
    private Label expenseFor;
    @FXML
    private Button backButton;
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
    @FXML
    private Button undoButton;
    private Event event;
    private long selectedParticipantId;
    private AddExpenseCommand addedExpenseCommand;
    private UndoManager undoManager;
    private Map<String, String> tagKeysToLocalized = new HashMap<>();

    /**
     * constructor
     * @param primaryStage primary stage
     * @param server server
     * @param mainController mainController
     * @param event Event
     * @param undoManager undo manager
     */
    @Inject
    public AddExpenseController(Stage primaryStage, ServerUtils server,
                                MainController mainController, Event event, UndoManager undoManager) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
        this.undoManager = undoManager;
    }


    /**
     * initialize method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);

        cancelButton.setOnAction(this::handleCancelAction);
        addExpenseButton.setOnAction(this::handleAddExpenseAction);

        DialogUtils.otherTagInputDialog(comboBox, () -> resourceBundle);
        undoButton.setMnemonicParsing(true);
    }

    /**
     * called by mainController
     * @param event event
     * @param participantId participant id
     */
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
        AnimationUtil.animateText(expenseFor, resourceBundle.getString("Add_Expense_for"));
        AnimationUtil.animateText(expenseDescription, resourceBundle.getString("Expense"));
        AnimationUtil.animateText(amountPaid, resourceBundle.getString("Amount_paid"));
        AnimationUtil.animateText(cancelButton, resourceBundle.getString("Cancel"));
        AnimationUtil.animateText(addExpenseButton, resourceBundle.getString("Add_expense"));
        AnimationUtil.animateText(undoButton, resourceBundle.getString("Undo"));
        AnimationUtil.animateText(backButton, resourceBundle.getString("back"));

        TagUtils.initializeTagLanguageMapping(resourceBundle, tagKeysToLocalized);
        TagUtils.initializeTagsComboBox(resourceBundle, comboBox, tagKeysToLocalized);

        // animate participant name, if participant exists
        if(selectedParticipantId != 0) {
            AnimationUtil.animateText(participantLabel, ServerUtils.getParticipant(selectedParticipantId).getFirstName()
                    + " " + ServerUtils.getParticipant(selectedParticipantId).getLastName());
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
        String tagLocalized = comboBox.getValue();
        double amountValue;

        if (category == null || category.isEmpty()) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_description"),
                    resourceBundle.getString("error"), resourceBundle.getString("The_category_cannot_be_empty."));
            return;
        }
        String normalizedAmount = amount.replace(',', '.');
        if (normalizedAmount.endsWith(".")) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_amount"), resourceBundle.getString("error"),
                    resourceBundle.getString("Please_enter_a_valid_number_for_the_amount."));
            return;
        }
        if (tagLocalized == null || tagLocalized.isEmpty()) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_tag"), resourceBundle.getString("error"),
                    resourceBundle.getString("Please_select_a_tag."));
            return;
        }

        try {
            amountValue = Double.parseDouble(normalizedAmount);
            Expense newExpense = new Expense(ServerUtils.findParticipantById(selectedParticipantId), category,
                    amountValue, event.getId());

            String tagKey = tagKeysToLocalized.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(tagLocalized))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(tagLocalized);

            newExpense.setExpenseType(tagKey);

            addedExpenseCommand = new AddExpenseCommand(newExpense, event.getId(), expense -> {
                Platform.runLater(() -> {
                    if (expense != null) {
                        undoButton.setDisable(false);
                        AlertUtils.showInformationAlert(resourceBundle.getString("Expense_Added"), "Information",
                                resourceBundle.getString("The_expense_has_been_successfully_added."));
                    } else {
                        AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                                resourceBundle.getString("Unexpected_Error"),
                                resourceBundle.getString("An_unexpected_error_occurred"));
                    }
                });
            }, resourceBundle);
            undoManager.executeCommand(addedExpenseCommand);
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Invalid_amount"), resourceBundle.getString("error"),
                    resourceBundle.getString("Please_enter_a_valid_number_for_the_amount."));
        } catch (RuntimeException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("Unexpected_Error"), resourceBundle.getString("error"),
                    resourceBundle.getString("An_unexpected_error_occurred") + e.getMessage());
        }
    }
    /**
     * Handles the action to undo the last added expense.
     */
    @FXML
    void handleUndoAction() {
        undoManager.undoLastCommand();

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
        if (AlertUtils.showConfirmationAlert(resourceBundle.getString("CancelAddExpense"),
                resourceBundle.getString("Are_you_sure_you_want_to_cancel?"))) {
            mainController.showEventOverview(this.event);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * switches to the event overview
     */
    @FXML
    void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event);

    }

    /**
     * clears the text field when you go to this page
     */
    public void clearTextFields() {
        expenseDescription.clear();
        amountPaid.clear();
    }

}
