package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

        }
        sumOfExpensesLabel.setText("Total: $" + String.format("%.2f", sumOfExpenses));
        expensesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
                            Expense expense = getExpenseFromListView(getIndex(), participantId);
                            Button editButton = new Button("Edit");
                            Button deleteButton = new Button("Delete");
                            editButton.setOnAction(event -> System.out.println("Test1"));
                            deleteButton.setOnAction(event -> handleDeleteButton(expense));
                            setGraphic(new HBox(editButton, deleteButton));
                        }
                    }
                };
            }
        });
    }

    private Expense getExpenseFromListView(int index, long participantId) {
        List<Expense> expenses = ServerUtils.getExpensesForParticipant(participantId);
        if (index >= 0 && index < expenses.size()) {
            return expenses.get(index);
        } else {
            return null;
        }
    }

    @FXML
    private void switchToEventOverviewScene() {
        mainController.showEventOverview(this.event);
    }

    /**
     * deletes current expense
     */
    @FXML
    private void handleDeleteButton(Expense expense) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to remove this expense? This action cannot be undone"
                , ButtonType.YES, ButtonType.NO);
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                ServerUtils.deleteExpense(expense.getId());
            }
        });
    }

}

