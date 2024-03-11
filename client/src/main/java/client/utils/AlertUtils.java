package client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Class for client side alerts
 */
public class AlertUtils {

    /**
     * Shows an informational alert
     * @param title title of the alert
     * @param content description of the alert
     */
    public static void showInformationAlert(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }

    /**
     * Shows an error alert
     * @param title title of the alert
     * @param content description of the alert
     */
    public static void showErrorAlert(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }

    /**
     * Generic alert method used by the above specific alert methods
     * @param type type of alert
     * @param title title of the alert
     * @param content description of the alert
     */
    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}