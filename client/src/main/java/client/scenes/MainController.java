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


    public void initialize(Stage primaryStage,
                           Pair<StartPageController, Parent> startPair,
                           Pair<EventOverviewController, Parent> eventOverviewPair,
                           Pair<ExpenseController, Parent> expense) {

        this.primaryStage = primaryStage;

        this.startScene = new Scene(startPair.getValue());
        this.eventOverviewScene = new Scene(eventOverviewPair.getValue());
        this.expenseScene = new Scene(expense.getValue());

        this.startPageController = startPair.getKey();
        this.eventOverviewController = eventOverviewPair.getKey();
        this.expenseCtrl = expense.getKey();

        // Show initial scene
        showStartPage();
        primaryStage.show();
    }

    private void showStartPage() {
        primaryStage.setTitle("Start Page");
        primaryStage.setScene(startScene);
        startPageController.initialize();
    }

    /*
    private void showOverview() {
        primaryStage.setTitle("Overview");
        primaryStage.setScene(expenseScene);
        expenseCtrl.initialize();
    }
    */
    /**
     * Shows the expense adding scene.
     */
    public void showAddExpense() {
        primaryStage.setTitle("Expenses: Add Expense");
        primaryStage.setScene(expenseScene);
        expenseCtrl.initialize();
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
