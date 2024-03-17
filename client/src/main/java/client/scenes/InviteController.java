package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Window;
import commons.Event;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class InviteController implements Initializable {

    @FXML Label title;
    @FXML Label inviteCode;

    @FXML
    private TextArea emailsField;

    @FXML
    private Button submitButton;


    private Event event;

    /**
     * handler for the submit button.
     *
     * @param event button press
     * @return an array list containing all email addresses to be processed further
     */
    @FXML
    public ArrayList<String> handleSubmitButtonAction(ActionEvent event) {
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
                    "tell them to bring me my money");
        }
        return emailAddresses;
    }

    /**
     * initializes the invite scene with correct title and invite code.
     * todo: is semi static for now, needs show correct attributes
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        event = new commons.Event(null, null, "Watching paint dry", 12345);
        title.setText(event.getTitle());
        inviteCode.setText(String.valueOf(event.getInviteCode()));
    }
}
