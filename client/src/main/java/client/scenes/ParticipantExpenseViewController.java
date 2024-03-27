package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


import java.util.List;


public class ParticipantExpenseViewController
{
    @FXML
    private ListView<String> expensesListView;
    @FXML
    private Label sumOfExpensesLabel;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;

    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController mainController
     * @param event Event
     */
    @Inject
    public ParticipantExpenseViewController(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    /** Format the expense information for display
     * @param expense the expense need to be displayed
     */
    private String formatExpenseForDisplay(Expense expense)
    {
        return String.format("%s - %s: %s %.2f",
                expense.getDate(),
                expense.getParticipant().getFirstName(),
                expense.getCurrency(),
                expense.getAmount());
    }

    /**
     * Integrates the viewing of expenses for a selected participant.
     *
     * @param participantId The ID of the participant whose expenses you want to view.
     */
    public void initializeExpensesForParticipant(Long participantId) {
        System.out.println("Initializing expenses for participant ID: " + participantId);
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
        System.out.println("Number of expenses fetched: " + expenses.size());

        expensesListView.getItems().clear();
        double sumOfExpenses = 0;
        for (Expense expense : expenses) {
            String expenseDisplay = formatExpenseForDisplay(expense);
            expensesListView.getItems().add(expenseDisplay);
            sumOfExpenses += expense.getAmount();

            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            editButton.setOnAction(event -> System.out.println("Test1"));
            //handleEditExpense(expense));
            deleteButton.setOnAction(event -> System.out.println("Test2"));
            //handleDeleteExpense(expense));
        }
        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
        expensesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() { //manage the cells in list
            // customise the cells
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        }else {
                            setText(item);
                            Button editButton = new Button("Edit");
                            Button deleteButton = new Button("Delete");
                            editButton.setOnAction(event -> System.out.println("Test1"));
                            deleteButton.setOnAction(event -> System.out.println("Test2"));
                            setGraphic(new HBox(editButton, deleteButton));
                        }
                    }
                };
            }
        });
    }


    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event);


    }
}

