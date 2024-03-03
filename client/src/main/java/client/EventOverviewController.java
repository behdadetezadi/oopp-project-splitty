package client;


import client.utils.AnimationUtil;
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
    private Button sendInvitesButton;

    @FXML
    private Button addExpenseButton;



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

        // Initialize options list view
        optionsListView.getItems()
                .addAll("From (selected participant)", "Including (selected participant)");


    }
    private void animateLabels() {
        animateText(titleLabel, "Event Overview");
        animateText(participantsLabel, "Participants");
        animateText(expensesLabel, "Expenses");
        animateText(optionsLabel, "Options");
    }

    private void animateButtonsText() {
        AnimationUtil.animateButton(sendInvitesButton);
        animateButton(addExpenseButton);
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
