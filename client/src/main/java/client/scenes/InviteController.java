package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Scanner;

public class InviteController  {
    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;

    private Event event;

    @FXML
    private Label title;
    @FXML
    private Label inviteCode;

    @FXML
    private TextArea emailsField;

    @FXML
    private Button submitButton;

    @FXML
    private AnchorPane root;


    /**
     * invite Controller injection
     * @param primaryStage primary stage
     * @param server server
     * @param mainController maincontroller
     */
    @Inject
    public InviteController(Stage primaryStage,ServerUtils server, MainController mainController) {
        this.primaryStage = primaryStage;
        this.server = server;
        this.mainController = mainController;
    }

    /**
     * handler for the submit button.
     *
     * @return an array list containing all email addresses to be processed further
     */
    @FXML
    public ArrayList<String> handleSubmitButtonAction() {
        Window owner = submitButton.getScene().getWindow();

        if(emailsField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter email addresses!");
            return null;
        }

        String emailAddressesAsString = emailsField.getText();
        Scanner scanner = new Scanner(emailAddressesAsString);
        ArrayList<String> emailAddresses = new ArrayList<>();
        boolean flag = false; // hack. needs a better alternative
        while(scanner.hasNext()) {
            String temp = scanner.next();

            if (!temp.contains("@") || !temp.contains(".")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "non-valid email!",
                        temp + " is not a valid email address!");
                flag = true;
            } else {
                emailAddresses.add(temp);
            }
        }

        if (!flag) {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "invites send!",
                    "MONEY!!!");
        }
        return emailAddresses;
    }




    /**
     * method that sets title and invite code according to passed event
     * @param event the event
     */
    public void initData(Event event) {
        this.event = event;
        inviteCode.setPrefWidth(Double.MAX_VALUE);
        inviteCode.setText(String.valueOf(event.getInviteCode()));
        title.setText(event.getTitle());
    }


    /**
     * handler of the button that takes you back to the overview scene
     *
     */
    @FXML
    public void handleBackButtonAction() {
        try {
            mainController.showEventOverview(event);
        } catch (IllegalStateException e) {
            e.printStackTrace();

        }
    }
}
