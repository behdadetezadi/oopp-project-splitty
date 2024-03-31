package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static client.utils.AnimationUtil.animateButton;
import static client.utils.AnimationUtil.animateText;

public class EventOverviewController {
    private ServerUtils server;
    private MainController mainController;
    private Event event;
    private ResourceBundle resourceBundle;
    private Locale activeLocale;
    private Stage primaryStage;



    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Button backToMain;

    @FXML
    private BorderPane root;
    @FXML
    private ListView<String> participantsListView;
    @FXML
    private ComboBox<ParticipantOption> participantDropdown;
    @FXML
    private Button showParticipantsButton;
    @FXML
    private ListView<String> optionsListView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Label expensesLabel;
    @FXML
    private Label optionsLabel;
    @FXML
    private Button filterOne;
    @FXML
    private Button filterTwo;
    @FXML
    private Button sendInvitesButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    private Button showExpensesButton;


    @FXML
    private final ObservableList<String> allOptions = FXCollections
            .observableArrayList("1", "2");
    @FXML
    private final ObservableList<String> filteredOptions = FXCollections.observableArrayList();



    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController maincontroller
     */
    @Inject
    public EventOverviewController(Stage primaryStage,ServerUtils server, MainController mainController) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
    }


    /**
     * default constructor for the program to work (don't know why)
     */
    public EventOverviewController() {
        // Default constructor
    }

    /**
     * initializer function does: //TODO
     */
    public void initialize(Locale locale) {

        // Load default language

        loadLanguage(locale);
        activeLocale = locale;

        // Populate language combo box
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            switchLanguage(selectedLanguage);
        });

        languageComboBox.setValue(locale.getDisplayLanguage(locale));

        if (event != null) {
            titleLabel.setText(event.getTitle());
            titleLabel.setOnMouseClicked(event -> editTitle());
            Tooltip editTitleTooltip = new Tooltip(resourceBundle.getString("Click_to_edit_the_title"));
            Tooltip.install(titleLabel, editTitleTooltip);

            showParticipantsButton.getStyleClass().add("button-hover");
            filterOne.getStyleClass().add("button-hover");
            filterTwo.getStyleClass().add("button-hover");
            sendInvitesButton.getStyleClass().add("button-hover");
            addExpenseButton.getStyleClass().add("button-hover");
            showExpensesButton.getStyleClass().add("button-hover");
            backToMain.getStyleClass().add("button-hover");


        }
        showExpensesButton.setOnAction(this::showExpensesForSelectedParticipant);
    }
    private void loadLanguage(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("message", locale);
        activeLocale = locale;
        updateUIElements();
    }

    private void switchLanguage(String language) {
        switch (language) {
            case "English":
                loadLanguage(Locale.ENGLISH);
                break;
            case "Deutsch":
                loadLanguage(Locale.GERMAN);
                break;
            case "Nederlands":
                loadLanguage(new Locale("nl"));
                break;
            default:
                // Default to English
                loadLanguage(Locale.ENGLISH);
                break;
        }
    }

    private void updateUIElements() {
        backToMain.setText(resourceBundle.getString("Go_Back"));
        participantsLabel.setText(resourceBundle.getString("Participants"));
        showParticipantsButton.setText(resourceBundle.getString("Show_Participants"));
        expensesLabel.setText(resourceBundle.getString("Expenses"));
        participantDropdown.setPromptText(resourceBundle.getString("Select_participant"));
        addExpenseButton.setText(resourceBundle.getString("Add_Expense"));
        showExpensesButton.setText(resourceBundle.getString("Show_Expenses"));
        optionsLabel.setText(resourceBundle.getString("Options"));
        filterOne.setText(resourceBundle.getString("From"));
        filterTwo.setText(resourceBundle.getString("Including"));
        sendInvitesButton.setText(resourceBundle.getString("Send_Invites"));
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
            mainController.showParticipantExpensesOverview(this.event, selectedParticipantId, activeLocale);
        } else {
            showErrorAlert(resourceBundle.getString("Please_select_a_participant_to_show_expenses"));
        }
    }
    /**
     * show expenses overview of the event
     */
    @FXML
    private void showAllExpensesOverview() {
        mainController.showExpenseOverview(this.event, activeLocale);
    }


    /**
     * called by startPage and other pages when setting up this page
     * @param event event to be set
     */
    public void setEvent(Event event, Locale locale) {
        this.event = event;
        initialize(locale);
        titleLabel.getStyleClass().add("label-hover");
        loadParticipants();
        animateLabels();
        animateButtonsText();
        initializeParticipants();
    }

    @FXML
    private void goBackToStartPage(ActionEvent event) {
        mainController.showStartPage(activeLocale);
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

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newTitle -> {
            titleLabel.setText(newTitle); // Update UI immediately
            server.updateEventTitle(event.getId(), newTitle); // Send request to server
            event.setTitle(newTitle); // Update local event object
        });
    }

    /**
     * method to switch over to the participant scene when clicked upon
     */
    @FXML
    private void showParticipants() {
        try {
            mainController.showTableOfParticipants(this.event, activeLocale);

        } catch (IllegalStateException e) {
            e.printStackTrace();
            showErrorAlert(resourceBundle.getString("Failed_to_load_the_participant_scene"));
        }
    }

//
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



    private void initializeOptionsListView() {
        // If you have specific options to show, add them here
        optionsListView.getItems().addAll("Option 1", "Option 2", "Option 3");
    }

    /**
     * animates the labels using AnimationUtil
     */
    private void animateLabels() {
        animateText(titleLabel, event.getTitle());
        animateText(participantsLabel, resourceBundle.getString("Participants"));
        animateText(expensesLabel, resourceBundle.getString("Expenses"));
        animateText(optionsLabel, resourceBundle.getString("Options"));
    }

    /**
     * animates the buttons using AnimationUtil
     */
    private void animateButtonsText() {
        //animateButton(sendInvitesButton);
        //animateButton(addExpenseButton);
        animateButton(filterOne);
        animateButton(filterTwo);
    }



    //TODO needs changing to better fit functionality and backlog
    /**
     * apply filters for options list
     */
    @FXML
    private void applyFromFilter() {
        // Assuming you want to filter based on a selected participant
        ParticipantOption selectedParticipant = participantDropdown.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            filteredOptions.clear();
            filteredOptions.add(resourceBundle.getString("Filtered_option_for") + selectedParticipant);
            optionsListView.setItems(filteredOptions);
        }
    }


    //TODO needs changing to better fit functionality and backlog
    /**
     * apply filters for options list
     */
    @FXML
    private void applyIncludingFilter() {
        String selectedParticipant = null; // Get selected participant
        if (selectedParticipant != null) {
            filteredOptions.clear();
            filteredOptions.add(resourceBundle.getString("Including")+ " " + selectedParticipant);
            optionsListView.setItems(filteredOptions);
        }
    }

    /**
     * sendInvites method
     */
    @FXML
    public void sendInvites() {

        try {
            mainController.showInvitePage(this.event, activeLocale);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            showErrorAlert(resourceBundle.getString("Failed_to_load_the_invite_scene"));
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
                mainController.showAddExpense(this.event, selectedParticipantId, activeLocale);
            } else {
                showErrorAlert(resourceBundle.getString("Please_select_for_which_participant_you_want_to_add_an_expense."));
            }
        } else {
            throw new IllegalStateException();
        }
    }





    /**
     * Here is just a simple regular error message which we
     * can add later for error handling
     * @param errorMessage String
     */
    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("There was an error.");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /**
     * self-explanatory
     */
    public void refreshParticipants() {
        loadParticipants();
    }



}
