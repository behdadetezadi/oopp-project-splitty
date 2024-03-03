package client;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

    /**
     * initializer method //TODO
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
        String code = codeInput.getText();
        // Logic to join meeting with the given code
    }

    /**
     * create an Event //TODO
     */
    public void createEvent() {
        String eventName = eventNameInput.getText();
        // Logic to create a new event with the given name
    }
}
