package client;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AnimationUtil {



    public static void animateButton(Button button) {
        String text = button.getText();
        animateText(button, text);
    }



    public static void animateTextField(TextField textField) {
        String text = textField.getPromptText();
        animateText(textField, text);
    }

    /**
     * animation for typing text
     * @param label a label text
     * @param text the content of the text in type string
     */

    public static void animateText(Label label, String text) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.05));
        StringBuilder stringBuilder = new StringBuilder();
        int[] currentIndex = {0};
        pauseTransition.setOnFinished(event -> {
            if (currentIndex[0] < text.length()) {
                stringBuilder.append(text.charAt(currentIndex[0]));
                label.setText(stringBuilder.toString());
                currentIndex[0]++;
                pauseTransition.play();
            }
        });
        pauseTransition.play();
    }

    public static void animateText(Button button, String text) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.05));
        StringBuilder stringBuilder = new StringBuilder();
        int[] currentIndex = {0};
        pauseTransition.setOnFinished(event -> {
            if (currentIndex[0] < text.length()) {
                stringBuilder.append(text.charAt(currentIndex[0]));
                button.setText(stringBuilder.toString());
                currentIndex[0]++;
                pauseTransition.play();
            }
        });
        pauseTransition.play();
    }


    public static void animateText(TextField textField, String text) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.05));
        StringBuilder stringBuilder = new StringBuilder();
        int[] currentIndex = {0};
        pauseTransition.setOnFinished(event -> {
            if (currentIndex[0] < text.length()) {
                stringBuilder.append(text.charAt(currentIndex[0]));
                textField.setPromptText(stringBuilder.toString());
                currentIndex[0]++;
                pauseTransition.play();
            }
        });
        pauseTransition.play();
    }



}
