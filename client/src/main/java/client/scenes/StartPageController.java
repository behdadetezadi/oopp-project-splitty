package client.scenes;

import client.Language;
import client.utils.AnimationUtil;
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
import javafx.application.Platform;

import java.util.*;

import static client.utils.AnimationUtil.*;

public class StartPageController implements LanguageChangeListener {
    @FXML
    private Button logoutButton;
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

        adjustComponentSizes();

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

        recentEventsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Event selectedEvent, boolean empty) {
                super.updateItem(selectedEvent, empty);
                if (empty || selectedEvent == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                // Create the event name label
                Label eventNameLabel = new Label(selectedEvent.getTitle());
                eventNameLabel.setStyle("-fx-alignment: center-left;");

                // Create the remove button with an icon
                Button removeButton = new Button();
                removeButton.setStyle("-fx-background-color: transparent;");
                ImageView removeImage = new ImageView("images/closeIcon.png");
                removeImage.setFitWidth(16);
                removeImage.setFitHeight(16);
                removeButton.setGraphic(removeImage);

                // Remove the event from the list when remove button is pressed
                removeButton.setOnAction(removeEvent -> {
                    events.remove(selectedEvent);
                });

                // HBox with label and the button
                HBox cellBox = new HBox(eventNameLabel, removeButton);
                cellBox.setSpacing(10);
                cellBox.setAlignment(Pos.CENTER_LEFT);

                // Put the button to the right
                HBox.setHgrow(eventNameLabel, Priority.ALWAYS);
                eventNameLabel.setMaxWidth(Double.MAX_VALUE);

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
        AnimationUtil.animateText(logoutButton, resourceBundle.getString("Logout"));
        AnimationUtil.animateText(codeInput, resourceBundle.getString("enter_code"));
        AnimationUtil.animateText(eventNameInput, resourceBundle.getString("enter_event_name"));
        AnimationUtil.animateText(joinButton, resourceBundle.getString("join_event"));
        AnimationUtil.animateText(createEventButton, resourceBundle.getString("create_event"));
        AnimationUtil.animateText(recentEventsLabel, resourceBundle.getString("recent_events"));
        adjustComponentSizes();
    }

    /**
     * Set the language combo box
     */
    public void setLanguageComboBox() {
        String languageName = LanguageUtils.localeToLanguageName(activeLocale);
        List<Language> languages = new ArrayList<>();
        languages.add(new Language("English",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/english.png")))));
        languages.add(new Language("Deutsch",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/german.png")))));
        languages.add(new Language("Nederlands",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/dutch.png")))));
        for (Language language : languages) {
            if (language.getName().equals(languageName)) {
                languageComboBox.setValue(language);
                break;
            }
        }
        languageComboBox.setItems(FXCollections.observableArrayList(languages));
        languageComboBox.setCellFactory(listView -> new StartPageController.LanguageListCell());
        languageComboBox.setButtonCell(new StartPageController.LanguageListCell());
    }

    /**
     * clears the textfield when you go to this page
     */
    public void clearTextFields() {
        codeInput.clear();
        eventNameInput.clear();
    }

    private void animateTextFields() {
        animateTextField(codeInput, codeInput.getText());
        animateTextField(eventNameInput, eventNameInput.getText());
    }

    private void animateButtonsText() {
        animateButton(joinButton);
        animateButton(createEventButton);
    }

    /**
     * join Meeting
     */
    public void joinEvent() {
        String inviteCode = codeInput.getText();
        try {
            Event event = ServerUtils.getEventByInviteCode(inviteCode);

            if (event != null) {
                events.add(event);
                recentEventsList.setItems(events);
                mainController.showEventOverview(event);
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
     * create an Event
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
     * refreshes the events list to make sure deleted events are not included
     */

    public void refreshEventsList() {
        new Thread(() -> {
            List<Event> serverEvents = ServerUtils.getAllEvents();
            Platform.runLater(() -> {
                events.removeIf(event -> !serverEvents.contains(event));
            });
        }).start();
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

    private class LanguageListCell extends ListCell<Language> {
        @Override
        protected void updateItem(Language item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getName());
                ImageView imageView = new ImageView(item.getFlag());
                imageView.setFitHeight(10);
                imageView.setFitWidth(20);
                setGraphic(imageView);
            }
        }
    }

    /**
     * Adjusts buttons based on language
     */
    public void adjustComponentSizes() {
        // Calculate the maximum preferred width for the text fields
        double maxTextFieldWidth = Math.max(
                computePrefWidth(codeInput, resourceBundle.getString("enter_code")),
                computePrefWidth(eventNameInput, resourceBundle.getString("enter_event_name"))
        );

        // Calculate the maximum preferred width for the buttons
        double maxButtonWidth = Math.max(
                computePrefWidth(joinButton, resourceBundle.getString("join_event")),
                computePrefWidth(createEventButton, resourceBundle.getString("create_event"))
        );

        // Set the calculated maximum width to the text fields
        codeInput.setPrefWidth(maxTextFieldWidth);
        eventNameInput.setPrefWidth(maxTextFieldWidth);

        // Set the calculated maximum width to the buttons
        joinButton.setPrefWidth(maxButtonWidth);
        createEventButton.setPrefWidth(maxButtonWidth);
    }

    private double computePrefWidth(Control control, String text) {
        Font font;
        if (control instanceof TextField) {
            font = ((TextField) control).getFont();
        } else if (control instanceof Button) {
            font = ((Button) control).getFont();
        } else {
            font = Font.getDefault();
        }
        return computeTextWidth(text, font) + 30;
    }

    private double computeTextWidth(String text, Font font) {
        Text textNode = new Text(text);
        textNode.setFont(font);
        return textNode.getBoundsInLocal().getWidth();
    }
}
