package space.rawrclient.catovg.enums;

import org.junit.jupiter.api.Test;
import org.lwjgl.nanovg.NanoVG;
import space.rawrclient.catovg.composite.VGBlendFactor;
import space.rawrclient.catovg.composite.VGCompositeOp;
import space.rawrclient.catovg.image.VGImageFlags;
import space.rawrclient.catovg.stroke.VGLineCap;
import space.rawrclient.catovg.stroke.VGLineJoin;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VGEnumsTest {

    @Test
    public void testLineCaps() {
        assertEquals(NanoVG.NVG_BUTT, VGLineCap.BUTT.id);
        assertEquals(NanoVG.NVG_ROUND, VGLineCap.ROUND.id);
        assertEquals(NanoVG.NVG_SQUARE, VGLineCap.SQUARE.id);
    }

    @Test
    public void testLineJoins() {
        assertEquals(NanoVG.NVG_MITER, VGLineJoin.MITER.id);
        assertEquals(NanoVG.NVG_ROUND, VGLineJoin.ROUND.id);
        assertEquals(NanoVG.NVG_BEVEL, VGLineJoin.BEVEL.id);
    }

    @Test
    public void testCompositeOps() {
        assertEquals(NanoVG.NVG_SOURCE_OVER, VGCompositeOp.SOURCE_OVER.id);
        assertEquals(NanoVG.NVG_SOURCE_IN, VGCompositeOp.SOURCE_IN.id);
        assertEquals(NanoVG.NVG_SOURCE_OUT, VGCompositeOp.SOURCE_OUT.id);
        assertEquals(NanoVG.NVG_ATOP, VGCompositeOp.ATOP.id);
        assertEquals(NanoVG.NVG_DESTINATION_OVER, VGCompositeOp.DESTINATION_OVER.id);
        assertEquals(NanoVG.NVG_DESTINATION_IN, VGCompositeOp.DESTINATION_IN.id);
        assertEquals(NanoVG.NVG_DESTINATION_OUT, VGCompositeOp.DESTINATION_OUT.id);
        assertEquals(NanoVG.NVG_DESTINATION_ATOP, VGCompositeOp.DESTINATION_ATOP.id);
        assertEquals(NanoVG.NVG_LIGHTER, VGCompositeOp.LIGHTER.id);
        assertEquals(NanoVG.NVG_COPY, VGCompositeOp.COPY.id);
        assertEquals(NanoVG.NVG_XOR, VGCompositeOp.XOR.id);
    }

    @Test
    public void testBlendFactors() {
        assertEquals(NanoVG.NVG_ZERO, VGBlendFactor.ZERO.id);
        assertEquals(NanoVG.NVG_ONE, VGBlendFactor.ONE.id);
        assertEquals(NanoVG.NVG_SRC_COLOR, VGBlendFactor.SRC_COLOR.id);
        assertEquals(NanoVG.NVG_ONE_MINUS_SRC_COLOR, VGBlendFactor.ONE_MINUS_SRC_COLOR.id);
        assertEquals(NanoVG.NVG_DST_COLOR, VGBlendFactor.DST_COLOR.id);
        assertEquals(NanoVG.NVG_ONE_MINUS_DST_COLOR, VGBlendFactor.ONE_MINUS_DST_COLOR.id);
        assertEquals(NanoVG.NVG_SRC_ALPHA, VGBlendFactor.SRC_ALPHA.id);
        assertEquals(NanoVG.NVG_ONE_MINUS_SRC_ALPHA, VGBlendFactor.ONE_MINUS_SRC_ALPHA.id);
        assertEquals(NanoVG.NVG_DST_ALPHA, VGBlendFactor.DST_ALPHA.id);
        assertEquals(NanoVG.NVG_ONE_MINUS_DST_ALPHA, VGBlendFactor.ONE_MINUS_DST_ALPHA.id);
        assertEquals(NanoVG.NVG_SRC_ALPHA_SATURATE, VGBlendFactor.SRC_ALPHA_SATURATE.id);
    }

    @Test
    public void testImageFlags() {
        assertEquals(NanoVG.NVG_IMAGE_GENERATE_MIPMAPS, VGImageFlags.GENERATE_MIPMAPS);
        assertEquals(NanoVG.NVG_IMAGE_REPEATX, VGImageFlags.REPEATX);
        assertEquals(NanoVG.NVG_IMAGE_REPEATY, VGImageFlags.REPEATY);
        assertEquals(NanoVG.NVG_IMAGE_FLIPY, VGImageFlags.FLIPY);
        assertEquals(NanoVG.NVG_IMAGE_PREMULTIPLIED, VGImageFlags.PREMULTIPLIED);
        assertEquals(NanoVG.NVG_IMAGE_NEAREST, VGImageFlags.NEAREST);
    }
}
