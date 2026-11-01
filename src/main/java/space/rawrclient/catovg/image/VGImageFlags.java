package space.rawrclient.catovg.image;

import org.lwjgl.nanovg.NanoVG;

/**
 * Image generation and sampling flags for NanoVG textures.
 */
public final class VGImageFlags {

    /** Generate mipmaps for the image texture. */
    public static final int GENERATE_MIPMAPS = NanoVG.NVG_IMAGE_GENERATE_MIPMAPS;

    /** Repeat the image horizontally. */
    public static final int REPEATX = NanoVG.NVG_IMAGE_REPEATX;

    /** Repeat the image vertically. */
    public static final int REPEATY = NanoVG.NVG_IMAGE_REPEATY;

    /** Flip the image vertically along the y-axis. */
    public static final int FLIPY = NanoVG.NVG_IMAGE_FLIPY;

    /** Image data has premultiplied alpha colors. */
    public static final int PREMULTIPLIED = NanoVG.NVG_IMAGE_PREMULTIPLIED;

    /** Use nearest-neighbor interpolation instead of bilinear filtering. */
    public static final int NEAREST = NanoVG.NVG_IMAGE_NEAREST;

    private VGImageFlags() {
    }
}
