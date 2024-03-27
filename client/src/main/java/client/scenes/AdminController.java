package client.scenes;

import client.utils.AlertUtils;
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

import java.io.DataInput;
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


    /**
     * TODO: Does not work yet
     * method that updates list with new event
     * @param e the event to add to the list
     */
    @FXML
    public void update(Event e) {
        eventData.add(e);
        eventsTable.setItems(eventData);
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
            String userHome = System.getProperty("user.home");
            File outputFile = new File(userHome + "/Splitty/Exports/" + event.getTitle() + ".json");

            objectMapper.writeValue(outputFile, event);


        } catch (IOException e) {
            System.out.println(e.getMessage());
            AlertUtils.showInformationAlert("Error", "Could not write to file",
                    "Could not find path");


        }

    }

    /**
     * method that creates a pop-up where filepath to event can be inserted
     * @param actionEvent button press
     *
     */
    @FXML
    public void importEvent(ActionEvent actionEvent) {
        ObjectMapper objectMapper = new ObjectMapper();


        final Stage dialog = new Stage();

        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        Label label = new Label("Please enter file path: ");
        dialogVbox.getChildren().add(label);
        TextArea textArea = new TextArea();
        dialogVbox.getChildren().add(textArea);
        Button button = new Button("submit");
        Event e = new Event();
        final String[] title = {null};
        final long[] id = new long[1];
        final int[] inviteCode = new int[1];
        final Event[] eventArr = new Event[1];
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {



                    String filepath = textArea.getText();
                    filepath = filepath.strip();
                        System.out.println(filepath);

                        try {
                            JsonNode jsonNode = objectMapper.readTree(new File(filepath));

                            try {

                                Event e = objectMapper.readValue((DataInput) jsonNode, Event.class);
                                eventArr[0] = e;
                                AlertUtils.showInformationAlert("Success", "Event added!",
                                        "You can close the dialogue window.");




                            } catch (Exception e) {
                                System.out.println("could not find correct attributes ");
                                AlertUtils.showErrorAlert("Error", "Conversion failed",
                                        "Could not make an event from the given JSON file.");

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                            AlertUtils.showErrorAlert("Error", "File not found",
                                    "Could not find given JSON file");

                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        e.setTitle(title[0]);
        e.setId(id[0]);
        e.setInviteCode(inviteCode[0]);
        this.update(eventArr[0]);

        dialogVbox.getChildren().add(button);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialogScene.getStylesheets().add("styles.css");
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
