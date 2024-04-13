package client.utils;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.util.Optional;

public class KeyboardUtils {
    /**
     * Adds flexible keyboard shortcuts to a specified scene
     * includes a mandatory shortcut for the ESCAPE key and other optional custom shortcuts
     * @param scene        The scene to which the keyboard shortcuts are added
     * @param goBackAction The action to be performed when the ESCAPE key is pressed
     * @param shortcuts    Varargs parameter that allows specifying multiple key-action pairs. Each pair includes a
     *                     {@link KeyCombination} that defines the shortcut, and a {@link Runnable} that defines the action
     *                     to be performed when the shortcut is triggered.
     */
    @SafeVarargs
    public static void addKeyboardShortcuts(Scene scene, Runnable goBackAction,
                                                    Pair<KeyCombination, Runnable>... shortcuts) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            final KeyCombination goBackCombination = new KeyCodeCombination(KeyCode.ESCAPE);
            if (goBackCombination.match(event) && goBackAction != null) {
                goBackAction.run();
                event.consume();
            } else {
                for (Pair<KeyCombination, Runnable> shortcut : shortcuts) {
                    if (shortcut.getKey().match(event)) {
                        if (shortcut.getValue() != null) {
                            shortcut.getValue().run();
                        }
                        event.consume();
                        break;
                    }
                }
            }
        });
    }

    /**
     * Adds a simple 'back' keyboard shortcut
     * allows the user to go back to previous page by pressing the ESCAPE key
     * @param scene The scene to which the keyboard shortcut is added
     * @param goBackAction The action to be performed when the ESCAPE key is pressed
     */
    public static void addSimpleBackShortcut(Scene scene, Runnable goBackAction) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (new KeyCodeCombination(KeyCode.ESCAPE).match(event)) {
                goBackAction.run();
                event.consume();
            }
        });
    }
}
