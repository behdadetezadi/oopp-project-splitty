package client.scenes;

import client.utils.LanguageChangeListener;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.*;

public class ExpenseOverviewController implements LanguageChangeListener {
    @FXML
    private Button backButton;
    @FXML
    private ListView<String> expensesListView;
    @FXML
    private Label sumOfExpensesLabel;
    @FXML
    private Button statisticsButton;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;
    private ResourceBundle resourceBundle;
    private Locale activeLocale;

    @Inject
    public ExpenseOverviewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
        server.registerForExpenses("/topic/expense", event.getId(), this::addExpenseToUI);
    }

    private void addExpenseToUI(Expense expense) {
        if (expense.getEventId() == this.event.getId()) {
            Platform.runLater(() -> {
                String expenseDisplay = formatExpenseForDisplay(expense, expensesListView.getItems().size() + 1);
                expensesListView.getItems().add(expenseDisplay);
                updateSumOfExpenses(expense.getAmount());
            });
        }
    }


    private void updateSumOfExpenses(double amount) {
        if (sumOfExpensesLabel.getText() != null && !sumOfExpensesLabel.getText().isEmpty()) {
            String currentTotal = sumOfExpensesLabel.getText().substring(resourceBundle.getString("total").length()).trim();
            double total = Double.parseDouble(currentTotal) + amount;
            sumOfExpensesLabel.setText(String.format(resourceBundle.getString("total"), String.format("%.2f", total)));
        } else {
            sumOfExpensesLabel.setText(String.format(resourceBundle.getString("total"), String.format("%.2f", amount)));
        }
    }



    /**
     * Initialize method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);
        initializeExpensesForEvent(this.event);

    }
    /**
     * Set the event and initialize expenses
     * @param event
     */
    public void setEvent(Event event) {
        this.event = event;
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
        statisticsButton.setText(resourceBundle.getString("Show_Statistics"));
    }

    /**
     * Format the expense information for display
     * @param expense the expense need to be displayed
     * @param expenseNumber expense number
     * @return string
     */
    public String formatExpenseForDisplay(Expense expense, int expenseNumber) {
        int numberOfParticipants = mainController.getUpdatedParticipantList(event).size();
        double amountOwedPerParticipant = expense.getAmount() / numberOfParticipants;

        // Using ResourceBundle for localized strings
        String expenseTemplate = resourceBundle.getString("expenseDetail");
        StringBuilder displayBuilder = new StringBuilder(String.format(expenseTemplate, expenseNumber, expense.getParticipant().getFirstName(), expense.getAmount(), expense.getCategory()));

        displayBuilder.append(String.format(resourceBundle.getString("tagTitle"),this.tagLanguageSwitch(expense.getExpenseType())));
        displayBuilder.append(resourceBundle.getString("DebtDetail"));
        String participantOwesTemplate = resourceBundle.getString("participantOwes");
        for (Participant participant : mainController.getUpdatedParticipantList(event)) {
            if (!participant.equals(expense.getParticipant())) {
                displayBuilder.append(String.format(participantOwesTemplate, participant.getFirstName(), amountOwedPerParticipant));
            }
            if(numberOfParticipants==1) {
                String noOneOwesMessage = resourceBundle.getString("expenseFullyCovered");
                displayBuilder.append(noOneOwesMessage);
            }
        }

        return displayBuilder.toString();
    }

    /**
     * makes sure the language switch works with the tags
     * @param expenseType the tag
     * @return the string in the right language
     */
    public String tagLanguageSwitch(String expenseType) {
        switch (expenseType) {
            case "Food":
                return resourceBundle.getString("tagFood");
            case "EntranceFees":
                return resourceBundle.getString("tagEntranceFees");
            case "Travel":
                return resourceBundle.getString("tagTravel");
            default:
                return expenseType;
        }
    }

    /**
     * method to switch to the event overview page
     */
    @FXML
    void switchToEventOverviewScene() {
        mainController.showEventOverview(event);
    }

    /**
     * method to switch to the stats page
     */
    @FXML
    void switchToStatistics() {
        mainController.showStatistics(event);
    }

    /**
     * initialises the expenses for an event
     * @param event the event
     */
    public void initializeExpensesForEvent(Event event) {
        try {
            List<Expense> expenses = server.getExpensesForEvent(event.getId());
            expensesListView.getItems().clear(); // Clear existing items
            double sumOfExpenses = 0;
            for (int i=0;i<expenses.size();i++) {
                if (expenses.get(i) == null) {
                    expensesListView.getItems().add(resourceBundle.getString("noExpensesRecorded"));
                }
                else {
                    String expenseDisplay = formatExpenseForDisplay(expenses.get(i),i+1);
                    expensesListView.getItems().add(expenseDisplay);
                    sumOfExpenses += expenses.get(i).getAmount();
                }
            }
            sumOfExpensesLabel.setText(String.format(resourceBundle.getString("total"), String.format("%.2f", sumOfExpenses)));
        } catch (RuntimeException ex) {
            // Handle case where no expenses are found
            expensesListView.getItems().clear();
            expensesListView.getItems().add(String.format(resourceBundle.getString("NoExpense")));
            sumOfExpensesLabel.setText(String.format(resourceBundle.getString("total")));
        }
    }

}






