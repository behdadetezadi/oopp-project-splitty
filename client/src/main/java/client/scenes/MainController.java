package client.scenes;

import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

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

    private TableOfParticipantsController tableOfParticipantsController;
    private Scene tableOfParticipantsScene;

    private ContactDetailsCtrl contactDetailsController;
    private Scene contactDetailsScene;

    private InviteController inviteController;
    private Scene inviteScene;

    /**
     * initializer method for mainController
     * @param primaryStage primary stage
     * @param startPair startPage pair
     * @param eventOverviewPair eventOverviewPage pair
     * @param expensePair expensePage pair
     * @param participantExpenseViewControllerPair participantExpenseViewController Pair
     * @param tableOfParticipantsControllerPair table of participants page pair
     * @param contactDetailsControllerPair contactDetails page pair
     * @param inviteControllerPair invitePage pair
     */
    public void initialize(Stage primaryStage,
                           Pair<StartPageController, Parent> startPair,
                           Pair<EventOverviewController, Parent> eventOverviewPair,
                           Pair<AddExpenseController, Parent> expensePair,
                           Pair<ParticipantExpenseViewController, Parent> participantExpenseViewControllerPair,
                           Pair<TableOfParticipantsController, Parent> tableOfParticipantsControllerPair,
                           Pair<ContactDetailsCtrl, Parent> contactDetailsControllerPair,
                           Pair<InviteController, Parent> inviteControllerPair)
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

        this.tableOfParticipantsScene = new Scene(tableOfParticipantsControllerPair.getValue());
        this.tableOfParticipantsController = tableOfParticipantsControllerPair.getKey();

        this.contactDetailsScene = new Scene(contactDetailsControllerPair.getValue());
        this.contactDetailsController = contactDetailsControllerPair.getKey();

        this.inviteScene = new Scene(inviteControllerPair.getValue());
        this.inviteController =  inviteControllerPair.getKey();

        // Show initial scene
        showStartPage();
        primaryStage.show();
    }

    /**
     * shows the StartPage
     */
    public void showStartPage() {
        primaryStage.setTitle("Start Page");
        startPageController.clearTextFields();
        primaryStage.setScene(startScene);
        startPageController.initialize();
    }


    /**
     *
     * @param event the event we are working on
     */
    public void showAddExpense(Event event, long participantId) {
        primaryStage.setTitle("Expenses: Add Expense");
        primaryStage.setScene(expenseScene);
        expenseCtrl.setEvent(event, participantId);
    }

    /**
     *
     * @param event the event we are working on
     */
    public void showTableOfParticipants(Event event) {
        primaryStage.setTitle("Participants");
        primaryStage.setScene(tableOfParticipantsScene);
        tableOfParticipantsController.setEvent(event);
    }


    /**
     * @param event the event we are working on
     */
    public void showContactDetailsPage(Event event){
        primaryStage.setTitle("ContactDetails");
        primaryStage.setScene(contactDetailsScene);
        contactDetailsController.setEvent(event);
    }


    //TODO it is not using initialize not sure if its ok

    /**
     *
     * @param event event
     */
    public void showInvitePage(Event event){
        primaryStage.setTitle("InvitePage");
        primaryStage.setScene(inviteScene);
        inviteController.initData(event);
        //inviteController.initialize();
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

    }
    /**
     * Shows the expense overview of the selected participant.
     * @param event The event to show overview for.
     */
    public void showParticipantExpensesOverview(Event event, Long participantId) {
        primaryStage.setTitle("Participant Expenses Overview");
        primaryStage.setScene(participantExpenseViewScene);
        participantExpenseViewController.setEvent(event,participantId);
        participantExpenseViewController.initializeExpensesForParticipant(participantId);
    }

}
