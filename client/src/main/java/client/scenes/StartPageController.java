package client.scenes;

import client.Language;
import client.utils.LanguageChangeListener;
import client.utils.LanguageUtils;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import static client.utils.AnimationUtil.*;

public class StartPageController implements LanguageChangeListener {
    @FXML
    private BorderPane root;
    @FXML
    private TextField codeInput;
    @FXML
    private TextField eventNameInput;
    @FXML
    private Label recentEventsLabel;
    @FXML
    private ListView<Event> recentEventsList;
    @FXML
    private Button joinButton;
    @FXML
    private Button createEventButton;
    @FXML
    private ImageView logo;

    @FXML
    private ComboBox<Language> languageComboBox;
    private ResourceBundle resourceBundle;
    private Locale activeLocale;

    private Stage primaryStage;
    private MainController mainController;
    private ObservableList<Event> events = FXCollections.observableArrayList();

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
     * initialize method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);
        // Populates the language combo box
        LanguageUtils.configureLanguageComboBox(languageComboBox, this);

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
        animateText(recentEventsLabel, resourceBundle.getString("recent_events"));

        recentEventsList.setCellFactory(listView -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event selectedEvent, boolean empty) {
                super.updateItem(selectedEvent, empty);
                if (empty || selectedEvent == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create the cell content as before
                    Label eventNameLabel = new Label(selectedEvent.getTitle());
                    // Create button and handle actions
                    Button removeButton = new Button();
                    removeButton.setStyle("-fx-background-color: transparent;");
                    ImageView removeImage = new ImageView("images/closeIcon.png");
                    removeImage.setFitWidth(16);
                    removeImage.setFitHeight(16);
                    removeButton.setGraphic(removeImage);
                    removeButton.setOnAction(removeEvent -> {
                        events.remove(selectedEvent); // Remove event from the list
                    });
                    HBox buttonBox = new HBox(removeButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);
                    VBox cellBox = new VBox(buttonBox, eventNameLabel);
                    cellBox.setSpacing(10);
                    cellBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(cellBox);
                    setText(null);

                    // Add event listener to the cell
                    setOnMouseClicked(mouseEvent -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1) {
                                // Call method to switch to event overview scene
                                mainController.showEventOverview(selectedEvent);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * sets the resource bundle
     * @param resourceBundle The resource bundle to set.
     */
    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * sets the active locale
     * @param locale The new locale to set as active.
     */
    @Override
    public void setActiveLocale(Locale locale) {
        this.activeLocale = locale;
    }
    private void adjustComponentSizes() {
        // Adjust the width of text fields and buttons based on the new language
        adjustTextFieldWidth(codeInput, codeInput.getPromptText());
        adjustTextFieldWidth(eventNameInput, eventNameInput.getPromptText());
        adjustButtonWidth(joinButton, joinButton.getText());
        adjustButtonWidth(createEventButton, createEventButton.getText());
    }
    private void adjustTextFieldWidth(TextField textField, String text) {
        // Calculate the required width based on the text in the new language
        double textWidth = computeTextWidth(text, textField.getFont());
        double prefWidth = textWidth + 30; // Add some padding
        textField.setPrefWidth(prefWidth);
    }

    /**
     * gets the main controller
     * @return main controller
     */
    @Override
    public MainController getMainController() {
        return mainController;
    }

    /**
     * updates the UI elements with the selected language
     */
    public void updateUIElements() {
        codeInput.setPromptText(resourceBundle.getString("enter_code"));
        eventNameInput.setPromptText(resourceBundle.getString("enter_event_name"));
        joinButton.setText(resourceBundle.getString("join_meeting"));
        createEventButton.setText(resourceBundle.getString("create_event"));
        recentEventsLabel.setText(resourceBundle.getString("recent_events"));
    }

    /**
     * Set the language combo box
     */
    public void setLanguageComboBox() {
        String languageName = LanguageUtils.localeToLanguageName(activeLocale);
        languageComboBox.setValue(languageName);
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
        try {
            long codeAsLong = Long.parseLong(inviteCode);

            Event event = ServerUtils.getEventByInviteCode(inviteCode);

            if (event != null) {
                events.add(event); // Add the created event to the list
                recentEventsList.setItems(events);
                mainController.showEventOverview(event); // Switch to event overview page
            } else {
                showErrorAlert(resourceBundle.getString("Invalid_invite_code._Please_try_again."));
            }
        } catch (NumberFormatException e) {
            showErrorAlert(resourceBundle.getString("Invalid_invite_code._Please_try_again."));
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
            showErrorAlert(resourceBundle.getString("Event_name_cannot_be_empty."));
            return;
        }

        Event newEvent = new Event(eventName);

        Event createdEvent = ServerUtils.addEvent(newEvent);

        if (createdEvent != null) {
            events.add(createdEvent); // Add the created event to the list
            recentEventsList.setItems(events);
            mainController.showEventOverview(createdEvent);
        } else {
            showErrorAlert(resourceBundle.getString("Failed_to_create_event._Please_try_again."));
        }
    }

    /**
     * goes back to the login page
     */
    public void logout() {
        try {
            mainController.showLoginPage();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
