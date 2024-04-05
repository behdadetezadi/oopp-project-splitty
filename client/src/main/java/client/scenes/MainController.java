package client.scenes;

import commons.Event;
import commons.Expense;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

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

    private TableOfParticipantsController tableOfParticipantsController;
    private Scene tableOfParticipantsScene;

    private ContactDetailsCtrl contactDetailsController;
    private Scene contactDetailsScene;

    private InviteController inviteController;
    private Scene inviteScene;

    private AdminController adminController;
    private Scene adminScene;

    private LoginController loginController;
    private Scene loginScene;
    private static final String LANGUAGE_PREFERENCE_KEY = "language";

    /**
     * initializer method for mainController
     * @param primaryStage primary stage
     * @param startPair startPage pair
     * @param eventOverviewPair eventOverviewPage pair
     * @param expensePair expensePage pair
     * @param participantExpenseViewControllerPair participantExpenseViewController Pair
     * @param expenseOverviewControllerPair expenseOverviewScene Pair
     * @param tableOfParticipantsControllerPair table of participants page pair
     * @param contactDetailsControllerPair contactDetails page pair
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
                           Pair<ContactDetailsCtrl, Parent> contactDetailsControllerPair,
                           Pair<InviteController, Parent> inviteControllerPair,
                           Pair<LoginController, Parent> loginControllerPair,
                           Pair<AdminController, Parent> adminControllerPair)
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

        this.contactDetailsScene = new Scene(contactDetailsControllerPair.getValue());
        this.contactDetailsController = contactDetailsControllerPair.getKey();

        this.inviteScene = new Scene(inviteControllerPair.getValue());
        this.inviteController =  inviteControllerPair.getKey();

        this.adminScene = new Scene(adminControllerPair.getValue());
        this.adminController = adminControllerPair.getKey();

        this.loginScene = new Scene(loginControllerPair.getValue());
        this.loginController = loginControllerPair.getKey();

        // Show initial scene
        showLoginPage(getStoredLanguagePreferenceOrDefault());
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
    public void showLoginPage(Locale locale) {
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
    }

    /**
     * shows the AdminPage
     */
    public void showAdminPage() {
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(adminScene);
        adminController.initialize();
    }
    /**
     * shows the StartPage
     */
    public void showStartPage(Locale locale) {
        primaryStage.setTitle("Start Page");
        startPageController.clearTextFields();
        primaryStage.setScene(startScene);
        startPageController.initialize(locale);
    }


    /**
     *
     * @param event the event we are working on
     */
    public void showAddExpense(Event event, long participantId, Locale locale) {
        primaryStage.setTitle("Expenses: Add Expense");
        expenseCtrl.clearTextFields();
        primaryStage.setScene(expenseScene);
        expenseCtrl.setEvent(event, participantId, locale);
    }

    /**
     *
     * @param event the event we are working on
     */
    public void showTableOfParticipants(Event event, Locale locale) {
        primaryStage.setTitle("Participants");
        primaryStage.setScene(tableOfParticipantsScene);
        tableOfParticipantsController.setEvent(event, locale);
    }


    /**
     * @param event the event we are working on
     */
    public void showContactDetailsPage(Event event, Locale locale){
        primaryStage.setTitle("ContactDetails");
        primaryStage.setScene(contactDetailsScene);
        contactDetailsController.setEvent(event, locale);
    }


    //TODO it is not using initialize not sure if its ok

    /**
     *
     * @param event event
     */
    public void showInvitePage(Event event, Locale locale){
        primaryStage.setTitle("InvitePage");
        primaryStage.setScene(inviteScene);
        inviteController.initData(event, locale);
        //inviteController.initialize();
    }

    /**
     * Shows the event overview scene.
     * @param event The event to show overview for.
     */
    public void showEventOverview(Event event, Locale locale) {
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(eventOverviewScene);
        eventOverviewController.setEvent(event, locale);
        eventOverviewController.refreshParticipants();
    }
    /**
     * Shows the expense overview of the selected participant.
     * @param event The event to show overview for.
     */
    public void showParticipantExpensesOverview(Event event, Long participantId, Locale locale) {
        primaryStage.setTitle("Participant Expenses Overview");
        primaryStage.setScene(participantExpenseViewScene);
        participantExpenseViewController.setEvent(event,participantId, locale);
        participantExpenseViewController.initializeExpensesForParticipant(participantId);
    }

    public void showExpenseOverview(Event event, Locale locale)
    {
        primaryStage.setTitle("Expenses Overview");
        primaryStage.setScene(expenseOverviewScene);
        expenseOverviewController.setEvent(event, locale);
    }

}
