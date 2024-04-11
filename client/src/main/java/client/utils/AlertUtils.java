package client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

/**
 * Class for client side alerts
 */
public class AlertUtils {
    /**
     * A generic method used by specific alert methods to show an alert of a given type.
     * @param type The type of the alert.
     * @param title The title of the alert.
     * @param header The header text of the alert; can be {@code null}.
     * @param content The content description of the alert.
     */
    public static void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtils.class.getResource("/styles.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("alertDialog");
        alert.showAndWait();
    }
    /**
     * Shows a success alert with an optional header.
     * @param title The title of the alert.
     * @param header The header text of the alert; can be {@code null} if no header is needed.
     * @param content The content description of the alert.
     */
    public static void showSuccessAlert(String title, String header, String content) {
        showAlert(AlertType.INFORMATION, title, header, content);
    }


    /**
     * Shows an informational alert with an optional header.
     * @param title The title of the alert.
     * @param header The header text of the alert; can be {@code null} if no header is needed.
     * @param content The content description of the alert.
     */
    public static void showInformationAlert(String title, String header, String content) {
        showAlert(AlertType.INFORMATION, title, header, content);
    }

    /**
     * Shows an error alert with an optional header.
     * @param title The title of the alert.
     * @param header The header text of the alert; can be {@code null} if no header is needed.
     * @param content The content description of the alert.
     */
    public static void showErrorAlert(String title, String header, String content) {
        showAlert(AlertType.ERROR, title, header, content);
    }

    /**
     * Shows a confirmation alert and waits for the user's response.
     * @param title The title of the alert.
     * @param content The content text of the alert.
     * @return {@code true} if the user clicked YES, {@code false} otherwise.
     */
    public static boolean showConfirmationAlert(String title, String content) {
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle(title);
        DialogPane dialogPane = confirmDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtils.class.getResource("/styles.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> response = confirmDialog.showAndWait();
        return response.filter(buttonType -> buttonType == ButtonType.YES).isPresent();
    }
}
