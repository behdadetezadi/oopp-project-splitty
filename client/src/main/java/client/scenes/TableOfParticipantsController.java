package client.scenes;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;

public class TableOfParticipantsController {

    @FXML
    private Pagination pagination;
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    private final ObservableList<Participant> participants = FXCollections.observableArrayList();

    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;


    /**
     * dependency injection
     * @param primaryStage primary stage
     * @param server server
     * @param mainController mainController
     * @param event The event we are working on
     */
    @Inject
    public TableOfParticipantsController(Stage primaryStage, ServerUtils server,
                                         MainController mainController, Event event) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
        this.event = event;
    }

    /**
     * Sets event to make sure we are making changes to the required event
     * @param event event
     */
    public void setEvent(Event event) {
        this.event = event;
        initialize();
    }


    /**
     * initializer method
     */
    @FXML
    public void initialize() {
        loadParticipants();
        setupPagination();

        addButton.getStyleClass().add("button-hover");
        deleteButton.getStyleClass().add("button-hover");

        Tooltip addTooltip = new Tooltip("Click to add a participant");
        Tooltip removeTooltip = new Tooltip("Click to remove the selected participant");
        Tooltip.install(addButton, addTooltip);
        Tooltip.install(deleteButton, removeTooltip);
    }

    /**
     * switching back to the event overview page
     */
    @FXML
    private void handleBackButton() {
        mainController.showEventOverview(event);
    }

    /**
     * adds a participant
     */
    @FXML
    private void handleAddButton() {
        HashSet<Long> eventIds = new HashSet<>();
        eventIds.add(event.getId());

        Participant newParticipant = new Participant("", "", "", "", "",
                "", new HashMap<>(), new HashMap<>(), eventIds, "");
        editParticipant(newParticipant, "Add New Participant", "Enter details for the new participant.",
                this::addParticipant);
    }

    /**
     * edits the participant on the page
     */
    @FXML
    private void handleEditButton() {
        if (participants.isEmpty()) {
            AlertUtils.showErrorAlert("Error", "No Participants Found",
                    "There are no participants to edit.");
            return;
        }
        Participant selectedParticipant = getSelectedParticipant();
        if (selectedParticipant != null) {
            editParticipant(selectedParticipant, "Edit Participant", "Edit the details of the participant.",
                    this::updateParticipant);
        }
    }

    /**
     * deletes current participant
     */
    @FXML
    private void handleDeleteButton() {
        Participant selectedParticipant = getSelectedParticipant();
        if (selectedParticipant != null) {
            boolean confirmation = AlertUtils.showConfirmationAlert("Remove participant",
                    "Are you sure you want to remove " + selectedParticipant.getFirstName()
                            + " " + selectedParticipant.getLastName() + "?");
            if (confirmation) {
                deleteParticipant(selectedParticipant);
            }
        }
    }

    /**
     * This allows each page to display a single participant with his/her attributes
     * @param pageIndex as an int
     * @return a Node
     */
    private Node createPage(int pageIndex) {
        VBox box = new VBox(5);
        if (pageIndex < participants.size()) {
            Participant participant = participants.get(pageIndex);
            String content = formatParticipantDetails(participant);
            Label label = new Label(content);
            label.getStyleClass().add("participant-label");
            box.getChildren().add(label);
        }
        return box;
    }

    /**
     * this method ensures all participants are added and
     * loaded before the method create Page executes
     */
    private void loadParticipants() {
        List<Participant> fetchedParticipants = ServerUtils.getParticipantsByEventId(event.getId());
        participants.setAll(fetchedParticipants);
        setupPagination();
    }

    /**
     * Configures the pagination control based on the number of participants.
     */
    private void setupPagination() {
        pagination.setPageCount(Math.max(1, participants.size()));
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Retrieves the participant currently selected in the pagination control.
     * @return The currently selected {@link Participant} or {@code null} if no participant is selected.
     */
    private Participant getSelectedParticipant() {
        int currentIndex = pagination.getCurrentPageIndex();
        if (currentIndex < participants.size()) {
            return participants.get(currentIndex);
        }
        return null;
    }

    /**
     * Displays a dialog for editing a participant's details and performs a specified action upon confirmation.
     * @param participant The {@link Participant} to edit.
     * @param title The title of the dialog window.
     * @param header The header text for the dialog.
     * @param action The action to perform with the edited participant.
     */
    private void editParticipant(Participant participant, String title, String header, ParticipantConsumer action) {
        ParticipantDialog dialog = new ParticipantDialog(participant, title, header);
        Optional<Participant> result = dialog.showAndWait();
        result.ifPresent(action::accept);
    }

    /**
     * Adds a new participant to the event and updates the UI accordingly.
     * @param participant The {@link Participant} to add to the event.
     */
    private void addParticipant(Participant participant) {
        Participant addedParticipant = ServerUtils.addParticipantToEvent(event.getId(), participant);
        if (addedParticipant != null) {
            participants.add(addedParticipant);
            setupPagination();
            AlertUtils.showInformationAlert("Success", "Participant Added",
                    "Participant was successfully added.");
        }
    }

    /**
     * Updates the details of an existing participant in the event and refreshes the UI.
     * @param participant The {@link Participant} whose details are to be updated.
     */
    private void updateParticipant(Participant participant) {
        long participantId = participant.getId();
        boolean isUpdated = server.updateParticipant(event.getId(), participantId, participant);
        if (isUpdated) {
            refreshParticipantDetails(participant);
            AlertUtils.showInformationAlert("Success", "Participant Updated",
                    "Participant details were successfully updated.");
        }
    }

    /**
     * Removes a participant from the event and updates the UI.
     * @param participant The {@link Participant} to remove.
     */
    private void deleteParticipant(Participant participant) {
        boolean isDeleted = server.deleteParticipant(participant.getId(), event.getId());
        if (isDeleted) {
            participants.remove(participant);
            setupPagination();
            AlertUtils.showInformationAlert("Success", "Participant Removed",
                    "Participant was successfully removed.");
        }
    }

    /**
     * Refreshes the details of a participant in the UI.
     * @param updatedParticipant The {@link Participant} with updated details.
     */
    private void refreshParticipantDetails(Participant updatedParticipant) {
        int index = participants.indexOf(updatedParticipant);
        if (index != -1) {
            participants.set(index, updatedParticipant);
        }
        setupPagination();
    }

    /**
     * Formats the details of a participant for display.
     * @param participant The {@link Participant} whose details are to be formatted.
     * @return A formatted string containing the participant's details.
     */
    private String formatParticipantDetails(Participant participant) {
        return String.format("""
                        First Name: %s
                        Last Name: %s
                        Username: %s
                        Email: %s
                        IBAN: %s
                        BIC: %s
                        Language Preference: %s""",
                participant.getFirstName(),
                participant.getLastName(),
                participant.getUsername(),
                participant.getEmail(),
                participant.getIban(),
                participant.getBic(),
                participant.getLanguageChoice());
    }

    /**
     * A functional interface for consuming a {@link Participant} instance.
     */
    @FunctionalInterface
    interface ParticipantConsumer {
        void accept(Participant participant);
    }

    /**
     * A dialog for creating or editing a participant's details.
     */
    static class ParticipantDialog extends Dialog<Participant> {
        ParticipantDialog(Participant participant, String title, String header) {
            setTitle(title);
            setHeaderText(header);

            ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            getDialogPane().setMinHeight(350);
            getDialogPane().setMinWidth(600);

            Pair<GridPane, Map<String, Control>> formPair = ParticipantForm.createParticipantForm(participant);
            GridPane grid = formPair.getKey();
            Map<String, Control> formFields = formPair.getValue();

            getDialogPane().setContent(grid);

            String cssPath = this.getClass().getResource("/styles.css").toExternalForm();
            getDialogPane().getStylesheets().add(cssPath);

            setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return ParticipantForm.extractParticipantFromForm(formFields, participant);
                }
                return null;
            });
        }
    }


    /**
     * Utility class for handling participant form creation and data extraction.
     */
    static class ParticipantForm {
        /**
         * Creates a form for entering or editing a participant's details.
         * @param participant The {@link Participant} whose details are to be used as initial form values.
         * @return A {@link Pair} containing the form as a {@link GridPane} and a map of form fields.
         */
        static Pair<GridPane, Map<String, Control>> createParticipantForm(Participant participant) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            TextField firstNameField = createTextField(participant.getFirstName(), "First Name");
            TextField lastNameField = createTextField(participant.getLastName(), "Last Name");
            TextField usernameField = createTextField(participant.getUsername(), "Username");
            TextField emailField = createTextField(participant.getEmail(), "Email");
            TextField ibanField = createTextField(participant.getIban(), "IBAN");
            TextField bicField = createTextField(participant.getBic(), "BIC");
            ComboBox<String> languageComboBox = createComboBox(participant.getLanguageChoice(),
                    "Language", "English", "Dutch");

            // Store the fields in a map for easy access later
            Map<String, Control> formFields = new LinkedHashMap<>();
            formFields.put("First Name", firstNameField);
            formFields.put("Last Name", lastNameField);
            formFields.put("Username", usernameField);
            formFields.put("Email", emailField);
            formFields.put("IBAN", ibanField);
            formFields.put("BIC", bicField);
            formFields.put("Language", languageComboBox);

            // Adding the fields to the grid
            int row = 0;
            for (String fieldName : formFields.keySet()) {
                Label label = new Label(fieldName + ":");
                grid.add(label, 0, row);
                grid.add(formFields.get(fieldName), 1, row++);
            }
            return new Pair<>(grid, formFields);
        }

        /**
         * Creates a text field with the specified initial value and prompt text.
         * @param value The initial value for the text field.
         * @param promptText The prompt text to display in the text field.
         * @return A {@link TextField} with the specified initial value and prompt text.
         */
        static TextField createTextField(String value, String promptText) {
            TextField textField = new TextField(value);
            textField.setPromptText(promptText);
            textField.setPrefWidth(300);
            return textField;
        }

        /**
         * Creates a combo box with the specified initial value, prompt text, and options.
         * @param value The initial value to select in the combo box.
         * @param promptText The prompt text to display in the combo box.
         * @param options The options available for selection in the combo box.
         * @return A {@link ComboBox} with the specified initial value, prompt text, and options.
         */
        static ComboBox<String> createComboBox(String value, String promptText, String... options) {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(options);
            comboBox.setPromptText(promptText);
            comboBox.setValue(value);
            return comboBox;
        }

        /**
         * Extracts participant details from the form fields and creates a {@link Participant} instance.
         * @param formFields The map of form fields containing participant details.
         * @param participant The original participant.
         * @return A new {@link Participant} instance with details extracted from the form fields.
         */
        static Participant extractParticipantFromForm(Map<String, Control> formFields, Participant participant) {
            participant.setUsername(((TextField) formFields.get("Username")).getText());
            participant.setFirstName(((TextField) formFields.get("First Name")).getText());
            participant.setLastName(((TextField) formFields.get("Last Name")).getText());
            participant.setEmail(((TextField) formFields.get("Email")).getText());
            participant.setIban(((TextField) formFields.get("IBAN")).getText());
            participant.setBic(((TextField) formFields.get("BIC")).getText());
            participant.setLanguageChoice(((ComboBox<String>) formFields.get("Language")).getValue());

            return participant;
        }
    }
}
