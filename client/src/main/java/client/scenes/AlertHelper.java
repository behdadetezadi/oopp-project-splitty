package client.scenes;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertHelper {

    /**
     *  helper method that shows alerts
     * @param alertType self-explanatory
     * @param owner the window which generates this alert
     * @param title title of the shown error
     * @param message error message
     */
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}