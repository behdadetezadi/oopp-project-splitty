package client;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

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
    public void initialize() {

        animateLabels();

        // Initialize participants list view
        participantsListView.getItems().addAll("Participant 1", "Participant 2", "Participant 3");

        // Initialize participant dropdown
        participantDropdown.getItems().addAll("Participant 1", "Participant 2", "Participant 3");

        // Initialize options list view
        optionsListView.getItems().addAll("From (selected participant)", "Including (selected participant)");

        /*
        Image editImage = new Image(getClass().getClassLoader().getResourceAsStream("EditIcon.png"));
        Image addImage = new Image(getClass().getClassLoader().getResourceAsStream("AddParticipantNeoIcon.png"));

        // Set images as graphics for buttons
        editButton.setGraphic(new ImageView(editImage));
        addButton.setGraphic(new ImageView(addImage));
        */

    }
    private void animateLabels() {
        AnimationUtil.animateText(titleLabel, "Event Overview");
        AnimationUtil.animateText(participantsLabel, "Participants");
        AnimationUtil.animateText(expensesLabel, "Expenses");
        AnimationUtil.animateText(optionsLabel, "Options");
    }
    @FXML
    public void sendInvites() {
        // Action for sending invites
    }

    @FXML
    public void editParticipants() {
        // Action for editing participants
    }

    @FXML
    public void addParticipant() {
        // Action for adding a new participant
    }

    @FXML
    public void addExpense() {
        // Action for adding an expense
    }
}
