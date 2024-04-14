package client.utils;

import javafx.scene.control.ComboBox;

import java.util.Map;
import java.util.ResourceBundle;

public class TagUtils {
    public static final String TAG_FOOD = "food";
    public static final String TAG_ENTRANCE_FEES = "entrance_fees";
    public static final String TAG_TRAVEL = "travel";
    public static final String TAG_OTHER = "other";

    /**
     * initializes tags to their language mappings
     * @param resourceBundle resource bundle
     * @param tagKeysToLocalized tags
     */
    public static void initializeTagLanguageMapping(ResourceBundle resourceBundle, Map<String, String> tagKeysToLocalized) {
        tagKeysToLocalized.clear();
        tagKeysToLocalized.put(TagUtils.TAG_FOOD, resourceBundle.getString("Food"));
        tagKeysToLocalized.put(TagUtils.TAG_ENTRANCE_FEES, resourceBundle.getString("Entrance_fees"));
        tagKeysToLocalized.put(TagUtils.TAG_TRAVEL, resourceBundle.getString("Travel"));
        tagKeysToLocalized.put(TagUtils.TAG_OTHER, resourceBundle.getString("Other"));
    }

    /**
     * initializes the combo box with the tags in the local language
     * @param resourceBundle resource bundle
     * @param comboBox combo box to fill
     * @param tagKeysToLocalized localised tags
     */
    public static void initializeTagsComboBox(ResourceBundle resourceBundle,
                                              ComboBox<String> comboBox, Map<String, String> tagKeysToLocalized) {
        comboBox.getItems().clear();
        comboBox.getItems().add(tagKeysToLocalized.get(TagUtils.TAG_FOOD));
        comboBox.getItems().add(tagKeysToLocalized.get(TagUtils.TAG_ENTRANCE_FEES));
        comboBox.getItems().add(tagKeysToLocalized.get(TagUtils.TAG_TRAVEL));
        comboBox.getItems().add(tagKeysToLocalized.get(TagUtils.TAG_OTHER));
        comboBox.setPromptText(resourceBundle.getString("Tag"));
    }
}