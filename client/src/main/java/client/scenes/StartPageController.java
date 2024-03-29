package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.inject.Inject;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

import static client.utils.AnimationUtil.*;

public class StartPageController {

    @FXML
    private BorderPane root;

    @FXML
    private TextField codeInput;

    @FXML
    private TextField eventNameInput;

    @FXML
    private Label recentEventsLabel;

    @FXML
    private ListView<String> recentEventsList;

    @FXML
    private Button joinButton;

    @FXML
    private Button createEventButton;

    @FXML
    private ImageView logo;

    private Stage primaryStage;
    private MainController mainController;

    /**
     *
     * @param primaryStage primary stage
     * @param mainController mainController
     */
    @Inject
    public StartPageController(Stage primaryStage, MainController mainController) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;
    }




    /**
     * initializer method //TODO
     */

    private static final ObservableList<String> EVENT_TITLES = FXCollections.observableArrayList(
            "Conference",
            "Workshop",
            "Seminar",
            "Hackathon",
            "Webinar"
    );

    /**
     * initialize method of the start page controller
     */
    public void initialize() {

        // Set fixed width for text fields
        codeInput.setPrefWidth(200);
        eventNameInput.setPrefWidth(200);
        // Set fixed width for buttons
        joinButton.setPrefWidth(150);
        createEventButton.setPrefWidth(150);

        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("images/MatrixGif.gif")));
        logo.setImage(image);

        animateTextFields();
        createEventButton.getStyleClass().add("button-hover");
        joinButton.getStyleClass().add("button-hover");

        animateButtonsText();
        // Delay setting focus to prevent text field from being selected immediately
        PauseTransition delay = new PauseTransition(Duration.seconds(0.01));
        delay.setOnFinished(event -> root.requestFocus());
        delay.play();

        // Dynamically adjust component sizes based on the parent container size
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            codeInput.setMaxWidth(width * 0.5);
            eventNameInput.setMaxWidth(width * 0.5);
            recentEventsList.setPrefWidth(width * 0.8);
        });
        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            double height = newVal.doubleValue();
            recentEventsList.setPrefHeight(height * 0.6);
        });



        // Add margin to the createEventButton
        VBox.setMargin(createEventButton, new Insets(10, 0, 0, 0));
        // Increase font size and weight of the recent events label
        recentEventsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        // Add some spacing between the label and the list view
        VBox.setMargin(recentEventsLabel, new Insets(0, 0, 10, 0)); // Adjust insets as needed
        // Ensure the label expands to fill available space horizontally
        HBox.setHgrow(recentEventsLabel, Priority.ALWAYS);
        animateText(recentEventsLabel, "Recent Events");

        recentEventsList.setItems(EVENT_TITLES);


        // Set the cell factory for the recentEventsList
        recentEventsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create the cell content as before
                    Label eventNameLabel = new Label(item);
                    // Create button and handle actions
                    Button removeButton = new Button();
                    removeButton.setStyle("-fx-background-color: transparent;");
                    ImageView removeImage = new ImageView("images/closeIcon.png");
                    removeImage.setFitWidth(16);
                    removeImage.setFitHeight(16);
                    removeButton.setGraphic(removeImage);
                    removeButton.setOnAction(event -> {
                        getListView().getItems().remove(item);
                    });
                    HBox buttonBox = new HBox(removeButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);
                    VBox cellBox = new VBox(eventNameLabel, buttonBox);
                    cellBox.setSpacing(10);
                    cellBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(cellBox);
                    setText(null);
                }
            }
        });


    }


    /**
     * clears the textfield when you go to this page
     */
    public void clearTextFields() {
        codeInput.clear();
        eventNameInput.clear();
    }



    private void animateTextFields() {
        animateTextField(codeInput);
        animateTextField(eventNameInput);
    }


    private void animateButtonsText() {
        animateButton(joinButton);
        animateButton(createEventButton);
    }

    /**
     * join Meeting //TODO
     */
    public void joinMeeting() {
        String inviteCode = codeInput.getText();
        // Call method to send invite code to server and retrieve event details
        Event event = ServerUtils.getEventByInviteCode(inviteCode);

        if (event != null) {
            mainController.showEventOverview(event); // Switch to event overview page
        } else {
            showErrorAlert("Invalid invite code. Please try again.");
        }
    }

    /**
     *
     * @param message message of the error
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * create an Event //TODO
     */
    public void createEvent() {
        String eventName = eventNameInput.getText();
        if (eventName.isEmpty()) {
            showErrorAlert("Event name cannot be empty.");
            return;
        }

        Event newEvent = new Event(eventName);

        Event createdEvent = ServerUtils.addEvent(newEvent);

        if (createdEvent != null) {
            mainController.showEventOverview(createdEvent);
        } else {
            showErrorAlert("Failed to create event. Please try again.");
        }
    }
}
