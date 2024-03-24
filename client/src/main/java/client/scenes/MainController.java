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

    private ExpenseController expenseCtrl;
    private Scene expenseScene;

    private TableOfParticipantsController tableOfParticipantsController;
    private Scene tableOfParticipantsScene;

    private ContactDetailsCtrl contactDetailsController;
    private Scene contactDetailsScene;

    private InviteController inviteController;
    private Scene inviteScene;


    public void initialize(Stage primaryStage,
                           Pair<StartPageController, Parent> startPair,
                           Pair<EventOverviewController, Parent> eventOverviewPair,
                           Pair<ExpenseController, Parent> expensePair,
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

    private void showStartPage() {
        primaryStage.setTitle("Start Page");
        primaryStage.setScene(startScene);
        startPageController.initialize();
    }


    /**
     *
     * @param event
     */
    public void showAddExpense(Event event) {
        primaryStage.setTitle("Expenses: Add Expense");
        primaryStage.setScene(expenseScene);
        expenseCtrl.setEvent(event);
    }

    public void showTableOfParticipants(Event event) {
        primaryStage.setTitle("Participants");
        primaryStage.setScene(tableOfParticipantsScene);
        tableOfParticipantsController.setEvent(event);
    }



    public void showContactDetailsPage(Event event){
        primaryStage.setTitle("ContactDetails");
        primaryStage.setScene(contactDetailsScene);
        contactDetailsController.setEvent(event);
    }


    //TODO
    public void showInvitePage(){
        primaryStage.setTitle("InvitePage");
        primaryStage.setScene(inviteScene);
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
    }
}
