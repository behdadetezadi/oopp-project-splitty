package client.utils;
import client.scenes.MainController;
import java.util.Locale;
import java.util.ResourceBundle;

public interface LanguageChangeListener {
    /**
     * Sets the resource bundle for the current locale.
     * @param resourceBundle The resource bundle to set.
     */
    void setResourceBundle(ResourceBundle resourceBundle);

    /**
     * Updates the active locale for the controller.
     * @param locale The new locale to set as active.
     */
    void setActiveLocale(Locale locale);

    /**
     * Updates the UI elements to reflect changes in the language.
     */
    void updateUIElements();

    /**
     * Retrieves the main controller associated with this listener.
     * @return The main controller.
     */
    MainController getMainController();
}

