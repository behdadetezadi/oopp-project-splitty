package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * This is the main controller which controls the switching between scenes
 */
public class MainController {
    private Stage primaryStage;
    private ExpenseController expenseCtrl;
    private Scene expenseScene;

    /**
     * Standard initializer method for our window
     * @param primaryStage the main stage
     * @param overview Pair<QuoteOverviewCtrl, Parent>
     * @param add Pair<AddQuoteCtrl, Parent>
     * @param expense Pair<ExpenseController, Parent> for the expense scene
     */
    public void initialize(Stage primaryStage,
                           Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<ExpenseController, Parent> expense) {

        this.primaryStage = primaryStage;
        this.expenseCtrl = expense.getKey();
        this.expenseScene = new Scene(expense.getValue());

        // Show initial scene
        showOverview();
        primaryStage.show();
    }

    private void showOverview() {
        primaryStage.setTitle("Overview");
        primaryStage.setScene(expenseScene);
        expenseCtrl.initialize();
    }

    /**
     * Shows the expense adding scene.
     */
    public void showAddExpense() {
        primaryStage.setTitle("Expenses: Add Expense");
        primaryStage.setScene(expenseScene);
        expenseCtrl.initialize();
    }
}
