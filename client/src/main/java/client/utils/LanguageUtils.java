package client.utils;

import client.Language;
import client.scenes.MainController;
import client.scenes.StartPageController;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

import java.util.*;

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
     */
    public static void switchLanguage(String language, LanguageChangeListener listener) {
        Locale locale = switch (language) {
            case "Deutsch" -> Locale.GERMAN;
            case "Nederlands" -> new Locale("nl");
            default -> Locale.ENGLISH;
        };
        loadLanguage(locale, listener);
    }

    /**
     * Changes the language to the next one in the list, rolling over to the first after the last.
     * @param currentLocale The current locale
     * @param listener The LanguageChangeListener that will have its language switched
     */
    public static void switchToNextLanguage(Locale currentLocale, LanguageChangeListener listener) {
        List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.GERMAN, new Locale("nl"));
        int currentLocaleIndex = locales.indexOf(currentLocale);
        Locale nextLocale = (currentLocaleIndex == -1 || currentLocaleIndex == locales.size() - 1)
                ? locales.get(0)
                : locales.get(currentLocaleIndex + 1);
        loadLanguage(nextLocale, listener);
    }

    /**
     * Converts a Locale to a displayable language name for the ComboBox.
     * Adjust the mapping to match your ComboBox items.
     * @param locale The locale to convert.
     * @return A String representing the displayable language name.
     */
    public static String localeToLanguageName(Locale locale) {
        if (Locale.ENGLISH.equals(locale)) {
            return "English";
        } else if (Locale.GERMAN.equals(locale)) {
            return "Deutsch";
        } else if ("nl".equals(locale.getLanguage())) {
            return "Nederlands";
        } else {
            return "English";
        }
    }

    /**
     * Configures a ComboBox for language selection. When a new language is selected,
     * the language is switched using the switchLanguage method.
     * @param languageComboBox The ComboBox to configure.
     * @param listener The LanguageChangeListener that will be notified of the switch.
     */
    public static void configureLanguageComboBox(ComboBox<Language> languageComboBox, LanguageChangeListener listener) {
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue().getName();
            switchLanguage(selectedLanguage, listener);
        });
    }

    /**
     * Populate a ComboBox for language selection with the languages and flags
     * @param activeLocale The locale to load the language resources for.
     * @param languageComboBox The ComboBox to populate.
     */
    public static void populateLanguageComboBox(Locale activeLocale, ComboBox<Language> languageComboBox) {
        String languageName = LanguageUtils.localeToLanguageName(activeLocale);
        List<Language> languages = new ArrayList<>();
        languages.add(new Language("English",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/english.png")))));
        languages.add(new Language("Deutsch",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/german.png")))));
        languages.add(new Language("Nederlands",
                new Image(Objects.requireNonNull(LanguageUtils.class.getClassLoader()
                        .getResourceAsStream("images/flags/dutch.png")))));
        for (Language language : languages) {
            if (language.getName().equals(languageName)) {
                languageComboBox.setValue(language);
                break;
            }
        }
        languageComboBox.setItems(FXCollections.observableArrayList(languages));
    }
}
