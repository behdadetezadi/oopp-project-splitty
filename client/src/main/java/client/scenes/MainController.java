package client.scenes;

import client.utils.KeyboardUtils;
import client.utils.LanguageUtils;
import commons.Event;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * This is the main controller which controls the switching between scenes
 */
public class MainController {
    private Stage primaryStage;
    private Scene startScene;
    private StartPageController startPageController;
    private Scene eventOverviewScene;
    private EventOverviewController eventOverviewController;
    private AddExpenseController expenseCtrl;
    private Scene expenseScene;
    private ParticipantExpenseViewController participantExpenseViewController;
    private Scene participantExpenseViewScene;
    private ExpenseOverviewController expenseOverviewController;
    private Scene expenseOverviewScene;
    private StatsCtrl statsController;
    private Scene statisticsScene;
    private TableOfParticipantsController tableOfParticipantsController;
    private Scene tableOfParticipantsScene;
    private InviteController inviteController;
    private Scene inviteScene;
    private AdminController adminController;
    private Scene adminScene;
    private LoginController loginController;
    private Scene loginScene;
    private static final String LANGUAGE_PREFERENCE_KEY = "language";
    private final Locale locale = getStoredLanguagePreferenceOrDefault();
    private final KeyCombination createEventCombination = new KeyCodeCombination(
            KeyCode.C, KeyCombination.CONTROL_DOWN);
    private final KeyCombination joinEventCombination = new KeyCodeCombination(
            KeyCode.J, KeyCombination.CONTROL_DOWN);
    private final KeyCombination cycleRecentEventsCombination = new KeyCodeCombination(
            KeyCode.E, KeyCombination.CONTROL_DOWN);
    private final KeyCombination joinSelectedEventCombination = new KeyCodeCombination(
            KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
    private final KeyCombination addExpenseCombination = new KeyCodeCombination(
            KeyCode.W, KeyCombination.CONTROL_DOWN);
    private final KeyCombination showSendInvitesCombination = new KeyCodeCombination(
            KeyCode.I, KeyCombination.CONTROL_DOWN);
    private final KeyCombination changeTitleCombination = new KeyCodeCombination(
            KeyCode.T, KeyCombination.CONTROL_DOWN);
    private final KeyCombination showParticipantsCombination = new KeyCodeCombination(
            KeyCode.P, KeyCombination.CONTROL_DOWN);
    private final KeyCombination showAllExpensesCombination = new KeyCodeCombination(
            KeyCode.Q, KeyCombination.CONTROL_DOWN);
    private final KeyCombination showParticipantExpensesCombination = new KeyCodeCombination(
            KeyCode.E, KeyCombination.CONTROL_DOWN);
    private final KeyCombination showStatsCombination = new KeyCodeCombination(
            KeyCode.S, KeyCombination.CONTROL_DOWN);
    private final KeyCombination undoCombination = new KeyCodeCombination(
            KeyCode.Z, KeyCombination.CONTROL_DOWN);
    private final KeyCombination loginAsUserCombination = new KeyCodeCombination(
            KeyCode.U, KeyCombination.CONTROL_DOWN);
    private final KeyCombination loginAsAdminCombination = new KeyCodeCombination(
            KeyCode.A, KeyCombination.CONTROL_DOWN);
    private final KeyCombination switchLanguageCombination = new KeyCodeCombination(
            KeyCode.L, KeyCombination.CONTROL_DOWN);
    private final KeyCombination sendInvitesCombination = new KeyCodeCombination(
            KeyCode.S, KeyCombination.CONTROL_DOWN);

    /**
     * initializer method for mainController
     * @param primaryStage primary stage
     * @param groupedPairs map with pairs of controllers
     */
    public void initialize(Stage primaryStage, Map<String, Pair<?, Parent>> groupedPairs) {
        this.primaryStage = primaryStage;

        // Extract and set up each scene and controller
        this.startScene = new Scene(groupedPairs.get("startPage").getValue());
        this.startPageController = (StartPageController) groupedPairs.get("startPage").getKey();

        this.eventOverviewScene = new Scene(groupedPairs.get("overviewPage").getValue());
        this.eventOverviewController = (EventOverviewController) groupedPairs.get("overviewPage").getKey();

        this.expenseScene = new Scene(groupedPairs.get("expensePage").getValue());
        this.expenseCtrl = (AddExpenseController) groupedPairs.get("expensePage").getKey();

        this.participantExpenseViewScene = new Scene(groupedPairs.get("participantExpenseViewPage").getValue());
        this.participantExpenseViewController = (ParticipantExpenseViewController) groupedPairs
                .get("participantExpenseViewPage").getKey();

        this.expenseOverviewScene = new Scene(groupedPairs.get("expenseOverviewPage").getValue());
        this.expenseOverviewController = (ExpenseOverviewController) groupedPairs.get("expenseOverviewPage").getKey();

        this.tableOfParticipantsScene = new Scene(groupedPairs.get("participantsPage").getValue());
        this.tableOfParticipantsController = (TableOfParticipantsController) groupedPairs.get("participantsPage").getKey();

        this.inviteScene = new Scene(groupedPairs.get("invitePage").getValue());
        this.inviteController = (InviteController) groupedPairs.get("invitePage").getKey();

        this.loginScene = new Scene(groupedPairs.get("loginPage").getValue());
        this.loginController = (LoginController) groupedPairs.get("loginPage").getKey();

        this.adminScene = new Scene(groupedPairs.get("adminPage").getValue());
        this.adminController = (AdminController) groupedPairs.get("adminPage").getKey();

        this.statisticsScene = new Scene(groupedPairs.get("statsPage").getValue());
        this.statsController = (StatsCtrl) groupedPairs.get("statsPage").getKey();

        // Setup all keyboard shortcuts
        KeyboardUtils.addKeyboardShortcuts(startScene,
                startPageController::logout,
                new Pair<>(createEventCombination, startPageController::createEvent),
                new Pair<>(joinEventCombination, startPageController::joinEvent),
                new Pair<>(cycleRecentEventsCombination, startPageController::cycleThroughEvents),
                new Pair<>(joinSelectedEventCombination, startPageController::joinSelectedEvent),
                new Pair<>(switchLanguageCombination, startPageController::switchToNextLanguage)
        );

        KeyboardUtils.addKeyboardShortcuts(eventOverviewScene,
                eventOverviewController::goBackToStartPage,
                new Pair<>(showSendInvitesCombination, eventOverviewController::sendInvites),
                new Pair<>(changeTitleCombination, eventOverviewController::editTitle),
                new Pair<>(showParticipantsCombination, eventOverviewController::showParticipants),
                new Pair<>(showAllExpensesCombination, eventOverviewController::showAllExpensesOverview),
                new Pair<>(addExpenseCombination, eventOverviewController::addExpense),
                new Pair<>(showParticipantExpensesCombination, eventOverviewController::showExpensesForSelectedParticipant),
                new Pair<>(switchLanguageCombination, eventOverviewController::switchToNextLanguage)
        );

        KeyboardUtils.addKeyboardShortcuts(expenseScene,
                expenseCtrl::switchToEventOverviewScene,
                new Pair<>(undoCombination, expenseCtrl::handleUndoAction)
        );

        KeyboardUtils.addKeyboardShortcuts(participantExpenseViewScene,
                participantExpenseViewController::switchToEventOverviewScene,
                new Pair<>(undoCombination, participantExpenseViewController::handleUndoAction)
        );

        KeyboardUtils.addKeyboardShortcuts(expenseOverviewScene,
                expenseOverviewController::switchToEventOverviewScene,
                new Pair<>(showStatsCombination, expenseOverviewController::switchToStatistics)
        );

        KeyboardUtils.addKeyboardShortcuts(tableOfParticipantsScene,
                tableOfParticipantsController::handleBackButton
        );

        KeyboardUtils.addKeyboardShortcuts(inviteScene,
                inviteController::handleBackButtonAction,
                new Pair<>(sendInvitesCombination, inviteController::handleSubmitButtonAction)
        );

        KeyboardUtils.addKeyboardShortcuts(adminScene,
                adminController::logout
        );

        KeyboardUtils.addKeyboardShortcuts(loginScene,
                null,
                new Pair<>(loginAsUserCombination, loginController::handleUserLogin),
                new Pair<>(loginAsAdminCombination, loginController::handleAdminLoginPrompt),
                new Pair<>(switchLanguageCombination, loginController::switchToNextLanguage)
        );

        KeyboardUtils.addKeyboardShortcuts(statisticsScene,
                statsController::switchToExpenseOverviewScene
        );

        // Show initial scene
        showLoginPage();
        primaryStage.show();
    }

    /**
     * gets the stored language preference in restart of application
     * @return a locale
     */
    public Locale getStoredLanguagePreferenceOrDefault() {
        // Retrieve stored language preference from preferences
        Preferences prefs = Preferences.userNodeForPackage(StartPageController.class);
        String storedLanguage = prefs.get(LANGUAGE_PREFERENCE_KEY, null);

        // If a preference exists, return the corresponding Locale
        if (storedLanguage != null) {
            return Locale.forLanguageTag(storedLanguage);
        } else {
            // Default to a predefined language if no preference is found
            return Locale.getDefault();
        }
    }

    /**
     * stores the chosen language for application restart
     * @param locale a locale from message resourceBundle
     */
    public void storeLanguagePreference(Locale locale) {
        // Store selected language preference in preferences
        Preferences prefs = Preferences.userNodeForPackage(StartPageController.class);
        prefs.put(LANGUAGE_PREFERENCE_KEY, locale.toLanguageTag());
    }

    /**
     * shows the LoginPage
     */
    public void showLoginPage() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), loginController);
        loginController.setLanguageComboBox();
    }

    /**
     * shows the AdminPage
     */
    public void showAdminPage() {
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(adminScene);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), adminController);
        adminController.fetchAndPopulateEvents();
    }

    /**
     * refreshes the last events list in the start page
     */
    public void refreshEventsList() {
        startPageController.refreshEventsList();
    }


    /**
     * shows the StartPage
     */
    public void showStartPage() {
        primaryStage.setTitle("Start Page");
        startPageController.clearTextFields();
        primaryStage.setScene(startScene);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), startPageController);
        startPageController.setLanguageComboBox();
    }

    /**
     * Shows the event overview scene.
     * @param event The event to show overview for.
     */
    public void showEventOverview(Event event) {
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(eventOverviewScene);
        eventOverviewController.setEvent(event);
        eventOverviewController.refreshParticipants();
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), eventOverviewController);
        eventOverviewController.setLanguageComboBox();
    }

    /**
     *
     * @param event the event we are working on
     * @param participantId the id of the participant
     */
    public void showAddExpense(Event event, long participantId) {
        primaryStage.setTitle("Expenses: Add Expense");
        expenseCtrl.clearTextFields();
        primaryStage.setScene(expenseScene);
        expenseCtrl.setEvent(event, participantId);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), expenseCtrl);
    }

    /**
     *
     * @param event the event we are working on
     */
    public void showTableOfParticipants(Event event) {
        primaryStage.setTitle("Participants");
        primaryStage.setScene(tableOfParticipantsScene);
        tableOfParticipantsController.setEvent(event);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), tableOfParticipantsController);
    }

    /**
     * shows invite page
     * @param event event
     */
    public void showInvitePage(Event event){
        primaryStage.setTitle("InvitePage");
        primaryStage.setScene(inviteScene);
        inviteController.setEvent(event);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), inviteController);
    }

    /**
     * Shows the expense overview of the selected participant.
     * @param event The event to show overview for.
     * @param participantId id of the participant
     */
    public void showParticipantExpensesOverview(Event event, Long participantId) {
        primaryStage.setTitle("Participant Expenses Overview");
        primaryStage.setScene(participantExpenseViewScene);
        participantExpenseViewController.setEvent(event,participantId);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), participantExpenseViewController);
        participantExpenseViewController.initializeExpensesForParticipant(participantId);
    }

    /**
     * Shows the expense overview
     * @param event The event to show overview for.
     */
    public void showExpenseOverview(Event event) {
        primaryStage.setTitle("Expenses Overview");
        primaryStage.setScene(expenseOverviewScene);
        // Loads the active locale, sets the resource bundle, and updates the UI
        expenseOverviewController.setEvent(event);
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), expenseOverviewController);
        expenseOverviewController.initializeExpensesForEvent(event);
    }

    /**
     * Shows the expense overview
     * @param event The event to show overview for.
     */
    public void showStatistics(Event event)
    {
        statsController.setEvent(event);
        statsController.initializePieChart();
        primaryStage.setScene(statisticsScene);
        primaryStage.setTitle("Statistics");
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), statsController);
    }

    /**
     * get the updated participant list of the selected event.
     * @param event The event to show list for.
     * @return updated participant list
     */
    public List<Participant> getUpdatedParticipantList(Event event) {
        return tableOfParticipantsController.getUpdatedParticipant(event);
    }


    /**
     * getter for locale. needed for checkstyle.
     * @return users locale
     */

    public Locale getLocale() {
        return locale;
    }
}
