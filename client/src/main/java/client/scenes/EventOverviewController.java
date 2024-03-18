package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static client.utils.AnimationUtil.animateButton;
import static client.utils.AnimationUtil.animateText;

public class EventOverviewController {

    private ServerUtils server;
    private MainController mainController;


    @FXML
    private BorderPane root;

    @FXML
    private ListView<String> participantsListView;

    @FXML
    private ComboBox<String> participantDropdown;

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

    @Inject
    public EventOverviewController(ServerUtils server, MainController mainController) {
        this.server = server;
        this.mainController = mainController;
    }


    /**
     * initializer function does: //TODO
     */
    public void initialize() {
        animateLabels();
        animateButtonsText();

    }

    /**
     * method to switch over to the participant scene when clicked upon
     */
    @FXML
    private void showParticipants() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/client/scenes/TableOfParticipants.fxml"));
            Parent participantRoot = loader.load();
            Scene scene = new Scene(participantRoot);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load the participant scene.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeParticipants() {
        // Assume you have a method to get your participants
        List<String> participants = Arrays.asList("Participant 1",
                "Participant 2", "Participant 3");
        participantsListView.getItems().setAll(participants);
        participantDropdown.getItems().setAll(participants);
    }

    private void initializeOptionsListView() {
        // If you have specific options to show, add them here
        optionsListView.getItems().addAll("Option 1", "Option 2", "Option 3");
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
        // Assuming you want to filter based on a selected participant
        String selectedParticipant = participantDropdown.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            filteredOptions.clear();
            filteredOptions.add("Filtered option for " + selectedParticipant);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invitation Sent");
        alert.setHeaderText(null);
        alert.setContentText("Invitations have been sent to all participants.");
        alert.showAndWait();
    }
    /**
     * edit participant method //TODO
     */
    @FXML
    public void editParticipants() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Participant Edited");
        alert.setHeaderText(null);
        alert.setContentText("The participant's details have been updated successfully.");
        alert.showAndWait();
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Expense Added");
        alert.setHeaderText(null);
        alert.setContentText("A new expense has been added successfully.");
        alert.showAndWait();
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


}
