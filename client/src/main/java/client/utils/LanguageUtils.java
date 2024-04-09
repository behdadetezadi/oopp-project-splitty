package client.utils;

import javafx.scene.control.ComboBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtils {
    /**
     * Loads the resource bundle for the specified locale and updates the UI elements.
     * @param locale The locale to load the language resources for.
     * @param listener The listener that will have its locale and resource bundle updated.
     */
    public static void loadLanguage(Locale locale, LanguageChangeListener listener) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("message", locale);
        listener.setResourceBundle(resourceBundle);
        listener.setActiveLocale(locale);
        listener.updateUIElements();
        listener.getMainController().storeLanguagePreference(locale);
    }

    /**
     * Switches the language based on the selected language string.
     * @param language The language string to switch to.
     * @param listener The listener that will have its language switched.
     * @param languageComboBox The language box
     */
    public static void switchLanguage(String language, LanguageChangeListener listener, ComboBox<String> languageComboBox) {
        Locale locale;
        switch (language) {
            case "English":
                locale = Locale.ENGLISH;
                break;
            case "Deutsch":
                locale = Locale.GERMAN;
                break;
            case "Nederlands":
                locale = new Locale("nl");
                break;
            default:
                locale = Locale.ENGLISH;
                break;
        }
        loadLanguage(locale, listener);
    }

    /**
     * Configures a ComboBox for language selection. When a new language is selected,
     * the language is switched using the switchLanguage method.
     * @param languageComboBox The ComboBox to configure.
     * @param listener The LanguageChangeListener that will be notified of the switch.
     */
    public static void configureLanguageComboBox(ComboBox<String> languageComboBox, LanguageChangeListener listener) {
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            switchLanguage(selectedLanguage, listener, languageComboBox);
        });
    }
}
