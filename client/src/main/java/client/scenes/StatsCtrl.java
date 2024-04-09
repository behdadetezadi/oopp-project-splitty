package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.*;

public class StatsCtrl {

    @FXML
    private PieChart pieChart;
    @FXML
    private Label cost;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private VBox chartLegend;
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
//        pieChart.setVisible(false);
//        this.resourceBundle = resourceBundle;
        fillPieChart(event.getId());
    }

    private void fillPieChart(Long eventId) {
        pieChart.getData().clear();

        Set<Expense> expenses = new HashSet<>(ServerUtils.getExpensesForEvent(eventId));
        HashMap<String, Double> tagAndExpense = tagExpense(expenses);
        tagAndExpense.forEach((tag, amount) -> {
            PieChart.Data slice = new PieChart.Data(tag, amount);
            pieChart.getData().add(slice);
//            colorSlice(slice, "color");
        });
        totalCost = (tagAndExpense.values().stream().mapToDouble(Double::doubleValue).sum());
        cost.setText("$"+totalCost);


        pieChart.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", data.pieValueProperty().getValue(),
                                " (", data.pieValueProperty().getValue() * 100 / totalCost, "%)")));
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        createCustomLegend(tagAndExpense);

        pieChart.setVisible(true);
    }

    private String translateTag(String tag) {
        String displayName = tag;
        if ("food".equals(tag) || "travel".equals(tag) || "entrance_fees".equals(tag)) {
            displayName = resourceBundle.getString(tag);
        }
        return displayName;
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


    private void colorSlice(PieChart.Data data, String color) {
        Node slice = data.getNode();
        slice.setStyle("-fx-pie-color: " + color.toLowerCase() + ";");
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

    private void createCustomLegend(HashMap<String, Double> tagAndExpense) {
        chartLegend.getChildren().clear();
        tagAndExpense.forEach((tag, amount) -> {
            HBox legendItem = new HBox(10);
            legendItem.setAlignment(Pos.CENTER_LEFT);
            Circle colorCircle = new Circle(8);
            colorCircle.setFill(Color.web("gray"));
            Label label = null;
            label = new Label(tag + ": " +
                    numberFormat.format(amount) +
                    " (" + numberFormat.format(amount * 100 / totalCost) + "%)");

            legendItem.getChildren().addAll(colorCircle, label);
            chartLegend.getChildren().add(legendItem);
        });

    }

    @FXML
    private void switchToExpenseOverviewScene() {
        mainController.showExpenseOverview(event, activeLocale);
    }
}
