package client.scenes;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ContactDetailsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;
    @FXML
    private Button contactButton;
    @FXML
    private Button cancelButton;
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * QuoteOverview controller
     * @param server   ServerUtils type
     * @param mainCtrl MainCtrl type
     */
    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializer method
     */
    @FXML
    public void initialize() {
//        cancelButton.setOnAction(this::clearFields);  -- change later
        contactButton.setOnAction(this::ok);
    }

    public void ok(ActionEvent actionEvent) {
        String userName = userNameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();

        // validate email
        if(!validateEmail((email))){
//            AlertUtils.showErrorAlert("Invalid email", "Please enter an email of the format: username@domain.com");
            return;
        }
        // validate iban
        if(!validateIban(iban)){
//            AlertUtils.showErrorAlert("Invalid IBAN", "Please enter a valid IBAN");
            return;
        }
        // validate bic
        if(!validateBIC(bic)){
//            AlertUtils.showErrorAlert("Invalid BIC", "Please enter a BIC of the format: AAAABBCCXXX");
            return;
        }
        try {
            Participant participant =  new Participant(userName, firstName, lastName, email, iban, bic,
                new HashMap<>(), new HashMap<>(), new HashSet<>(), "English");
            ServerUtils.addParticipant(participant);
//            switchToStartPageScene(actionEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * This method makes sure the email is of a correct format
     * might need to be moved to validationUtils later
     * @param emailInput the user's input
     * @return whether it is of a correct format or not
     */
    public static boolean validateEmail(String emailInput) {
        String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(REGEX_PATTERN);
        Matcher matcher = pattern.matcher(emailInput);
        return matcher.matches();
    }

    /**
     * This method checks if the IBAN is valid
     * might need to be moved to validationUtils later
     * @param ibanInput the user input
     * @return whether it is a valid IBAN or not
     */
    public static boolean validateIban(String ibanInput) {
        ibanInput = ibanInput.replaceAll("\\s", "").toUpperCase(); // remove unnecessary spaces
        if(ibanInput.length() < 15 || ibanInput.length() > 34) { // iban must be in this range
            return false;
        }
        if(!ibanInput.matches("^[A-Z]{2}.*")) { // it has to start with a county code of 2 letters
            return false;
        }
        String rearrangedIBAN = ibanInput.substring(4) + ibanInput.substring(0, 4);
        StringBuilder numericIBAN = new StringBuilder();
        for(char c : rearrangedIBAN.toCharArray()) {
            if(Character.isLetter(c)) {
                numericIBAN.append(Character.getNumericValue(c));
            } else {
                numericIBAN.append(c);
            }
        }
        long ibanValue = Long.parseLong(numericIBAN.toString());
        return ibanValue % 97 == 1; // iban is valid if the numeric value % 97 == 1
    }

    /**
     * This method makes sure that the entered BIC is a valid one
     * might need to be moved to validationUtils later
     * @param bicInput the input from the user
     * @return whether it is a valid BIC or not
     */
    public static boolean validateBIC(String bicInput) {
        String VALID_BIC = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$";
        Pattern pattern = Pattern.compile(VALID_BIC);
        Matcher matcher = pattern.matcher(bicInput);
        return matcher.matches();
    }

    /**
     * switches to the startPage scene, probably needs to be moved to the mainController later
     * @param event an event
     */
    public void switchToStartPageScene(javafx.event.ActionEvent event){
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client/scenes/startPage.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * clears all the fields
     */
    @FXML
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        userNameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
    }

}