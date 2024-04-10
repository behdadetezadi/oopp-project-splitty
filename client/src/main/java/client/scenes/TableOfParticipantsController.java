package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import commons.ParticipantDeletionRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class TableOfParticipantsController implements LanguageChangeListener {
    @FXML
    private Pagination pagination;
    @FXML
    private Button addButton;
    @FXML
    private Button backButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label titleLabel;
    private final ObservableList<Participant> participants = FXCollections.observableArrayList();
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Event event;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;


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

        registerForParticipantUpdates();
    }

    /**
     * initializer method
     */
    @FXML
    public void initialize() {
        // Loads the active locale, sets the resource bundle, and updates the UI
        LanguageUtils.loadLanguage(mainController.getStoredLanguagePreferenceOrDefault(), this);

        addButton.getStyleClass().add("button-hover");
        deleteButton.getStyleClass().add("button-hover");
        backButton.getStyleClass().add("button-hover");
        editButton.getStyleClass().add("button-hover");

        Tooltip addTooltip = new Tooltip(resourceBundle.getString("Click_to_add_a_participant"));
        Tooltip removeTooltip = new Tooltip(resourceBundle.getString("Click_to_remove_the_selected_participant"));
        Tooltip.install(addButton, addTooltip);
        Tooltip.install(deleteButton, removeTooltip);
    }

    private void registerForParticipantUpdates() {
        if (event != null && server != null) {
            server.registerForMessages("/topic/participants", event.getId(), null, this::handleParticipantUpdates);
            server.registerForMessages("/topic/participantDeletion", event.getId(), null, p -> {
                Platform.runLater(() -> {
                    participants.removeIf(participant -> participant.getId() == p.getId());
                    setupPagination();
                });
            });
        }
    }

    @FunctionalInterface
    public interface Validator {
        /**
         * Validates the provided Participant object.
         * @param participant The Participant to be validated.
         * @return A list of validation error messages. If the list is empty, the Participant is considered valid.
         */
        List<String> validate(Participant participant);
    }

    /**
     * Sets event to make sure we are making changes to the required event
     * @param event event
     */
    public void setEvent(Event event) {
        this.event = event;
        loadParticipants();
        setupPagination();
        ServerUtils.registerForUpdates(event.getId(),this::handleParticipantUpdates);
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
     * Update the text to the selected language
     */
    public void updateUIElements() {
        AnimationUtil.animateText(titleLabel, resourceBundle.getString("Participant_Overview"));
        AnimationUtil.animateText(backButton, resourceBundle.getString("Leave"));
        AnimationUtil.animateText(editButton, resourceBundle.getString("Modify"));
        AnimationUtil.animateText(addButton, resourceBundle.getString("plus"));
        AnimationUtil.animateText(deleteButton, resourceBundle.getString("minus"));
        refreshParticipantDetailsDisplay();
    }

    private void refreshParticipantDetailsDisplay() {
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private void handleParticipantUpdate(Participant participant) {
        Platform.runLater(() -> {
            boolean exists = participants.stream()
                    .anyMatch(p -> p.getId() == participant.getId());

            if (!exists) {
                participants.add(participant);
                setupPagination();
            }
        });
    }
    public void stop(){
        server.stop();
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
        editParticipant(newParticipant, "Add New Participant", resourceBundle.getString("Enter_details_for_the_new_participant."),
                this::addParticipant);
    }

    /**
     * edits the participant on the page
     */
    @FXML
    private void handleEditButton() {
        if (participants.isEmpty()) {
            AlertUtils.showErrorAlert("Error", "No Participants Found",
                    resourceBundle.getString("There_are_no_participants_to_edit."));
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
                    resourceBundle.getString("Are_you_sure_you_want_to_remove")+ " " + selectedParticipant.getFirstName()
                            + " " + selectedParticipant.getLastName() + "?");
            if (confirmation) {
                ParticipantDeletionRequest request = new ParticipantDeletionRequest(event.getId(), selectedParticipant.getId());

                server.send("/app/participantDeletion", request);
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
            String content = formatParticipantDetails(participant, resourceBundle);
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
    protected void loadParticipants() {
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
        ParticipantDialog dialog = new ParticipantDialog(participant, title,
                header, this::validateParticipantData, resourceBundle);
        Optional<Participant> result = dialog.showAndWait();
        result.ifPresent(p -> {
            p.setFirstName(ValidationUtils.autoCapitalizeWord(p.getFirstName()));
            p.setLastName(ValidationUtils.autoCapitalizeWord(p.getLastName()));
            action.accept(p);
        });

    }

    /**
     * Adds a new participant to the event and updates the UI accordingly.
     * @param participant The {@link Participant} to add to the event.
     */
    private void addParticipant(Participant participant) {
        boolean exists = participants.stream()
                .anyMatch(p -> p.getId() == participant.getId());

        if (!exists){
            Participant addedParticipant = ServerUtils.addParticipantToEvent(event.getId(), participant);
            if (addedParticipant != null) {
                participants.add(addedParticipant);
                setupPagination();
                AlertUtils.showInformationAlert("Success", "Participant Added",
                        resourceBundle.getString("Participant_was_successfully_added."));
            }}
    }

    /**
     * Updates the details of an existing participant in the event and refreshes the UI.
     * @param participant The {@link Participant} whose details are to be updated.
     */
    private void updateParticipant(Participant participant) {
        participant.setFirstName(ValidationUtils.autoCapitalizeWord(participant.getFirstName()));
        participant.setLastName(ValidationUtils.autoCapitalizeWord(participant.getLastName()));
        long participantId = participant.getId();
        boolean isUpdated = server.updateParticipant(event.getId(), participantId, participant);
        server.send("app/participants",participant);
        if (isUpdated) {
            refreshParticipantDetails(participant);
            AlertUtils.showInformationAlert("Success", "Participant Updated",
                    resourceBundle.getString("Participant_details_were_successfully_updated."));
        }
    }

    private void handleParticipantUpdates(Participant updatedParticipant) {
        Platform.runLater(() -> {
            OptionalInt indexOpt = IntStream.range(0, participants.size())
                    .filter(i -> participants.get(i).getId()==(updatedParticipant.getId()))
                    .findFirst();

            if (indexOpt.isPresent()) {
                participants.set(indexOpt.getAsInt(), updatedParticipant);
            } else {
                participants.add(updatedParticipant);
            }
            setupPagination();
        });
    }


    /**
     * Removes a participant from the event and updates the UI.
     * @param participant The {@link Participant} to remove.
     */
    private void deleteParticipant(Participant participant) {
        boolean isDeleted = ServerUtils.deleteParticipant(participant.getId(), event.getId());
        if (isDeleted) {
            participants.remove(participant);
            setupPagination();
            AlertUtils.showInformationAlert("Success", "Participant Removed",
                    resourceBundle.getString("Participant_was_successfully_removed."));
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
     * collects data from the form
     * @param formFields Hashmap
     * @return Participant
     */
    private Participant collectDataFromForm(Map<String, Control> formFields) {
        String firstName = ((TextField) formFields.get("First Name")).getText();
        String lastName = ((TextField) formFields.get("Last Name")).getText();
        String username = ((TextField) formFields.get("Username")).getText();
        String email = ((TextField) formFields.get("Email")).getText();
        String iban = ((TextField) formFields.get("IBAN")).getText();
        String bic = ((TextField) formFields.get("BIC")).getText();
        String languageChoice = ((ComboBox<String>) formFields.get("Language")).getValue();

        return new Participant(firstName, lastName, username, email, iban, bic, languageChoice);
    }

    /**
     * method to validate participant data by checking the fields entered
     * @param participant a Participant
     * @return an array list of strings
     */
    private List<String> validateParticipantData(Participant participant) {
        List<String> errors = new ArrayList<>();

        if (!ValidationUtils.isValidName(participant.getFirstName())) {
            errors.add("First Name must only contain letters.");
        }
        if (!ValidationUtils.isValidName(participant.getLastName())) {
            errors.add("Last Name must only contain letters.");
        }
        if (!ValidationUtils.isValidUsername(participant.getUsername())) {
            errors.add("Username must contain only letters, digits, and underscores.");
        }
        if (!ValidationUtils.isValidEmail(participant.getEmail())) {
            errors.add("Email must be in a valid format (e.g., user@example.com).");
        }
        if (!ValidationUtils.isValidIBAN(participant.getIban())) {
            errors.add("IBAN must be in a valid format (e.g., NL89 BANK 0123 4567 89).");
        }
        if (!ValidationUtils.isValidBIC(participant.getBic())) {
            errors.add("BIC must be in a valid format: 8 alphanumeric characters (e.g., ABCDEF12).");
        }
        if (!ValidationUtils.isValidLanguage(participant.getLanguageChoice())) {
            errors.add("Select a language please.");
        }
        return errors;
    }

    /**
     * Formats the details of a participant for display.
     * @param participant The {@link Participant} whose details are to be formatted.
     * @return A formatted string containing the participant's details.
     */
    private String formatParticipantDetails(Participant participant, ResourceBundle resourceBundle) {
        return String.format("""
                    %s: %s
                    %s: %s
                    %s: %s
                    %s: %s
                    %s: %s
                    %s: %s
                    %s: %s""",
                resourceBundle.getString("First_Name"), participant.getFirstName(),
                resourceBundle.getString("Last_Name"), participant.getLastName(),
                resourceBundle.getString("User_Name"), participant.getUsername(),
                resourceBundle.getString("Email"), participant.getEmail(),
                resourceBundle.getString("IBAN"), participant.getIban(),
                resourceBundle.getString("BIC"), participant.getBic(),
                resourceBundle.getString("Language"), participant.getLanguageChoice());
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
        ParticipantDialog(Participant participant, String title, String header,
                          Validator validator, ResourceBundle resourceBundle) {
            setTitle(title);
            setHeaderText(header);

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            getDialogPane().setMinHeight(350);
            getDialogPane().setMinWidth(600);

            Pair<GridPane, Map<String, Control>> formPair = ParticipantForm.createParticipantForm(participant,
                    resourceBundle);
            GridPane grid = formPair.getKey();
            Map<String, Control> formFields = formPair.getValue();

            getDialogPane().setContent(grid);

            String cssPath = this.getClass().getResource("/styles.css").toExternalForm();
            getDialogPane().getStylesheets().add(cssPath);

            Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
            saveButton.addEventFilter(ActionEvent.ACTION, event -> {
                Participant tempParticipant = ParticipantForm.extractParticipantFromForm(formFields, new Participant());
                List<String> validationErrors = validator.validate(tempParticipant);
                if (!validationErrors.isEmpty()) {
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", validationErrors), ButtonType.OK);
                    alert.setHeaderText("Validation Error");
                    alert.showAndWait();
                }
            });

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
        private static final String FIRST_NAME = "First Name";
        private static final String LAST_NAME = "Last Name";
        private static final String USERNAME = "Username";
        private static final String EMAIL = "Email";
        private static final String IBAN = "IBAN";
        private static final String BIC = "BIC";
        private static final String LANGUAGE = "Language";
        static Pair<GridPane, Map<String, Control>> createParticipantForm(Participant participant,
                                                                          ResourceBundle resourceBundle) {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            TextField firstNameField = createTextField(participant.getFirstName(), resourceBundle.getString("First_Name"));
            TextField lastNameField = createTextField(participant.getLastName(), resourceBundle.getString("Last_Name"));
            TextField usernameField = createTextField(participant.getUsername(), resourceBundle.getString("User_Name"));
            TextField emailField = createTextField(participant.getEmail(), resourceBundle.getString("Email"));
            TextField ibanField = createTextField(participant.getIban(), resourceBundle.getString("IBAN"));
            TextField bicField = createTextField(participant.getBic(), resourceBundle.getString("BIC"));
            ComboBox<String> languageComboBox = createComboBox(participant.getLanguageChoice(),
                    resourceBundle.getString("Language"), "English", "Dutch", "German");

            Map<String, Control> formFields = new LinkedHashMap<>();
            formFields.put(FIRST_NAME, firstNameField);
            formFields.put(LAST_NAME, lastNameField);
            formFields.put(USERNAME, usernameField);
            formFields.put(EMAIL, emailField);
            formFields.put(IBAN, ibanField);
            formFields.put(BIC, bicField);
            formFields.put(LANGUAGE, languageComboBox);

            int row = 0;
            for (String fieldName : formFields.keySet()) {
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
            participant.setUsername(((TextField) formFields.get(USERNAME)).getText());
            participant.setFirstName(((TextField) formFields.get(FIRST_NAME)).getText());
            participant.setLastName(((TextField) formFields.get(LAST_NAME)).getText());
            participant.setEmail(((TextField) formFields.get(EMAIL)).getText());
            participant.setIban(((TextField) formFields.get(IBAN)).getText());
            participant.setBic(((TextField) formFields.get(BIC)).getText());
            participant.setLanguageChoice(((ComboBox<String>) formFields.get(LANGUAGE)).getValue());

            return participant;
        }

    }
    public List<Participant>getUpdatedParticipant(Event event)
    {
        return participants;
    }
}
