package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.*;

public class StatsCtrl {

    @FXML
    private PieChart pieChart;
    @FXML
    private Label cost;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    private MainController mainController;
    private Event event;
    private final Map<PieChart.Data, Tooltip> sliceTooltips = new HashMap<>();
    private final DecimalFormat numberFormat = new DecimalFormat("#.00");
    private Double totalCost;
    private Stage primaryStage;
    private ServerUtils server;

    @Inject
    public StatsCtrl(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    public void initialize() {
        fillPieChart(event.getId());
        addEventHandlersToSlices();
    }

    private void fillPieChart(Long eventId) {
        pieChart.getData().clear();

        Set<Expense> expenses = new HashSet<>(ServerUtils.getExpensesForEvent(eventId));
        HashMap<String, Double> tagAndExpense = tagExpense(expenses);
        tagAndExpense.forEach((tag, amount) -> {
            PieChart.Data slice = new PieChart.Data(tag, amount);
            pieChart.getData().add(slice);
        });
        totalCost = (tagAndExpense.values().stream().mapToDouble(Double::doubleValue).sum());
        cost.setText("Total cost: $"+numberFormat.format(totalCost));

        pieChart.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", data.pieValueProperty().getValue(),
                                " (", numberFormat.format(data.pieValueProperty().getValue() * 100 / totalCost), "%)")));

    }

    private HashMap<String, Double> tagExpense(Set<Expense> expenses) {
        HashMap<String, Double> tagAndExpense = new HashMap<>();
        for (Expense expense : expenses) {
            String tag = expense.getExpenseType();
            if (tag != null) {
                tagAndExpense.put(tag, tagAndExpense.getOrDefault(tag, 0.0) + expense.getAmount());
            } else {
                tagAndExpense.put("other", tagAndExpense.getOrDefault("other", 0.0) + expense.getAmount());
            }
        }
        return tagAndExpense;
    }

    /**
     * called by mainController
     * @param event event
     */
    public void setEvent(Event event, Locale locale) {
        this.activeLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle("message", locale);
        this.event = event;
        initialize();
    }

    private void handleSliceClick(MouseEvent event) {
        Node node = (Node) event.getSource();
        PieChart.Data data = (PieChart.Data) node.getUserData();
        if (sliceTooltips.containsKey(data)) {
            Tooltip currentTooltip = sliceTooltips.get(data);
            currentTooltip.hide();
            sliceTooltips.remove(data);
        } else {
            Tooltip newTooltip = new Tooltip(data.getName());
            newTooltip.show(node, event.getScreenX(), event.getScreenY() - 10);
            sliceTooltips.put(data, newTooltip);
        }
    }

    /**
     * Adds event handlers (mouse click) to the slices.
     */
    public void addEventHandlersToSlices() {
        pieChart.getData().forEach(slice -> {
            slice.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleSliceClick);
            slice.getNode().setUserData(slice);
        });
    }
    @FXML
    private void switchToExpenseOverviewScene() {
        mainController.showExpenseOverview(event, activeLocale);
    }
}
