package space.rawrclient.catovg.composite;

import org.lwjgl.nanovg.NanoVG;

/**
 * Composite operations for blending NanoVG draws onto the framebuffer.
 */
public enum VGCompositeOp {

    /** Default source-over blending. */
    SOURCE_OVER(NanoVG.NVG_SOURCE_OVER),

    /** Source restricted to overlapping destination pixels. */
    SOURCE_IN(NanoVG.NVG_SOURCE_IN),

    /** Source restricted to non-overlapping destination pixels. */
    SOURCE_OUT(NanoVG.NVG_SOURCE_OUT),

    /** Source over destination within destination boundaries. */
    ATOP(NanoVG.NVG_ATOP),

    /** Destination over source. */
    DESTINATION_OVER(NanoVG.NVG_DESTINATION_OVER),

    /** Destination restricted to overlapping source pixels. */
    DESTINATION_IN(NanoVG.NVG_DESTINATION_IN),

    /** Destination restricted to non-overlapping source pixels. */
    DESTINATION_OUT(NanoVG.NVG_DESTINATION_OUT),

    /** Destination over source within source boundaries. */
    DESTINATION_ATOP(NanoVG.NVG_DESTINATION_ATOP),

    /** Additive color blending. */
    LIGHTER(NanoVG.NVG_LIGHTER),

    /** Copies source directly, discarding destination. */
    COPY(NanoVG.NVG_COPY),

    /** Exclusive-or blending. */
    XOR(NanoVG.NVG_XOR);

    /** The native NanoVG constant. */
    public final int id;

    VGCompositeOp(int id) {
        this.id = id;
    }
}
