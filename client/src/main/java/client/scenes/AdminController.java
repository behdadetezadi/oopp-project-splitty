package client.scenes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
     * method that exports given event as json
     * @param event button press
     */
    @FXML
    public void exportEvent(Event event) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File( event.getTitle() + ".json"), event);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * TODO Logic for maybe showing a pop-up to fill in the import JSON
     * @param actionEvent button press
     */
    @FXML
    public void importEvent(ActionEvent actionEvent) {
        ObjectMapper objectMapper = new ObjectMapper();


        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Please enter file path: "));
        TextArea textArea = new TextArea();
        dialogVbox.getChildren().add(textArea);
        Button button = new Button("submit");
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String filepath = textArea.getText();
                        System.out.println(filepath);

                        try {
                            JsonNode jsonNode = objectMapper.readTree(new File(filepath));

                            try {
                                String title = jsonNode.get("title").asText();
                                long id = jsonNode.get("id").asLong();
                                int inviteCode = jsonNode.get("inviteCode").asInt();
                                Event e = new Event(title);
                                e.setId(id);
                                e.setInviteCode(inviteCode);

                            } catch (Exception e) {
                                System.out.println("could not find correct attributes ");
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        
        dialogVbox.getChildren().add(button);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();



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
