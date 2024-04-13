package client.utils;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class KeyboardUtils {
    /**
     * adds default keyboard shortcuts for a scene
     * supports the ESCAPE key and an optional action for a custom key combination
     * @param scene           The scene to which the keyboard shortcuts are added
     * @param goBackAction    The action to be performed when the ESCAPE key is pressed
     * @param optionalAction  An optional action to be performed when a specified custom key combination is pressed
     * @param optionalShortcut A custom {@link KeyCombination} that triggers the optionalAction
     */
    public static void addDefaultKeyboardShortcuts(Scene scene, Runnable goBackAction,
                                                   Runnable optionalAction, KeyCombination optionalShortcut) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            final KeyCombination goBackCombination = new KeyCodeCombination(KeyCode.ESCAPE);
            if (goBackCombination.match(event)) {
                goBackAction.run();
                event.consume();
            }
            else if (optionalShortcut != null && optionalShortcut.match(event)) {
                if (optionalAction != null) {
                    optionalAction.run();
                }
                event.consume();
            }
        });
    }

    /**
     * Adds a simple keyboard shortcut for going back (using the ESCAPE key) to a scene.
     * This method is a convenience wrapper around
     * {@link #addDefaultKeyboardShortcuts(Scene, Runnable, Runnable, KeyCombination)}
     * @param scene        The scene to which the keyboard shortcut is added
     * @param goBackAction The action to be performed when the ESCAPE key is pressed
     */
    public static void addSimpleBackShortcut(Scene scene, Runnable goBackAction) {
        addDefaultKeyboardShortcuts(scene, goBackAction, null, null);
    }
}
