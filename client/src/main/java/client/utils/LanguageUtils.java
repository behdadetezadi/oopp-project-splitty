package client.utils;

import client.Language;
import client.scenes.MainController;
import client.scenes.StartPageController;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static client.utils.AlertUtils.showErrorAlert;

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
        if(language.equals("Download Template")){
            generateTemplateFile(Locale.ENGLISH);
            return;
        }
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
     * @param languageComboBox The ComboBox to change
     */
    public static void switchToNextLanguage(Locale currentLocale, LanguageChangeListener listener,
                                            ComboBox<Language> languageComboBox) {
        List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.GERMAN, new Locale("nl"));
        int currentLocaleIndex = locales.indexOf(currentLocale);
        Locale nextLocale = (currentLocaleIndex == -1 || currentLocaleIndex == locales.size() - 1)
                ? locales.get(0)
                : locales.get(currentLocaleIndex + 1);
        loadLanguage(nextLocale, listener);
        String nextLanguageName = localeToLanguageName(nextLocale);
        for (Language lang : languageComboBox.getItems()) {
            if (lang.getName().equals(nextLanguageName)) {
                languageComboBox.setValue(lang);
                break;
            }
        }
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

    private static void generateTemplateFile(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("message", locale);
        Set<String> keys = bundle.keySet();
        String languageKey = "yourLanguageKey";
        String folderName = "splitty_files"; // Name of the folder
        String filePath = System.getProperty("user.home") + File.separator + folderName + File.separator + "message_" + languageKey + ".properties";

        try {

            File folder = new File(System.getProperty("user.home") + File.separator + folderName);
            if (!folder.exists()) {
                folder.mkdirs(); // Create the folder and any necessary parent folders
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.println("# Instructions: Fill in the translations for each key below and send your file to the developer via email.\n" +
                        "#After inspection your language will be added to the application\n"+
                        "#please make sure to change the term yourLanguageKey to the respective locale code.\n"
                        + "#in language. key fill in the languages display name according to its own locale");
                writer.println();

                for (String key : keys) {
                    writer.println(key + "=");
                }
            } catch (IOException e) {
                showErrorAlert("File Error", "IO Error", "Error making and writing the file");
            }

            try {
                File file = new File(filePath);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                showErrorAlert("File Error", "IO Error", "Error reading the file");
            }
        } catch (Exception e) {
            showErrorAlert("File Error", "IO Error", "Error making and writing the file");
        }
    }

}
