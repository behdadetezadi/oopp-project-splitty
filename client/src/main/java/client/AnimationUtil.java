package client;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class AnimationUtil {

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
}
