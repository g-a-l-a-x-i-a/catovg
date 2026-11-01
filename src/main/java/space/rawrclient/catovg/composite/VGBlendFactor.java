package space.rawrclient.catovg.composite;

import org.lwjgl.nanovg.NanoVG;

/**
 * Blend factors for custom composite blend functions in NanoVG.
 */
public enum VGBlendFactor {

    /** Factor 0.0. */
    ZERO(NanoVG.NVG_ZERO),

    /** Factor 1.0. */
    ONE(NanoVG.NVG_ONE),

    /** Factor source color. */
    SRC_COLOR(NanoVG.NVG_SRC_COLOR),

    /** Factor 1.0 - source color. */
    ONE_MINUS_SRC_COLOR(NanoVG.NVG_ONE_MINUS_SRC_COLOR),

    /** Factor destination color. */
    DST_COLOR(NanoVG.NVG_DST_COLOR),

    /** Factor 1.0 - destination color. */
    ONE_MINUS_DST_COLOR(NanoVG.NVG_ONE_MINUS_DST_COLOR),

    /** Factor source alpha. */
    SRC_ALPHA(NanoVG.NVG_SRC_ALPHA),

    /** Factor 1.0 - source alpha. */
    ONE_MINUS_SRC_ALPHA(NanoVG.NVG_ONE_MINUS_SRC_ALPHA),

    /** Factor destination alpha. */
    DST_ALPHA(NanoVG.NVG_DST_ALPHA),

    /** Factor 1.0 - destination alpha. */
    ONE_MINUS_DST_ALPHA(NanoVG.NVG_ONE_MINUS_DST_ALPHA),

    /** Factor min(source alpha, 1.0 - destination alpha). */
    SRC_ALPHA_SATURATE(NanoVG.NVG_SRC_ALPHA_SATURATE);

    /** The native NanoVG constant. */
    public final int id;

    VGBlendFactor(int id) {
        this.id = id;
    }
}
