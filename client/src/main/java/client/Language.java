package client;
import javafx.scene.image.Image;

public class Language {
    private String name;
    private Image flag;

    /**
     * language constructor
     * @param name name
     * @param flag flag
     */
    public Language(String name, Image flag) {
        this.name = name;
        this.flag = flag;
    }

    /**
     * getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getter
     * @return flag
     */
    public Image getFlag() {
        return flag;
    }

    /**
     * to string
     * @return name
     */
    @Override
    public String toString() {
        return name;
    }
}