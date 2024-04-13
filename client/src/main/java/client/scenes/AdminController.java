package client.scenes;

import client.utils.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableColumn;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;

public class AdminController implements LanguageChangeListener {
    @FXML
    private Button logoutButton;
    @FXML
    private Button importButton;
    @FXML
    private Button exportAllButton;
    @FXML
    private Button deleteAllButton;
    @FXML
    private TableView<Event> eventsTable;
    @FXML
    private TableColumn<Event, String> titleColumn;
    @FXML
    private TableColumn<Event, String> creationDateColumn;
    @FXML
    private TableColumn<Event, String> lastActivityColumn;
    @FXML
    private TableColumn<Event, Void> actionsColumn;
    private ObservableList<Event> eventData = FXCollections.observableArrayList();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    private StringProperty deleteButtonText = new SimpleStringProperty();
    private StringProperty exportButtonText = new SimpleStringProperty();
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;

    /**
     * admin Controller injection
     * @param primaryStage primary stage
     * @param server server
     * @param mainController maincontroller
     */
    @Inject
    public AdminController(Stage primaryStage,ServerUtils server, MainController mainController) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
    }


    /**
     * Initializer method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);

        fetchAndPopulateEvents();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Date formatting
        creationDateColumn.setCellValueFactory(event -> {
            LocalDateTime creationDateTime = event.getValue().getCreationDate();
            return new SimpleStringProperty(creationDateTime != null ? formatDateTime(creationDateTime) : "");
        });
        lastActivityColumn.setCellValueFactory(event -> {
            LocalDateTime lastActivityDateTime = event.getValue().getLastActivity();
            return new SimpleStringProperty(lastActivityDateTime != null ? formatDateTime(lastActivityDateTime) : "");
        });

        setupActionsColumn();
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
        AnimationUtil.animateText(importButton, resourceBundle.getString("Import"));
        AnimationUtil.animateText(exportAllButton, resourceBundle.getString("Export_All"));
        AnimationUtil.animateText(deleteAllButton, resourceBundle.getString("Delete_All"));
        titleColumn.setText(resourceBundle.getString("Title"));
        creationDateColumn.setText(resourceBundle.getString("Creation_Date"));
        lastActivityColumn.setText(resourceBundle.getString("Last_Activity"));
        deleteButtonText.set(resourceBundle.getString("delete"));
        exportButtonText.set(resourceBundle.getString("Export"));
    }

    private String formatDateTime(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        return date.equals(LocalDate.now()) ? "Today at " + time.format(timeFormatter) :
                date.format(dateFormatter) + " " + time.format(timeFormatter);
    }

    /**
     * fetches all events and puts them in the table
     */
    public void fetchAndPopulateEvents() {
        new Thread(() -> {
            List<Event> events = ServerUtils.getAllEvents();
            Platform.runLater(() -> {
                eventData.removeAll();
                eventsTable.getItems().clear();
                eventData.addAll(events);
                eventsTable.setItems(eventData);
            });
        }).start();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();
            private final Button exportButton = new Button();
            private final HBox container = new HBox(10, deleteButton, exportButton);

            {
                deleteButton.textProperty().bind(deleteButtonText);
                exportButton.textProperty().bind(exportButtonText);

                deleteButton.getStyleClass().add("button-delete");
                exportButton.getStyleClass().add("button-export");

                container.setAlignment(Pos.CENTER);
                container.setPadding(new Insets(5, 0, 5, 0));

                deleteButton.setOnAction(event -> {
                    Event eventData = getTableView().getItems().get(getIndex());
                    deleteEvent(eventData);
                });
                exportButton.setOnAction(event -> {
                    Event eventData = getTableView().getItems().get(getIndex());
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
     * Deletes an event
     * @param event button press
     */
    @FXML
    private void deleteEvent(Event event) {
        boolean userConfirmed = AlertUtils.showConfirmationAlert("Confirm Deletion",
                "Are you sure you want to delete this event?");

        if (!userConfirmed) {
            return;
        }

        new Thread(() -> {
            boolean success = ServerUtils.deleteEvent(event.getId());
            Platform.runLater(() -> {
                if (success) {
                    eventData.remove(event);
                    eventsTable.setItems(eventData);
                    AlertUtils.showInformationAlert("Success", "Event Deleted",
                            "The event has been successfully deleted.");
                } else {
                    AlertUtils.showErrorAlert("Error", "Deletion Failed",
                            "Failed to delete the event.");
                }
            });
        }).start();
    }


    /**
     * method that exports given event as json
     * @param event event tied to the button
     */
    @FXML
    public void exportEvent(Event event) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        String safeFileName = event.getTitle().replaceAll("[^a-zA-Z0-9.-]", "_");
        File outputFile = new File(System.getProperty("user.home") + File.separator + safeFileName + ".json");

        try {
            objectMapper.writeValue(outputFile, event);
            AlertUtils.showInformationAlert("Event Exported!", "Exported to:", outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            AlertUtils.showErrorAlert("Error", "Directory Not Found", "The specified directory does not exist.");
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Error", "Export Failed", "Failed to export the event. " + e.getMessage());
        }
    }

    /**
     * method that exports given event as json
     */
    @FXML
    public void exportAllEvents() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        File outputFile = new File(System.getProperty("user.home") + File.separator + "AllEvents.json");

        try {
            objectMapper.writeValue(outputFile, eventData);
            AlertUtils.showInformationAlert("All Events Exported!", "Exported to:", outputFile.getAbsolutePath());
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Error", "Export Failed", "Failed to export all events. " + e.getMessage());
        }
    }

    /**
     * method that creates a pop-up where filepath to event can be inserted
     *
     */
    @FXML
    public void importEvent() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        Label label = new Label("Please enter file path: ");
        dialogVbox.getChildren().add(label);
        TextField textField = new TextField();
        dialogVbox.getChildren().add(textField);
        Button button = new Button("Submit");
        button.setDefaultButton(true);

        button.setOnAction(e -> {
            String filepath = textField.getText().trim();
            File file = new File(filepath);

            if (!file.exists()) {
                AlertUtils.showErrorAlert("Error", "File not found", "The specified file could not be found.");
                return;
            }

            try {
                List<Event> importedEvents = objectMapper.readValue(file, new TypeReference<>() {});
                importedEvents.forEach(this::addImportedEvent);
                dialog.close();
                AlertUtils.showInformationAlert("Success", "Events Imported and Added",
                        "The events have been successfully imported and added to the server.");
            } catch (IOException ex) {
                // If reading as a list fails, try reading as a single event
                try {
                    Event importedEvent = objectMapper.readValue(file, Event.class);
                    addImportedEvent(importedEvent);
                    dialog.close();
                    AlertUtils.showInformationAlert("Success", "Event Imported and Added",
                            "The event has been successfully imported and added to the server.");
                } catch (IOException nestedEx) {
                    nestedEx.printStackTrace();
                    AlertUtils.showErrorAlert("Error", "Import Failed",
                            "Failed to import the event(s) from the specified file.");
                }
            }
        });

        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 300, 150);
        dialogScene.getStylesheets().add(AlertUtils.class.getResource("/styles.css").toExternalForm());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void addImportedEvent(Event importedEvent) {
        Event addedEvent = ServerUtils.addEvent(importedEvent);
        if (addedEvent != null) {
            Platform.runLater(() -> {
                eventData.add(addedEvent);
                eventsTable.setItems(eventData);
            });
        } else {
            AlertUtils.showErrorAlert("Error", "Add Event Failed",
                    "The event was imported but could not be added to the server.");
        }
    }

    /**
     * Goes back to the login page
     */
    @FXML
    void logout() {
        mainController.refreshEventsList();
        mainController.showLoginPage();
    }

    /**
     * Deletes all events from the server
     */
    @FXML
    public void deleteAllEvents() {
        boolean userConfirmed = AlertUtils.showConfirmationAlert("Confirm Delete All",
                "Are you sure you want to delete all events from the server? This operation cannot be reverted.");

        if (!userConfirmed) {
            return;
        }

        new Thread(() -> {
            boolean success = ServerUtils.deleteAllEvents();
            Platform.runLater(() -> {
                if (success) {
                    eventData.clear();
                    eventsTable.setItems(eventData);
                    AlertUtils.showInformationAlert("Success", "All Events Deleted",
                            "All events have been successfully deleted.");
                } else {
                    AlertUtils.showErrorAlert("Error", "Deletion Failed",
                            "Failed to delete all events.");
                }
            });
        }).start();
    }

}
