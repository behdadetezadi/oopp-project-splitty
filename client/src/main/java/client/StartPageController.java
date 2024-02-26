package client;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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

    public void initialize() {
        animateTextFields();
        animateButtonsText();

        // Delay setting focus to prevent text field from being selected immediately
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
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
