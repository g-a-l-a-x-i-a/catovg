package space.rawrclient.catovg.resource;

import org.junit.jupiter.api.Test;
import space.rawrclient.catovg.font.VGFont;
import space.rawrclient.catovg.image.VGImage;

import static org.junit.jupiter.api.Assertions.*;

public class VGResourceTest {

    @Test
    public void testFontValidation() {
        assertThrows(IllegalArgumentException.class, () -> new VGFont(-1, "test"));
        assertThrows(NullPointerException.class, () -> new VGFont(1, null));

        VGFont font = new VGFont(2, "arial");
        assertEquals(2, font.handle());
        assertEquals("arial", font.name());
        assertTrue(font.isValid());
    }

    @Test
    public void testImageValidation() {
        assertThrows(IllegalArgumentException.class, () -> new VGImage(0, 0, 100, 100));
        assertThrows(IllegalArgumentException.class, () -> new VGImage(0, -1, 100, 100));

        VGImage image = new VGImage(0, 1, 64, 32);
        assertEquals(1, image.handle());
        assertEquals(64, image.width());
        assertEquals(32, image.height());
        assertTrue(image.isValid());

        assertDoesNotThrow(image::delete);
        assertDoesNotThrow(image::close);
    }
}
