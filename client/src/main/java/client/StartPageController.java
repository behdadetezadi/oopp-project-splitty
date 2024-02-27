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
    private VBox centerVBox;

    @FXML
    private ImageView logo;

    public void initialize() {
        // Set fixed width for text fields
        codeInput.setPrefWidth(200);
        eventNameInput.setPrefWidth(200);

        // Set fixed width for buttons
        joinButton.setPrefWidth(150);
        createEventButton.setPrefWidth(150);



        /*
        Image gif = new Image(getClass().getClassLoader().getResourceAsStream("MatrixGif.gif"));
        logo.setImage(gif);

        // Create a fade out transition to gradually fade out the GIF
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(2), logo);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0);

        // Load and set the image after a short delay
        PauseTransition logoDelay = new PauseTransition(Duration.seconds(2)); // Adjust the delay as needed
        logoDelay.setOnFinished(event -> {
            Image image = new Image(getClass().getClassLoader().getResourceAsStream("SplittyLogo.png"));
            logo.setImage(image);
        });

        // Create a fade in transition to gradually fade in the image
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(2), logo);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        // Start the fade out, delay, and fade in transitions in parallel
        ParallelTransition transition = new ParallelTransition(fadeOutTransition, logoDelay, fadeInTransition);
        transition.play();
        */


        Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/MatrixGif.gif"));
        logo.setImage(image);

        // Apply CSS glow effect to the logo
        logo.getStyleClass().add("glow");


        animateTextFields();
        animateButtonsText();

        // Delay setting focus to prevent text field from being selected immediately
        PauseTransition delay = new PauseTransition(Duration.seconds(0.01));
        delay.setOnFinished(event -> {
            root.requestFocus();
        });
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

    private void animateText(Label label, String text) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.05));
        StringBuilder stringBuilder = new StringBuilder();
        int[] currentIndex = {0};
        pauseTransition.setOnFinished(event -> {
            if (currentIndex[0] < text.length()) {
                stringBuilder.append(text.charAt(currentIndex[0]));
                label.setText(stringBuilder.toString());
                currentIndex[0]++;
                pauseTransition.play();
            }
        });
        pauseTransition.play();
    }

    private void animateTextFields() {
        animateTextField(codeInput);
        animateTextField(eventNameInput);
    }

    private void animateTextField(TextField textField) {
        String text = textField.getPromptText();
        animateText(textField, text);
    }

    private void animateButtonsText() {
        animateButton(joinButton);
        animateButton(createEventButton);
    }

    private void animateButton(Button button) {
        String text = button.getText();
        animateText(button, text);
    }

    private void animateText(TextField textField, String text) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.05));
        StringBuilder stringBuilder = new StringBuilder();
        int[] currentIndex = {0};
        pauseTransition.setOnFinished(event -> {
            if (currentIndex[0] < text.length()) {
                stringBuilder.append(text.charAt(currentIndex[0]));
                textField.setPromptText(stringBuilder.toString());
                currentIndex[0]++;
                pauseTransition.play();
            }
        });
        pauseTransition.play();
    }

    private void animateText(Button button, String text) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.05));
        StringBuilder stringBuilder = new StringBuilder();
        int[] currentIndex = {0};
        pauseTransition.setOnFinished(event -> {
            if (currentIndex[0] < text.length()) {
                stringBuilder.append(text.charAt(currentIndex[0]));
                button.setText(stringBuilder.toString());
                currentIndex[0]++;
                pauseTransition.play();
            }
        });
        pauseTransition.play();
    }

    public void joinMeeting() {
        String code = codeInput.getText();
        // Logic to join meeting with the given code
    }

    public void createEvent() {
        String eventName = eventNameInput.getText();
        // Logic to create a new event with the given name
    }
}
