package client.scenes;

import client.utils.LanguageChangeListener;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.*;

public class StatsCtrl implements LanguageChangeListener {

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
    @FXML
    private Button backButton;

    /**
     * constructor
     * @param primaryStage stage
     * @param server server
     * @param mainController controller
     * @param event event
     */
    @Inject
    public StatsCtrl(Stage primaryStage, ServerUtils server, MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    /**
     * Initialises the UI components and event handlers if an event is provided.
     * This method is called automatically by JavaFX after loading the FXML file.
     */
    public void initialize(Event event) {
        this.event = event;
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);
        fillPieChart(event.getId());
        addEventHandlersToSlices();
    }

    /**
     * this method fills up the pie chart
     * @param eventId event id
     */
    private void fillPieChart(Long eventId) {
        pieChart.getData().clear();

        Set<Expense> expenses = new HashSet<>(ServerUtils.getExpensesForEvent(eventId));
        HashMap<String, Double> tagAndExpense = tagWithExpense(expenses);
        tagAndExpense.forEach((tag, amount) -> {
            PieChart.Data slice = new PieChart.Data(tag, amount);
            pieChart.getData().add(slice);
        });
        totalCost = (tagAndExpense.values().stream().mapToDouble(Double::doubleValue).sum());
        cost.setText(resourceBundle.getString("totalCost")+numberFormat.format(totalCost));

        pieChart.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", numberFormat.format(data.pieValueProperty().getValue()),
                                " (", numberFormat.format(data.pieValueProperty().getValue() * 100 / totalCost), "%)")));

        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
    }

    /**
     * this method sorts all the expenses per tag in a hashmap and also stores the total amount spent for each tag
     * @param expenses the expenses
     * @return a hashmap
     */
    private HashMap<String, Double> tagWithExpense(Set<Expense> expenses) {
        HashMap<String, Double> tagAndExpense = new HashMap<>();
        for (Expense expense : expenses) {
            String tag = resourceBundle.getString("tag"+expense.getExpenseType());
            if (tag != null) {
                tagAndExpense.put(tag, tagAndExpense.getOrDefault(tag, 0.0) + expense.getAmount());
            } else {
                tagAndExpense.put("other", tagAndExpense.getOrDefault("other", 0.0) + expense.getAmount());
            }
        }
        return tagAndExpense;
    }


    /**
     * makes the pie chart clickable
     * @param event mouse event
     */
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

    /**
     * switch to expenseOverview
     */
    @FXML
    private void switchToExpenseOverviewScene() {
        mainController.showExpenseOverview(event);
    }

    /**
     * Sets the resource bundle for the current locale.
     *
     * @param resourceBundle The resource bundle to set.
     */
    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Updates the active locale for the controller.
     *
     * @param locale The new locale to set as active.
     */
    @Override
    public void setActiveLocale(Locale locale) {
        this.activeLocale = locale;
    }

    /**
     * Updates the UI elements to reflect changes in the language.
     */
    @Override
    public void updateUIElements() {
        backButton.setText(resourceBundle.getString("back"));
        pieChart.setTitle(resourceBundle.getString("Pie_Chart"));
    }

    /**
     * Retrieves the main controller associated with this listener.
     *
     * @return The main controller.
     */
    @Override
    public MainController getMainController() {
        return mainController;
    }
}
