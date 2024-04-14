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
        boolean userConfirmed = AlertUtils.showConfirmationAlert(resourceBundle.getString("confirmDeletion"),
                resourceBundle.getString("confirmDeleteEvent"));

        if (!userConfirmed) {
            return;
        }

        new Thread(() -> {
            boolean success = ServerUtils.deleteEvent(event.getId());
            Platform.runLater(() -> {
                if (success) {
                    eventData.remove(event);
                    eventsTable.setItems(eventData);
                    AlertUtils.showInformationAlert(resourceBundle.getString("success"),
                            resourceBundle.getString("eventDeleted"),
                            resourceBundle.getString("eventDeletedSuccess"));

                } else {
                    AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                            resourceBundle.getString("deletionFailed"),
                            resourceBundle.getString("eventDeleteFail"));

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
        String folderName = "splitty_files";
        File outputFolder = new File(System.getProperty("user.home") + File.separator + folderName);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        File outputFile = new File(outputFolder, safeFileName + ".json");

        try {
            objectMapper.writeValue(outputFile, event);
            AlertUtils.showInformationAlert(resourceBundle.getString("eventExported"),
                    resourceBundle.getString("exportedTo"), outputFile.getAbsolutePath());

        } catch (FileNotFoundException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                    resourceBundle.getString("directoryNotFound"),
                    resourceBundle.getString("directoryNotExist"));

        } catch (IOException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                    resourceBundle.getString("exportFailed"),
                    resourceBundle.getString("failedExportEvent") + e.getMessage());

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

        String folderName = "splitty_files";
        File outputFolder = new File(System.getProperty("user.home") + File.separator + folderName);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        File outputFile = new File(System.getProperty("user.home")
                + File.separator + folderName + File.separator + "AllEvents.json");

        try {
            objectMapper.writeValue(outputFile, eventData);
            AlertUtils.showInformationAlert(resourceBundle.getString("allEventsExported"),
                    resourceBundle.getString("exportedTo"), outputFile.getAbsolutePath());
        } catch (IOException e) {
            AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                    resourceBundle.getString("exportFailedAll"),
                    resourceBundle.getString("failedExportAllEvents") + e.getMessage());
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
        Label label = new Label(resourceBundle.getString("enterFilePath"));
        dialogVbox.getChildren().add(label);
        String defaultPath = System.getProperty("user.home") + File.separator + "splitty_files" + File.separator;
        TextField textField = new TextField(defaultPath);
        dialogVbox.getChildren().add(textField);
        // Set cursor at the end of the text in the TextField
        Platform.runLater(() -> {
            textField.requestFocus();
            textField.positionCaret(textField.getText().length());
        });
        Button button = new Button(resourceBundle.getString("Import"));
        button.setDefaultButton(true);

        button.setOnAction(e -> {
            String filepath = textField.getText().trim();
            File file = new File(filepath);

            if (!file.exists()) {
                AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                        resourceBundle.getString("fileNotFound"),
                        resourceBundle.getString("specifiedFileNotFound"));

                return;
            }

            try {
                List<Event> importedEvents = objectMapper.readValue(file, new TypeReference<>() {});
                importedEvents.forEach(this::addImportedEvent);
                dialog.close();
                AlertUtils.showInformationAlert(resourceBundle.getString("success"),
                        resourceBundle.getString("eventsImportedAdded"),
                        resourceBundle.getString("eventsSuccessfullyImportedAdded"));
            } catch (IOException ex) {
                // If reading as a list fails, try reading as a single event
                try {
                    Event importedEvent = objectMapper.readValue(file, Event.class);
                    addImportedEvent(importedEvent);
                    dialog.close();
                    AlertUtils.showInformationAlert(resourceBundle.getString("success"),
                            resourceBundle.getString("eventImportedAdded"),
                            resourceBundle.getString("eventSuccessfullyImportedAdded"));
                } catch (IOException nestedEx) {
                    AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                            resourceBundle.getString("importFailed"),
                            resourceBundle.getString("failedImportEvents"));
                }
            }
        });

        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 400, 200);
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
            AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                    resourceBundle.getString("addEventFailed"),
                    resourceBundle.getString("eventImportedNotAdded"));

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
        boolean userConfirmed = AlertUtils.showConfirmationAlert(resourceBundle.getString("confirmDeleteAll"),
                resourceBundle.getString("confirmDeleteAllEvents"));

        if (!userConfirmed) {
            return;
        }

        new Thread(() -> {
            boolean success = ServerUtils.deleteAllEvents();
            Platform.runLater(() -> {
                if (success) {
                    eventData.clear();
                    eventsTable.setItems(eventData);
                    AlertUtils.showInformationAlert(resourceBundle.getString("success"),
                            resourceBundle.getString("allEventsDeleted"),
                            resourceBundle.getString("allEventsSuccessfullyDeleted"));
                } else {
                    AlertUtils.showErrorAlert(resourceBundle.getString("error"),
                            resourceBundle.getString("deletionFailed"),
                            resourceBundle.getString("failedDeleteAllEvents"));
                }
            });
        }).start();
    }

}
