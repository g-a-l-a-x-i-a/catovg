package space.rawrclient.catovg.stroke;

import org.lwjgl.nanovg.NanoVG;

/**
 * Line join styles for stroking connected path segments in NanoVG.
 */
public enum VGLineJoin {

    /** Sharp corner extended to miter limit. */
    MITER(NanoVG.NVG_MITER),

    /** Rounded corner transition. */
    ROUND(NanoVG.NVG_ROUND),

    /** Beveled flat corner transition. */
    BEVEL(NanoVG.NVG_BEVEL);

    /** The native NanoVG constant. */
    public final int id;

    VGLineJoin(int id) {
        this.id = id;
    }
}
