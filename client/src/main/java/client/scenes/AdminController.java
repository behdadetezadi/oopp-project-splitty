package client.scenes;

import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    @FXML
    private TableView<Event> eventsTable;
    @FXML
    private TableColumn<Event, String> titleColumn;
    @FXML
    private TableColumn<Event, LocalDate> creationDateColumn;
    @FXML
    private TableColumn<Event, LocalDateTime> lastActivityColumn;
    @FXML
    private TableColumn<Event, Void> actionsColumn;
    private ObservableList<Event> eventData = FXCollections.observableArrayList();

    /**
     * Initializer method
     */
    @FXML
    public void initialize() {
        List<Event> dummyEvents = createDummyEvents(15); // TODO only for testing
        eventData.addAll(dummyEvents); // TODO only for testing

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        lastActivityColumn.setCellValueFactory(new PropertyValueFactory<>("lastActivity"));

        setupActionsColumn();
        eventsTable.setItems(eventData);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button exportButton = new Button("Export");
            private final HBox container = new HBox(10, deleteButton, exportButton);

            {
                deleteButton.getStyleClass().add("button-delete");
                exportButton.getStyleClass().add("button-export");

                container.setAlignment(Pos.CENTER);

                container.setPadding(new Insets(5, 0, 5, 0));

                deleteButton.setOnAction(event -> {
                    commons.Event eventData = getTableView().getItems().get(getIndex());
                    deleteEvent(eventData);
                });
                exportButton.setOnAction(event -> {
                    commons.Event eventData = getTableView().getItems().get(getIndex());
                    exportEvent(eventData);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    /**
     * TODO Logic to delete the event
     * @param event button press
     */
    @FXML
    private void deleteEvent(Event event) {

    }

    /**
     * TODO Logic to export the event data as JSON
     * @param event button press
     */
    @FXML
    private void exportEvent(Event event) {

    }

    /**
     * TODO Logic for maybe showing a pop-up to fill in the import JSON
     * @param actionEvent button press
     */
    @FXML
    public void importEvent(ActionEvent actionEvent) {

    }

    /**
     * TODO only for testing
     * @param count nr of events
     * @return list
     */
    @FXML
    public List<Event> createDummyEvents(int count) {
        List<Event> events = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            events.add(new Event("Event " + i));
        }

        return events;
    }
}
