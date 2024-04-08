package client;
import javafx.scene.image.Image;

public class Language {
    private String name;
    private Image flag;

    public Language(String name, Image flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public Image getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return name;
    }
}