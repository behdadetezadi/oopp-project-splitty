package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static client.utils.AnimationUtil.animateButton;
import static client.utils.AnimationUtil.animateText;

public class EventOverviewController {
    private ServerUtils server;
    private MainController mainController;
    private Event event;

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
//    @FXML
//    private Button editButton;
//    @FXML
//    private Button addButton;
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

    private Stage primaryStage;

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
    public void initialize() {
        if (event != null) {
            titleLabel.setText(event.getTitle());
            titleLabel.setOnMouseClicked(event -> editTitle());
//            initializeParticipants();
            //inviteCodeLabel.setText(String.valueOf(event.getInviteCode()));
        }
        showExpensesButton.setOnAction(this::showExpensesForSelectedParticipant);
    }

    /**
     * called by startPage and other pages when setting up this page
     * @param event event to be set
     */
    public void setEvent(Event event) {
        this.event = event;
        initialize();
        loadParticipants();
        animateLabels();
        animateButtonsText();
    }

    /**
     * loads participants / Trying something and commented out intiialize participants methods
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
        dialog.setTitle("Edit Title");
        dialog.setHeaderText(null);
        dialog.setContentText("New Title:");

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
            mainController.showTableOfParticipants(this.event);

        } catch (IllegalStateException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load the participant scene.");
        }
    }

    private void initializeParticipants() {
        List<Participant> participants = event.getPeople();
        if (participants != null) {
            List<ParticipantOption> participantOptions = participants.stream()
                    .map(p -> new ParticipantOption(p.getId(), p.getFirstName()))
                    .collect(Collectors.toList());
            participantDropdown.getItems().setAll(participantOptions);
        }
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
        animateText(participantsLabel, "Participants");
        animateText(expensesLabel, "Expenses");
        animateText(optionsLabel, "Options");
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
     * sendInvites method
     */
    @FXML
    public void sendInvites() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/client/scenes/inviteScene.fxml"));
            Parent inviteRoot = loader.load();
            Scene scene = new Scene(inviteRoot);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);

            InviteController controller = loader.getController();
            controller.initData(this.event);
            stage.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load the invite scene.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //TODO
    /**
     *
     * @param event Actionevent?
     */
    @FXML
    public void addExpense(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            mainController.showAddExpense(this.event);
        } else {
            throw new IllegalStateException();
        }
    }

    @FXML
    private void showExpensesForSelectedParticipant(ActionEvent event) {
        ParticipantOption selectedOption = participantDropdown.getSelectionModel().getSelectedItem();
        if (selectedOption == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Participant Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a participant to view their expenses.");
            alert.showAndWait();
            return;
        }

        long selectedParticipantId = selectedOption.getId();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/ParticipantExpenseOverview.fxml"));
            Parent root = loader.load();
            ExpenseController controller = loader.getController();
            controller.initializeExpensesForParticipant(selectedParticipantId); 

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
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
     * self explanatory
     */
    public void refreshParticipants() {
        loadParticipants();
    }


}
