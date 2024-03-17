package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Scanner;

public class InviteController {

    @FXML
    private TextArea emailsField;

    @FXML
    private Button submitButton;

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
}
