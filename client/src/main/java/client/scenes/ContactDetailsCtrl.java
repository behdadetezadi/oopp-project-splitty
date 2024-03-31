package client.scenes;

import java.math.BigInteger;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ContactDetailsCtrl {

    private ServerUtils server;
    private MainController mainController;
    private Stage primaryStage;
    private Locale activeLocale;
    private ResourceBundle resourceBundle;
    @FXML
    private Label contactDetails;
    @FXML
    private Label addEdit;
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

    private Event event;



    /**
     *
     * @param primaryStage primary stage
     * @param server server
     * @param mainController main controller
     */
    @Inject
    public ContactDetailsCtrl(Stage primaryStage,ServerUtils server, MainController mainController) {
        this.server = server;
        this.mainController = mainController;
        this.primaryStage = primaryStage;
    }


    /**
     * sets the event and calls initialize (used by maincontroller)
     * @param event event
     */
    public void setEvent(Event event, Locale locale) {
        this.activeLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle("message", locale);
        updateUIElements();
        this.event = event;
        initialize();
    }
    public void updateUIElements() {
        contactDetails.setText(resourceBundle.getString("Contact_Details"));
        addEdit.setText(resourceBundle.getString("Add/Edit_Participant"));
        userNameField.setPromptText(resourceBundle.getString("User_Name"));
        firstNameField.setPromptText(resourceBundle.getString("First_Name"));
        lastNameField.setPromptText(resourceBundle.getString("Last_Name"));
        cancelButton.setText(resourceBundle.getString("Cancel"));
        contactButton.setText(resourceBundle.getString("Ok"));

    }

    /**
     * Initializer method
     */
    @FXML
    public void initialize() {
//        cancelButton.setOnAction(this::clearFields);  -- change later
//        contactButton.setOnAction(this::ok);
    }

    /**
     * Method for checking if the information entered is okay in the contact
     * details page
     * @param actionEvent ActionEvent
     */
    public void ok(ActionEvent actionEvent) {
        String userName = userNameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();

        // validate email
        if(!validateEmail((email))){
//            AlertUtils.showErrorAlert("Invalid email",
//            "Please enter an email of the format: username@domain.com");
            System.out.println("wrong mail"); // for testing, can remove later
            return;
        }
        // validate iban
        if(!validateIban(iban)){
//            AlertUtils.showErrorAlert("Invalid IBAN", "Please enter a valid IBAN");
            System.out.println("wrong iban"); // for testing, can remove later
            return;
        }
        // validate bic
        if(!validateBIC(bic)){
//            AlertUtils.showErrorAlert("Invalid BIC",
//            "Please enter a BIC of the format: AAAABBCCXXX");
            System.out.println("wrong bic"); // for testing, can remove later
            return;
        }
        try {
            Participant participant =  new Participant(userName,
                    firstName, lastName, email, iban, bic,
                new HashMap<>(), new HashMap<>(), new HashSet<>(), "English");
            System.out.println("test"); // for testing, can remove later
            ServerUtils.addParticipant(participant);

            //TODO switch to the next page
//            switchToStartPageScene(actionEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println("hello"); // for testing, can remove later
        }
    }

    /**
     * This method makes sure the email is of a correct format
     * might need to be moved to validationUtils later
     * @param emailInput the user's input
     * @return whether it is of a correct format or not
     */
    public static boolean validateEmail(String emailInput) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regexPattern);
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
         //Switching from Long to BigInteger allows handling of IBANs'
        // numeric conversions beyond Long's limit, ensuring accurate validation without overflow errors.
        BigInteger ibanValue = new BigInteger(numericIBAN.toString());
        return ibanValue.mod(BigInteger.valueOf(97)).equals(BigInteger.ONE);
    }

    /**
     * This method makes sure that the entered BIC is a valid one
     * might need to be moved to validationUtils later
     * @param bicInput the input from the user
     * @return whether it is a valid BIC or not
     */
    public static boolean validateBIC(String bicInput) {
        String validBIC = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$";
        Pattern pattern = Pattern.compile(validBIC);
        Matcher matcher = pattern.matcher(bicInput);
        return matcher.matches();
    }

    /**
     * switches to the startPage scene, probably needs to be moved to the mainController later
     * @param event an event
     */
    //TODO use maincontroller
    public void switchToStartPageScene(javafx.event.ActionEvent event){
        try {
            root = FXMLLoader.load(Objects.requireNonNull
                    (getClass().getResource("client/scenes/startPage.fxml")));
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
        // TODO go back to the scene before
    }

}