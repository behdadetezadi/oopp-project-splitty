package client.scenes;

import client.utils.LanguageUtils;
import commons.Event;
import commons.Participant;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Locale;
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
    private Locale locale = getStoredLanguagePreferenceOrDefault();

    /**
     * initializer method for mainController
     * @param primaryStage primary stage
     * @param startPair startPage pair
     * @param eventOverviewPair eventOverviewPage pair
     * @param expensePair expensePage pair
     * @param participantExpenseViewControllerPair participantExpenseViewController Pair
     * @param expenseOverviewControllerPair expenseOverviewScene Pair
     * @param tableOfParticipantsControllerPair table of participants page pair
     * @param inviteControllerPair invitePage pair
     * @param loginControllerPair adminController pair
     * @param adminControllerPair adminController pair
     */
    public void initialize(Stage primaryStage,
                           Pair<StartPageController, Parent> startPair,
                           Pair<EventOverviewController, Parent> eventOverviewPair,
                           Pair<AddExpenseController, Parent> expensePair,
                           Pair<ParticipantExpenseViewController, Parent> participantExpenseViewControllerPair,
                           Pair<ExpenseOverviewController,Parent>expenseOverviewControllerPair,
                           Pair<TableOfParticipantsController, Parent> tableOfParticipantsControllerPair,
                           Pair<InviteController, Parent> inviteControllerPair,
                           Pair<LoginController, Parent> loginControllerPair,
                           Pair<AdminController, Parent> adminControllerPair,
                           Pair<StatsCtrl, Parent> statsPair
                           )
    {

        this.primaryStage = primaryStage;

        this.startScene = new Scene(startPair.getValue());
        this.startPageController = startPair.getKey();

        this.eventOverviewScene = new Scene(eventOverviewPair.getValue());
        this.eventOverviewController = eventOverviewPair.getKey();

        this.expenseScene = new Scene(expensePair.getValue());
        this.expenseCtrl = expensePair.getKey();

        this.participantExpenseViewScene=new Scene(participantExpenseViewControllerPair.getValue());
        this.participantExpenseViewController=participantExpenseViewControllerPair.getKey();

        this.expenseOverviewScene=new Scene(expenseOverviewControllerPair.getValue());
        this.expenseOverviewController=expenseOverviewControllerPair.getKey();

        this.tableOfParticipantsScene = new Scene(tableOfParticipantsControllerPair.getValue());
        this.tableOfParticipantsController = tableOfParticipantsControllerPair.getKey();

        this.inviteScene = new Scene(inviteControllerPair.getValue());
        this.inviteController =  inviteControllerPair.getKey();

        this.adminScene = new Scene(adminControllerPair.getValue());
        this.adminController = adminControllerPair.getKey();

        this.loginScene = new Scene(loginControllerPair.getValue());
        this.loginController = loginControllerPair.getKey();

        this.statisticsScene = new Scene(statsPair.getValue());
        this.statsController = statsPair.getKey();

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
     * shows the StartPage
     */
    public void showStartPage() {
        primaryStage.setTitle("Start Page");
        startPageController.clearTextFields();
        primaryStage.setScene(startScene);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), startPageController);
        startPageController.setLanguageComboBox();
        startPageController.refreshEventsList();
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


        eventOverviewScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination goBackCombination = new KeyCodeCombination(KeyCode.Z,
                    KeyCombination.CONTROL_DOWN);
            final KeyCombination addExpenseCombination = new KeyCodeCombination(KeyCode.A,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (goBackCombination.match(ke)) {
                    eventOverviewController.goBackToStartPage(null);
                    ke.consume(); // <-- stops passing the event to next node
                }
                if (addExpenseCombination.match(ke)) {
                    eventOverviewController.addExpense();
                }
            }
        }
        );
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

        expenseOverviewScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination showStatsCombination = new KeyCodeCombination(KeyCode.S,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (showStatsCombination.match(ke)) {
                    expenseOverviewController.switchToStatistics();
                    ke.consume(); // <-- stops passing the event to next node
                }

            }
        }
        );
    }

    /**
     * Shows the expense overview
     * @param event The event to show overview for.
     */
    public void showStatistics(Event event)
    {
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statisticsScene);
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(getStoredLanguagePreferenceOrDefault(), statsController);
        statsController.initialize(event);
    }
    /**
     * get the updated participant list of the selected event.
     * @param event The event to show list for.
     * @return updated participant list
     */
    public List<Participant> getUpdatedParticipantList(Event event) {
        return tableOfParticipantsController.getUpdatedParticipant(event);
    }
}
