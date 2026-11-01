package space.rawrclient.catovg.draw;

import org.junit.jupiter.api.Test;
import space.rawrclient.catovg.color.VGColor;
import space.rawrclient.catovg.composite.VGBlendFactor;
import space.rawrclient.catovg.composite.VGCompositeOp;
import space.rawrclient.catovg.font.VGFont;
import space.rawrclient.catovg.stroke.VGLineCap;
import space.rawrclient.catovg.stroke.VGLineJoin;

import static org.junit.jupiter.api.Assertions.*;

public class VGDrawTest {

    @Test
    public void testSafeWithZeroContext() {
        assertDoesNotThrow(() -> {
            VGDraw.rect(0, 0, 0, 10, 10, VGColor.WHITE);
            VGDraw.roundedRect(0, 0, 0, 10, 10, 2, VGColor.WHITE);
            VGDraw.circle(0, 5, 5, 5, VGColor.WHITE);
            VGDraw.line(0, 0, 0, 10, 10, 1, VGColor.WHITE);
            VGDraw.line(0, 0, 0, 10, 10, 1, VGColor.WHITE, VGLineCap.ROUND);
            VGDraw.bezier(0, 0, 0, 2, 2, 8, 8, 10, 10, 1, VGColor.WHITE);
            VGDraw.quadCurve(0, 0, 0, 5, 5, 10, 10, 1, VGColor.WHITE);
            VGDraw.polyline(0, new float[]{0, 0, 10, 10}, 1, VGColor.WHITE);
            VGDraw.polyline(0, new float[]{0, 0, 10, 10}, 1, VGColor.WHITE, VGLineCap.ROUND, VGLineJoin.ROUND);
            VGDraw.polygon(0, new float[]{0, 0, 10, 0, 5, 10}, VGColor.WHITE);
            VGDraw.polygonOutline(0, new float[]{0, 0, 10, 0, 5, 10}, 1, VGColor.WHITE);
            VGDraw.lineCap(0, VGLineCap.ROUND);
            VGDraw.lineJoin(0, VGLineJoin.ROUND);
            VGDraw.miterLimit(0, 10.0f);
            VGDraw.globalAlpha(0, 0.5f);
            VGDraw.compositeOperation(0, VGCompositeOp.LIGHTER);
            VGDraw.compositeBlendFunc(0, VGBlendFactor.SRC_ALPHA, VGBlendFactor.ONE);
            VGDraw.compositeBlendFuncSeparate(0, VGBlendFactor.SRC_ALPHA, VGBlendFactor.ONE, VGBlendFactor.ONE, VGBlendFactor.ZERO);
            VGDraw.text(0, "test", 0, 0, "sans", 12, VGColor.WHITE);
            VGDraw.textBox(0, "multiline\ntest", 0, 0, 100, "sans", 12, VGColor.WHITE);
            VGDraw.deleteImage(0, 1);
            VGDraw.image(0, 1, 0, 0, 50, 50);
        });
    }

    @Test
    public void testSafeNullHandling() {
        assertDoesNotThrow(() -> {
            VGDraw.rect(1, 0, 0, 10, 10, (VGColor) null);
            VGDraw.polyline(1, null, 1, VGColor.WHITE);
            VGDraw.polyline(1, new float[]{0, 0}, 1, VGColor.WHITE);
            VGDraw.polygon(1, null, VGColor.WHITE);
            VGDraw.polygon(1, new float[]{0, 0}, VGColor.WHITE);
            VGDraw.text(1, null, 0, 0, "sans", 12, VGColor.WHITE);
            VGDraw.textBox(1, null, 0, 0, 100, "sans", 12, VGColor.WHITE);
        });
    }

    @Test
    public void testMeasurementZeroHandling() {
        assertEquals(0.0f, VGDraw.textWidth(0, "hello", "sans", 14));
        assertEquals(0.0f, VGDraw.textWidth(1, "", "sans", 14));

        float[] bounds = VGDraw.textBounds(0, "hello", "sans", 14, null);
        assertArrayEquals(new float[]{0, 0, 0, 0}, bounds);

        float[] boxBounds = VGDraw.textBoxBounds(0, "hello\nworld", 0, 0, 100, "sans", 14, null);
        assertArrayEquals(new float[]{0, 0, 0, 0}, boxBounds);

        assertEquals(0.0f, VGDraw.textBoxHeight(0, "hello\nworld", 100, "sans", 14));
        assertEquals(0.0f, VGDraw.textBoxHeight(1, "", 100, "sans", 14));

        VGFont dummyFont = new VGFont(1, "test");
        assertEquals(0.0f, VGDraw.textBoxHeight(0, "hello", 100, dummyFont, 14));
        assertEquals(0.0f, VGDraw.textWidth(0, "hello", dummyFont, 14));
    }

    @Test
    public void testImageSizeDefaults() {
        int[] size = VGDraw.imageSize(0, 1, null);
        assertArrayEquals(new int[]{0, 0}, size);

        int[] scratch = VGDraw.imageSize(0, 0);
        assertArrayEquals(new int[]{0, 0}, scratch);
    }
}
