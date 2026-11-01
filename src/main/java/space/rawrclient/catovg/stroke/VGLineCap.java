package space.rawrclient.catovg.stroke;

import org.lwjgl.nanovg.NanoVG;

/**
 * Line cap styles for stroking paths in NanoVG.
 */
public enum VGLineCap {

    /** Flat edge capped at end points. */
    BUTT(NanoVG.NVG_BUTT),

    /** Rounded semicircle capped at end points. */
    ROUND(NanoVG.NVG_ROUND),

    /** Squared edge extending past end points by half the stroke width. */
    SQUARE(NanoVG.NVG_SQUARE);

    /** The native NanoVG constant. */
    public final int id;

    VGLineCap(int id) {
        this.id = id;
    }
}
