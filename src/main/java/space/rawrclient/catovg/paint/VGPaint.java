package space.rawrclient.catovg.paint;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import space.rawrclient.catovg.color.VGColor;

import java.util.Objects;

/**
 * Lightweight handle to a NanoVG paint (gradient or image pattern)
 * <p>
 * Native paint structs come from a per thread ring buffer of
 * {@value #POOL_SIZE} entries. a {@code VGPaint} stays valid until that many
 * further paints are created on the same thread, so paints should be created
 * and used within the same frame and never cached long term.
 */
public final class VGPaint {

    /** Pool size: must be a power of 2 so the index can wrap with a bit mask */
    private static final int POOL_SIZE = 16;
    private static final int POOL_MASK = POOL_SIZE - 1;

    private static final ThreadLocal<State> STATE = ThreadLocal.withInitial(State::new);

    private final NVGPaint nativePaint;

    private VGPaint(NVGPaint nativePaint) {
        this.nativePaint = nativePaint;
    }

    /**
     * Returns the underlying native paint struct.
     *
     * @return the native paint
     */
    public NVGPaint nvg() {
        return nativePaint;
    }

    /**
     * Creates a linear gradient between 2 points.
     *
     * @param vg    NanoVG context handle
     * @param sx    start x
     * @param sy    start y
     * @param ex    end x
     * @param ey    end y
     * @param start color at the start point
     * @param end   color at the end point
     * @return the paint
     */
    public static VGPaint linearGradient(long vg, float sx, float sy, float ex, float ey,
                                         VGColor start, VGColor end) {
        State state = STATE.get();
        NVGPaint target = state.nextPaint();
        NanoVG.nvgLinearGradient(vg, sx, sy, ex, ey, state.inner(start), state.outer(end), target);
        return new VGPaint(target);
    }

    /**
     * Creates a box gradient, usually used for drop shadows and soft edges.
     *
     * @param vg      NanoVG context handle
     * @param x       box x
     * @param y       box y
     * @param w       box width
     * @param h       box height
     * @param radius  corner radius
     * @param feather blur distance across the edge
     * @param start   inner color
     * @param end     outer color
     * @return the paint
     */
    public static VGPaint boxGradient(long vg, float x, float y, float w, float h,
                                      float radius, float feather, VGColor start, VGColor end) {
        State state = STATE.get();
        NVGPaint target = state.nextPaint();
        NanoVG.nvgBoxGradient(vg, x, y, w, h, radius, feather, state.inner(start), state.outer(end), target);
        return new VGPaint(target);
    }

    /**
     * Creates a radial gradient.
     *
     * @param vg    NanoVG context handle
     * @param cx    center x
     * @param cy    center y
     * @param inR   inner radius
     * @param outR  outer radius
     * @param start color at the inner radius
     * @param end   color at the outer radius
     * @return the paint
     */
    public static VGPaint radialGradient(long vg, float cx, float cy, float inR, float outR,
                                         VGColor start, VGColor end) {
        State state = STATE.get();
        NVGPaint target = state.nextPaint();
        NanoVG.nvgRadialGradient(vg, cx, cy, inR, outR, state.inner(start), state.outer(end), target);
        return new VGPaint(target);
    }

    /**
     * Creates an image pattern.
     *
     * @param vg    NanoVG context handle
     * @param ox    pattern origin x
     * @param oy    pattern origin y
     * @param w     width of one image tile
     * @param h     height of one image tile
     * @param angle rotation in radians
     * @param image NanoVG image handle
     * @param alpha opacity in {@code [0, 1]}
     * @return the paint
     */
    public static VGPaint imagePattern(long vg, float ox, float oy, float w, float h,
                                       float angle, int image, float alpha) {
        NVGPaint target = STATE.get().nextPaint();
        NanoVG.nvgImagePattern(vg, ox, oy, w, h, angle, image, alpha, target);
        return new VGPaint(target);
    }

    /** Per thread scratch memory: the paint ring buffer plus 2 color slots. */
    private static final class State {
        private final NVGPaint[] paints = new NVGPaint[POOL_SIZE];
        private final NVGColor innerColor = NVGColor.create();
        private final NVGColor outerColor = NVGColor.create();
        private int index;

        State() {
            for (int i = 0; i < POOL_SIZE; i++) {
                paints[i] = NVGPaint.create();
            }
        }

        NVGPaint nextPaint() {
            NVGPaint paint = paints[index];
            index = (index + 1) & POOL_MASK;
            return paint;
        }

        NVGColor inner(VGColor color) {
            return Objects.requireNonNull(color, "start").toNVG(innerColor);
        }

        NVGColor outer(VGColor color) {
            return Objects.requireNonNull(color, "end").toNVG(outerColor);
        }
    }
}
