package client.scenes;

import client.Language;
import client.utils.LanguageChangeListener;
import client.Language;
import client.utils.AlertUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import static client.utils.AnimationUtil.animateText;

public class EventOverviewController implements LanguageChangeListener {
    private ServerUtils server;
    private MainController mainController;
    private Event event;
    private ResourceBundle resourceBundle;
    private Locale activeLocale;
    private Stage primaryStage;

    @FXML
    private ComboBox<Language> languageComboBox;
    @FXML
    private Button backToMain;
    @FXML
    private Button showAllExpensesButton;
    @FXML
    private BorderPane root;
    @FXML
    private ListView<String> participantsListView;
    @FXML
    private ComboBox<ParticipantOption> participantDropdown;
    @FXML
    private Label inviteCodeLabel;
    @FXML
    private Button showParticipantsButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Label expensesLabel;
    @FXML
    private Button sendInvitesButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private Button showExpensesButton;
    @FXML
    private Label inviteCode;

    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController main controller
     */
    @Inject
    public EventOverviewController(Stage primaryStage,ServerUtils server, MainController mainController) {
        this.primaryStage = primaryStage;
        this.server = server;
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

        Tooltip editTitleTooltip = new Tooltip(resourceBundle.getString("Click_to_edit_the_title"));
        Tooltip.install(titleLabel, editTitleTooltip);
        showParticipantsButton.getStyleClass().add("button-hover");
        sendInvitesButton.getStyleClass().add("button-hover");
        addExpenseButton.getStyleClass().add("button-hover");
        showExpensesButton.getStyleClass().add("button-hover");
        backToMain.getStyleClass().add("button-hover");
        showAllExpensesButton.getStyleClass().add("button-hover");
        showExpensesButton.setOnAction(this::showExpensesForSelectedParticipant);
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
        animateText(backToMain, resourceBundle.getString("Go_Back"));
        animateText(inviteCodeLabel, resourceBundle.getString("Invite_Code"));
        animateText(participantsLabel, resourceBundle.getString("Participants"));
        animateText(expensesLabel, resourceBundle.getString("Expenses"));
        animateText(showParticipantsButton, resourceBundle.getString("Show_Participants"));
        participantDropdown.setPromptText(resourceBundle.getString("Select_participant"));
        animateText(addExpenseButton, resourceBundle.getString("Add_Expense"));
        animateText(showExpensesButton, resourceBundle.getString("Show_Expenses"));
        animateText(sendInvitesButton, resourceBundle.getString("Send_Invites"));
        animateText(showAllExpensesButton, resourceBundle.getString("Show_All_Expenses"));
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
        languageComboBox.setCellFactory(listView -> new LanguageListCell());
        languageComboBox.setButtonCell(new LanguageListCell());
    }

    /**
     * show expenses of selected participant
     * @param event the targeted event
     */
    @FXML
    private void showExpensesForSelectedParticipant(ActionEvent event) {
        ParticipantOption selectedParticipantOption = participantDropdown.getSelectionModel().getSelectedItem();
        if (selectedParticipantOption != null) {
            Long selectedParticipantId = selectedParticipantOption.getId();
            mainController.showParticipantExpensesOverview(this.event, selectedParticipantId);
        } else {
            AlertUtils.showErrorAlert("Select participant", "Error", resourceBundle.getString("Please_select_a_participant_to_show_expenses"));
        }
    }
    /**
     * show expenses overview of the event
     */
    @FXML
    private void showAllExpensesOverview() {
        mainController.showExpenseOverview(this.event);
    }


    /**
     * called by startPage and other pages when setting up this page
     * @param event event to be set
     */
    public void setEvent(Event event) {
        this.event = event;

        server.registerForEventUpdates("/topic/eventTitle", event.getId(), null, event1 -> {
            Platform.runLater(() -> {
                this.event = event1;
                titleLabel.setText(this.event.getTitle());
            });
        });

        titleLabel.setText(event.getTitle());
        titleLabel.setOnMouseClicked(click -> editTitle());

        this.inviteCode.setText(String.valueOf(this.event.getInviteCode()));
        this.inviteCode.setOnMouseClicked(click -> copyInviteCode());
        Tooltip inviteCodeToolTip = new Tooltip(resourceBundle.getString("Click_to_copy_the_invite_code"));
        Tooltip.install(inviteCode,inviteCodeToolTip);
        this.inviteCode.getStyleClass().add("label-hover");

        titleLabel.getStyleClass().add("label-hover");
        loadParticipants();
        initializeParticipants();
        animateEventTitle();
    }

    @FXML
    void goBackToStartPage(ActionEvent event) {
        mainController.showStartPage();
    }


    /**
     * loads participants / Trying something and commented out initialize participants methods
     */
    private void loadParticipants() {
        if (event != null) {
            List<Participant> fetchedParticipants = ServerUtils.getParticipantsByEventId(event.getId());
//            ObservableList<String> participantNames = FXCollections.observableArrayList(
//                    fetchedParticipants.stream().map(Participant::getFirstName).collect(Collectors.toList())
//            );
//            participantsListView.setItems(participantNames);
        }
    }

    /**
     * Edit the title directly in the label
     */
    private void editTitle() {
        TextInputDialog dialog = new TextInputDialog(titleLabel.getText());
        dialog.setTitle(resourceBundle.getString("Edit_Title"));
        dialog.setHeaderText(null);
        dialog.setContentText(resourceBundle.getString("New_Title"));

        String cssPath = Objects.requireNonNull(this.getClass().getResource("/styles.css")).toExternalForm();
        dialog.getDialogPane().getStylesheets().add(cssPath);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newTitle -> {
            titleLabel.setText(newTitle); // Update UI immediately
            server.updateEventTitle(event.getId(), newTitle); // Send request to server

            Map<String, Object> payload = new HashMap<>();
            payload.put("eventId", event.getId());
            payload.put("newTitle", newTitle);

            server.send("/app/eventTitle", payload);
            event.setTitle(newTitle); // Update local event object
        });
    }

    private void copyInviteCode() {

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(
                this.inviteCode.getText()
        );
        clipboard.setContent(content);
        AlertUtils.showInformationAlert("Invite code copied!",
                "copied the following invitecode: ",
                this.inviteCode.getText());
    }

    /**
     * method to switch over to the participant scene when clicked upon
     */
    @FXML
    private void showParticipants() {
        try {
            mainController.showTableOfParticipants(this.event);

        } catch (IllegalStateException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Failed to Load", "Error", resourceBundle.getString("Failed_to_load_the_participant_scene"));
        }
    }


    /**
     * Initializes the participants dropdown with options based on the event's associated participants.
     */

    public void initializeParticipants() {
        List<Participant> participants = ServerUtils.getParticipantsByEventId(event.getId());
        ObservableList<ParticipantOption> participantOptions = FXCollections.observableArrayList(
                participants.stream()
                        .map(p -> new ParticipantOption(p.getId(), p.getFirstName() + " " + p.getLastName()))
                        .collect(Collectors.toList())
        );

        participantDropdown.getItems().clear(); // Clear existing options
        participantDropdown.setItems(participantOptions);
    }

    /**
     * animates the labels using AnimationUtil
     */
    private void animateEventTitle() {
        animateText(titleLabel, event.getTitle());
    }

    /**
     * sendInvites method
     */
    @FXML
    public void sendInvites() {

        try {
            mainController.showInvitePage(this.event);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Failed to Load", "Error", resourceBundle.getString("Failed_to_load_the_invite_scene"));
        }
    }

    //TODO
    /**
     *
     * @param event Action event?
     */
    @FXML
    public void addExpense(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            ParticipantOption selectedParticipantOption = participantDropdown.getSelectionModel().getSelectedItem();
            if(selectedParticipantOption != null) {
                Long selectedParticipantId = selectedParticipantOption.getId();
                mainController.showAddExpense(this.event, selectedParticipantId);
            } else {
                AlertUtils.showErrorAlert("Select participant", "Error",
                        resourceBundle.getString("Please_select_for_which_participant_you_want_to_add_an_expense."));
            }
        } else {
            throw new IllegalStateException();
        }
    }


    /**
     * overloaded version of addExpense to be used by keyboard shortcuts
     */
    @FXML
    public void addExpense() {

            ParticipantOption selectedParticipantOption = participantDropdown.getSelectionModel().getSelectedItem();
            if(selectedParticipantOption != null) {
                Long selectedParticipantId = selectedParticipantOption.getId();
                mainController.showAddExpense(this.event, selectedParticipantId);
            } else {
                AlertUtils.showErrorAlert("Select participant", "Error",
                        resourceBundle.getString("Please_select_for_which_participant_you_want_to_add_an_expense."));
            }

    }

    /**
     * self-explanatory
     */
    public void refreshParticipants() {
        loadParticipants();
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
}
