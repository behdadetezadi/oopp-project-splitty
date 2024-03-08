package client.scenes;
import client.utils.AnimationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import static client.utils.AnimationUtil.animateButton;
import static client.utils.AnimationUtil.animateText;

public class EventOverviewController {

    @FXML
    private BorderPane root;

    @FXML
    private ListView<String> participantsListView;

    @FXML
    private ComboBox<String> participantDropdown;

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
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private Button filterOne;

    @FXML
    private Button filterTwo;

    @FXML
    private Button sendInvitesButton;

    @FXML
    private Button addExpenseButton;

    @FXML
    private final ObservableList<String> allOptions = FXCollections
            .observableArrayList("1", "2");
    @FXML
    private final ObservableList<String> filteredOptions = FXCollections.observableArrayList();

    /**
     * initializer function does: //TODO
     */
    public void initialize() {

        animateLabels();
        animateButtonsText();

        // Initialize participants list view
        participantsListView.getItems().addAll("Participant 1", "Participant 2", "Participant 3");

        // Initialize participant dropdown
        participantDropdown.getItems().addAll("Participant 1", "Participant 2", "Participant 3");

        // Set maximum height of participantsListView to fit its items
        participantsListView.setMaxHeight(150);

        // Initialize options list view


        // Set maximum height of optionsListView to fit its items
        optionsListView.setMaxHeight(250);

    }

    /**
     * animates the labels using AnimationUtil
     */
    private void animateLabels() {
        animateText(titleLabel, "Event Overview");
        animateText(participantsLabel, "Participants");
        animateText(expensesLabel, "Expenses");
        animateText(optionsLabel, "Options");
    }

    /**
     * animates the buttons using AnimationUtil
     */
    private void animateButtonsText() {
        animateButton(sendInvitesButton);
        animateButton(addExpenseButton);
        animateButton(filterOne);
        animateButton(filterTwo);
    }

    //TODO needs changing to better fit functionality and backlog
    /**
     * apply filters for options list
     */
    @FXML
    private void applyFromFilter() {
        String selectedParticipant = null; // Get selected participant
        if (selectedParticipant != null) {
            filteredOptions.clear();
            filteredOptions.add("From " + selectedParticipant);
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
            filteredOptions.add("Including " + selectedParticipant);
            optionsListView.setItems(filteredOptions);
        }
    }

    /**
     * sendInvites method //TODO
      */
    @FXML
    public void sendInvites() {
        // Action for sending invites
    }

    /**
     * edit participant method //TODO
     */
    @FXML
    public void editParticipants() {
        // Action for editing participants
    }

    /**
     * add participant //TODO
     */
    @FXML
    public void addParticipant() {
        // Action for adding a new participant
    }

    /**
     * add expense //TODO
     */
    @FXML
    public void addExpense() {
        // Action for adding an expense
    }
}
