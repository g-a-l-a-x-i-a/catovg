package space.rawrclient.catovg.canvas;

import org.junit.jupiter.api.Test;
import space.rawrclient.catovg.color.VGColor;
import space.rawrclient.catovg.composite.VGBlendFactor;
import space.rawrclient.catovg.composite.VGCompositeOp;
import space.rawrclient.catovg.font.VGFont;
import space.rawrclient.catovg.stroke.VGLineCap;
import space.rawrclient.catovg.stroke.VGLineJoin;

import static org.junit.jupiter.api.Assertions.*;

public class VGCanvasTest {

    @Test
    public void testFluentChaining() {
        VGCanvas canvas = VGCanvas.of(0);
        VGFont font = new VGFont(1, "roboto");

        assertSame(canvas, canvas.save());
        assertSame(canvas, canvas.translate(10, 20));
        assertSame(canvas, canvas.rotate(0.5f));
        assertSame(canvas, canvas.scale(2.0f, 2.0f));
        assertSame(canvas, canvas.scissor(0, 0, 100, 100));
        assertSame(canvas, canvas.intersectScissor(10, 10, 50, 50));
        assertSame(canvas, canvas.resetScissor());

        assertSame(canvas, canvas.rect(0, 0, 100, 50, VGColor.BLACK));
        assertSame(canvas, canvas.rectOutline(0, 0, 100, 50, 1.0f, VGColor.WHITE));
        assertSame(canvas, canvas.roundedRect(0, 0, 100, 50, 4.0f, VGColor.BLACK));
        assertSame(canvas, canvas.roundedRectOutline(0, 0, 100, 50, 4.0f, 1.0f, VGColor.WHITE));
        assertSame(canvas, canvas.roundedRectVarying(0, 0, 100, 50, 2, 4, 6, 8, VGColor.BLACK));
        assertSame(canvas, canvas.circle(50, 50, 25, VGColor.RED));
        assertSame(canvas, canvas.circleOutline(50, 50, 25, 2.0f, VGColor.BLUE));
        assertSame(canvas, canvas.ellipse(50, 50, 30, 20, VGColor.GREEN));
        assertSame(canvas, canvas.line(0, 0, 100, 100, 1.0f, VGColor.WHITE));
        assertSame(canvas, canvas.line(0, 0, 100, 100, 1.0f, VGColor.WHITE, VGLineCap.ROUND));
        assertSame(canvas, canvas.arc(50, 50, 20, 0, 3.14f, 1, 2.0f, VGColor.WHITE));
        assertSame(canvas, canvas.quad(0, 0, 10, 0, 10, 10, 0, 10, VGColor.WHITE));

        assertSame(canvas, canvas.bezier(0, 0, 10, 20, 30, 40, 50, 50, 1.0f, VGColor.WHITE));
        assertSame(canvas, canvas.quadCurve(0, 0, 25, 50, 50, 50, 1.0f, VGColor.WHITE));
        assertSame(canvas, canvas.polyline(new float[]{0, 0, 10, 10, 20, 20}, 1.0f, VGColor.WHITE));
        assertSame(canvas, canvas.polyline(new float[]{0, 0, 10, 10, 20, 20}, 1.0f, VGColor.WHITE, VGLineCap.SQUARE, VGLineJoin.BEVEL));
        assertSame(canvas, canvas.polygon(new float[]{0, 0, 10, 0, 5, 10}, VGColor.WHITE));
        assertSame(canvas, canvas.polygonOutline(new float[]{0, 0, 10, 0, 5, 10}, 1.0f, VGColor.WHITE));

        assertSame(canvas, canvas.beginPath());
        assertSame(canvas, canvas.moveTo(0, 0));
        assertSame(canvas, canvas.lineTo(10, 10));
        assertSame(canvas, canvas.bezierTo(1, 2, 3, 4, 5, 6));
        assertSame(canvas, canvas.quadTo(1, 2, 3, 4));
        assertSame(canvas, canvas.arcTo(0, 0, 10, 10, 5));
        assertSame(canvas, canvas.closePath());
        assertSame(canvas, canvas.fill(VGColor.WHITE));
        assertSame(canvas, canvas.stroke(1.0f, VGColor.BLACK));

        assertSame(canvas, canvas.lineCap(VGLineCap.ROUND));
        assertSame(canvas, canvas.lineJoin(VGLineJoin.ROUND));
        assertSame(canvas, canvas.miterLimit(10.0f));
        assertSame(canvas, canvas.globalAlpha(0.8f));
        assertSame(canvas, canvas.compositeOperation(VGCompositeOp.SOURCE_OVER));
        assertSame(canvas, canvas.compositeBlendFunc(VGBlendFactor.SRC_ALPHA, VGBlendFactor.ONE_MINUS_SRC_ALPHA));
        assertSame(canvas, canvas.compositeBlendFuncSeparate(VGBlendFactor.SRC_ALPHA, VGBlendFactor.ONE, VGBlendFactor.ONE, VGBlendFactor.ZERO));

        assertSame(canvas, canvas.shadow(0, 0, 100, 50, 4, 8, 2, VGColor.BLACK));
        assertSame(canvas, canvas.glow(0, 0, 100, 50, 4, VGColor.WHITE));

        assertSame(canvas, canvas.text("test", 0, 0, "sans", 12, VGColor.WHITE));
        assertSame(canvas, canvas.text("test", 0, 0, font, 12, VGColor.WHITE));
        assertSame(canvas, canvas.textBox("wrapped\ntext", 0, 0, 200, "sans", 12, VGColor.WHITE));
        assertSame(canvas, canvas.textBox("wrapped\ntext", 0, 0, 200, font, 12, VGColor.WHITE));
        assertSame(canvas, canvas.textLineHeight(1.2f));
        assertSame(canvas, canvas.textLetterSpacing(0.5f));

        assertSame(canvas, canvas.image(1, 0, 0, 64, 64));
        assertSame(canvas, canvas.image(1, 0, 0, 64, 64, 8.0f, 0.5f));
        assertSame(canvas, canvas.deleteImage(1));

        assertSame(canvas, canvas.restore());
    }

    @Test
    public void testMeasurementDelegation() {
        VGCanvas canvas = VGCanvas.of(0);
        VGFont font = new VGFont(1, "test");

        assertEquals(0.0f, canvas.textWidth("hello", "sans", 14));
        assertEquals(0.0f, canvas.textWidth("hello", font, 14));
        assertEquals(0.0f, canvas.textBoxHeight("hello\nworld", 100, "sans", 14));
        assertEquals(0.0f, canvas.textBoxHeight("hello\nworld", 100, font, 14));
        assertArrayEquals(new float[]{0, 0, 0, 0}, canvas.textBounds("hello", "sans", 14, null));
        assertArrayEquals(new float[]{0, 0, 0, 0}, canvas.textBounds("hello", font, 14, null));
        assertArrayEquals(new float[]{0, 0, 0, 0}, canvas.textBoxBounds("hello", 0, 0, 100, "sans", 14, null));
        assertArrayEquals(new float[]{0, 0, 0, 0}, canvas.textBoxBounds("hello", 0, 0, 100, font, 14, null));
        assertArrayEquals(new int[]{0, 0}, canvas.imageSize(1));
    }
}
