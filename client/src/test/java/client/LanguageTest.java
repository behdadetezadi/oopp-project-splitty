package client;

import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class LanguageTest {

    @Test
    public void testLanguageConstructorAndGetters() {
        Image mockImage = mock(Image.class);
        Language language = new Language("English", mockImage);
        assertEquals("English", language.getName());
        assertEquals(mockImage, language.getFlag());
    }

    @Test
    public void testToString() {
        Image mockImage = mock(Image.class);
        Language language = new Language("English", mockImage);
        assertEquals("English", language.toString());
    }
}
