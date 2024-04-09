package client.scenes;

import client.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LoginController {

    @FXML
    private ImageView logo;

    @FXML
    private Button userLoginButton;

    @FXML
    private Button adminLoginButton;

    private MainController mainController;
    private String adminPassword;

    /**
     * Sets up the controller after the FXML file is loaded.
     */
    public void initialize() {
        adminPassword = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Admin password: " + adminPassword);

        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("images/MatrixGif.gif")));
        logo.setImage(image);
    }

    /**
     * Transitions to the start page upon user login action.
     * TODO add language switch
     */
    @FXML
    private void handleUserLogin() {
        mainController.showStartPage();
    }

    /**
     * Displays admin login prompt and processes authentication.
     */
    @FXML
    private void handleAdminLoginPrompt() {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Admin Login");
        passwordDialog.setHeaderText("Admin Authentication");
        passwordDialog.setContentText("Please enter admin password:");
        String cssPath = this.getClass().getResource("/styles.css").toExternalForm();
        passwordDialog.getDialogPane().getScene().getStylesheets().add(cssPath);

        Optional<String> result = passwordDialog.showAndWait();
        result.ifPresent(password -> {
            if (password.equals(adminPassword)) {
                mainController.showAdminPage();
            } else {
                AlertUtils.showErrorAlert("Invalid Password", "Error", "The password you have entered is incorrect, please try again.");
            }
        });
    }

    /**
     * Injects the MainController into this controller.
     * @param mainController The main controller to be used.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
