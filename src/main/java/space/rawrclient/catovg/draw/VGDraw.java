package space.rawrclient.catovg.draw;

import org.lwjgl.nanovg.NanoVG;
import space.rawrclient.catovg.color.VGColor;
import space.rawrclient.catovg.composite.VGBlendFactor;
import space.rawrclient.catovg.composite.VGCompositeOp;
import space.rawrclient.catovg.font.VGFont;
import space.rawrclient.catovg.image.VGImage;
import space.rawrclient.catovg.paint.VGGradient;
import space.rawrclient.catovg.paint.VGPaint;
import space.rawrclient.catovg.path.VGPath;
import space.rawrclient.catovg.stroke.VGLineCap;
import space.rawrclient.catovg.stroke.VGLineJoin;

import java.nio.FloatBuffer;
import java.util.function.LongConsumer;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Stateless immediate mode drawing helpers on top of NanoVG.
 * <p>
 * Every method accepts a raw NanoVG context handle and silently does nothing
 * when the handle is {@code 0} or the supplied color/paint is {@code null},
 * which makes the helpers safe to call before a context is available.
 */
public final class VGDraw {

    /** Default text alignment: left edge, top of the em box. */
    public static final int DEFAULT_TEXT_ALIGN = NVG_ALIGN_LEFT | NVG_ALIGN_TOP;

    private static final ThreadLocal<float[]> SCRATCH_BOUNDS = ThreadLocal.withInitial(() -> new float[4]);
    private static final ThreadLocal<int[]> SCRATCH_SIZE = ThreadLocal.withInitial(() -> new int[2]);
    private static final ThreadLocal<float[]> SCRATCH_ASC = ThreadLocal.withInitial(() -> new float[1]);
    private static final ThreadLocal<float[]> SCRATCH_DESC = ThreadLocal.withInitial(() -> new float[1]);
    private static final ThreadLocal<float[]> SCRATCH_LINEH = ThreadLocal.withInitial(() -> new float[1]);

    private VGDraw() {
    }

    /**
     * Fills a rectangle with a solid color.
     *
     * @param vg    NanoVG context handle
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @param color fill color
     */
    public static void rect(long vg, float x, float y, float w, float h, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgRect(vg, x, y, w, h);
        fill(vg, color);
    }

    /**
     * Fills a rectangle with a paint.
     *
     * @param vg    NanoVG context handle
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @param paint fill paint
     */
    public static void rect(long vg, float x, float y, float w, float h, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgRect(vg, x, y, w, h);
        fill(vg, paint);
    }

    /**
     * Fills a rectangle with a multi-stop gradient.
     *
     * @param vg       NanoVG context handle
     * @param x        left edge
     * @param y        top edge
     * @param w        width
     * @param h        height
     * @param gradient multi-stop gradient
     */
    public static void rect(long vg, float x, float y, float w, float h, VGGradient gradient) {
        if (vg == 0 || gradient == null) return;
        fillGradient(vg, gradient, () -> {
            nvgBeginPath(vg);
            nvgRect(vg, x, y, w, h);
        });
    }

    /**
     * Strokes the outline of a rectangle.
     *
     * @param vg          NanoVG context handle
     * @param x           left edge
     * @param y           top edge
     * @param w           width
     * @param h           height
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void rectOutline(long vg, float x, float y, float w, float h, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgRect(vg, x, y, w, h);
        stroke(vg, strokeWidth, color);
    }

    public static void rectOutline(long vg, float x, float y, float w, float h, float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgRect(vg, x, y, w, h);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Fills a rounded rectangle with a solid color.
     *
     * @param vg     NanoVG context handle
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param color  fill color
     */
    public static void roundedRect(long vg, float x, float y, float w, float h, float radius, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgRoundedRect(vg, x, y, w, h, radius);
        fill(vg, color);
    }

    /**
     * Fills a rounded rectangle with a paint.
     *
     * @param vg     NanoVG context handle
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param paint  fill paint
     */
    public static void roundedRect(long vg, float x, float y, float w, float h, float radius, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgRoundedRect(vg, x, y, w, h, radius);
        fill(vg, paint);
    }

    /**
     * Fills a rounded rectangle with a multi-stop gradient.
     *
     * @param vg       NanoVG context handle
     * @param x        left edge
     * @param y        top edge
     * @param w        width
     * @param h        height
     * @param radius   corner radius
     * @param gradient multi-stop gradient
     */
    public static void roundedRect(long vg, float x, float y, float w, float h, float radius, VGGradient gradient) {
        if (vg == 0 || gradient == null) return;
        fillGradient(vg, gradient, () -> {
            nvgBeginPath(vg);
            nvgRoundedRect(vg, x, y, w, h, radius);
        });
    }

    /**
     * Strokes the outline of a rounded rectangle.
     *
     * @param vg          NanoVG context handle
     * @param x           left edge
     * @param y           top edge
     * @param w           width
     * @param h           height
     * @param radius      corner radius
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void roundedRectOutline(long vg, float x, float y, float w, float h, float radius,
                                          float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgRoundedRect(vg, x, y, w, h, radius);
        stroke(vg, strokeWidth, color);
    }

    public static void roundedRectOutline(long vg, float x, float y, float w, float h, float radius,
                                          float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgRoundedRect(vg, x, y, w, h, radius);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Fills a rounded rectangle with a separate radius per corner.
     *
     * @param vg             NanoVG context handle
     * @param x              left edge
     * @param y              top edge
     * @param w              width
     * @param h              height
     * @param radTopLeft     top-left radius
     * @param radTopRight    top-right radius
     * @param radBottomRight bottom-right radius
     * @param radBottomLeft  bottom-left radius
     * @param color          fill color
     */
    public static void roundedRectVarying(long vg, float x, float y, float w, float h,
                                          float radTopLeft, float radTopRight,
                                          float radBottomRight, float radBottomLeft, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgRoundedRectVarying(vg, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft);
        fill(vg, color);
    }

    /**
     * Fills a rounded rectangle with a separate radius per corner using a paint.
     *
     * @param vg             NanoVG context handle
     * @param x              left edge
     * @param y              top edge
     * @param w              width
     * @param h              height
     * @param radTopLeft     top-left radius
     * @param radTopRight    top-right radius
     * @param radBottomRight bottom-right radius
     * @param radBottomLeft  bottom-left radius
     * @param paint          fill paint
     */
    public static void roundedRectVarying(long vg, float x, float y, float w, float h,
                                          float radTopLeft, float radTopRight,
                                          float radBottomRight, float radBottomLeft, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgRoundedRectVarying(vg, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft);
        fill(vg, paint);
    }

    /**
     * Strokes the outline of a rounded rectangle with separate corner radiuses.
     *
     * @param vg             NanoVG context handle
     * @param x              left edge
     * @param y              top edge
     * @param w              width
     * @param h              height
     * @param radTopLeft     top-left radius
     * @param radTopRight    top-right radius
     * @param radBottomRight bottom-right radius
     * @param radBottomLeft  bottom-left radius
     * @param strokeWidth    stroke width
     * @param color          stroke color
     */
    public static void roundedRectVaryingOutline(long vg, float x, float y, float w, float h,
                                                 float radTopLeft, float radTopRight,
                                                 float radBottomRight, float radBottomLeft,
                                                 float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgRoundedRectVarying(vg, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes the outline of a rounded rectangle with separate corner radiuses using a paint.
     *
     * @param vg             NanoVG context handle
     * @param x              left edge
     * @param y              top edge
     * @param w              width
     * @param h              height
     * @param radTopLeft     top-left radius
     * @param radTopRight    top-right radius
     * @param radBottomRight bottom-right radius
     * @param radBottomLeft  bottom-left radius
     * @param strokeWidth    stroke width
     * @param paint          stroke paint
     */
    public static void roundedRectVaryingOutline(long vg, float x, float y, float w, float h,
                                                 float radTopLeft, float radTopRight,
                                                 float radBottomRight, float radBottomLeft,
                                                 float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgRoundedRectVarying(vg, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Fills a circle with a solid color.
     *
     * @param vg     NanoVG context handle
     * @param cx     center x
     * @param cy     center y
     * @param radius radius
     * @param color  fill color
     */
    public static void circle(long vg, float cx, float cy, float radius, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgCircle(vg, cx, cy, radius);
        fill(vg, color);
    }

    /**
     * Fills a circle with a paint.
     *
     * @param vg     NanoVG context handle
     * @param cx     center x
     * @param cy     center y
     * @param radius radius
     * @param paint  fill paint
     */
    public static void circle(long vg, float cx, float cy, float radius, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgCircle(vg, cx, cy, radius);
        fill(vg, paint);
    }

    /**
     * Fills a circle with a multi-stop gradient.
     *
     * @param vg       NanoVG context handle
     * @param cx       center x
     * @param cy       center y
     * @param radius   radius
     * @param gradient multi-stop gradient
     */
    public static void circle(long vg, float cx, float cy, float radius, VGGradient gradient) {
        if (vg == 0 || gradient == null) return;
        fillGradient(vg, gradient, () -> {
            nvgBeginPath(vg);
            nvgCircle(vg, cx, cy, radius);
        });
    }

    /**
     * Strokes the outline of a circle.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void circleOutline(long vg, float cx, float cy, float radius, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgCircle(vg, cx, cy, radius);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes the outline of a circle with a paint.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void circleOutline(long vg, float cx, float cy, float radius, float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgCircle(vg, cx, cy, radius);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Alias for {@link #circleOutline(long, float, float, float, float, VGColor)}.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void ring(long vg, float cx, float cy, float radius, float strokeWidth, VGColor color) {
        circleOutline(vg, cx, cy, radius, strokeWidth, color);
    }

    /**
     * Alias for {@link #circleOutline(long, float, float, float, float, VGPaint)}.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void ring(long vg, float cx, float cy, float radius, float strokeWidth, VGPaint paint) {
        circleOutline(vg, cx, cy, radius, strokeWidth, paint);
    }

    /**
     * Fills an ellipse with a solid color.
     *
     * @param vg    NanoVG context handle
     * @param cx    center x
     * @param cy    center y
     * @param rx    horizontal radius
     * @param ry    vertical radius
     * @param color fill color
     */
    public static void ellipse(long vg, float cx, float cy, float rx, float ry, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgEllipse(vg, cx, cy, rx, ry);
        fill(vg, color);
    }

    /**
     * Fills an ellipse with a paint.
     *
     * @param vg    NanoVG context handle
     * @param cx    center x
     * @param cy    center y
     * @param rx    horizontal radius
     * @param ry    vertical radius
     * @param paint fill paint
     */
    public static void ellipse(long vg, float cx, float cy, float rx, float ry, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgEllipse(vg, cx, cy, rx, ry);
        fill(vg, paint);
    }

    /**
     * Strokes the outline of an ellipse with a solid color.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param rx          horizontal radius
     * @param ry          vertical radius
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void ellipseOutline(long vg, float cx, float cy, float rx, float ry, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgEllipse(vg, cx, cy, rx, ry);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes the outline of an ellipse with a paint.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param rx          horizontal radius
     * @param ry          vertical radius
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void ellipseOutline(long vg, float cx, float cy, float rx, float ry, float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgEllipse(vg, cx, cy, rx, ry);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Strokes a straight line segment.
     *
     * @param vg          NanoVG context handle
     * @param x1          start x
     * @param y1          start y
     * @param x2          end x
     * @param y2          end y
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void line(long vg, float x1, float y1, float x2, float y2, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes a straight line segment with a custom line cap.
     *
     * @param vg          NanoVG context handle
     * @param x1          start x
     * @param y1          start y
     * @param x2          end x
     * @param y2          end y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap style
     */
    public static void line(long vg, float x1, float y1, float x2, float y2, float strokeWidth, VGColor color, VGLineCap cap) {
        if (vg == 0 || color == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        line(vg, x1, y1, x2, y2, strokeWidth, color);
    }

    public static void line(long vg, float x1, float y1, float x2, float y2, float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        stroke(vg, strokeWidth, paint);
    }

    public static void line(long vg, float x1, float y1, float x2, float y2, float strokeWidth, VGPaint paint, VGLineCap cap) {
        if (vg == 0 || paint == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        line(vg, x1, y1, x2, y2, strokeWidth, paint);
    }

    /**
     * Strokes a circular arc.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param a0          start angle in radians
     * @param a1          end angle in radians
     * @param dir         winding direction ({@link NanoVG#NVG_CW} or {@link NanoVG#NVG_CCW})
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void arc(long vg, float cx, float cy, float radius, float a0, float a1, int dir,
                           float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgArc(vg, cx, cy, radius, a0, a1, dir);
        stroke(vg, strokeWidth, color);
    }

    public static void arc(long vg, float cx, float cy, float radius, float a0, float a1, int dir,
                           float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgArc(vg, cx, cy, radius, a0, a1, dir);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Fills an arbitrary quadrilateral. Vertices should be given in winding order.
     *
     * @param vg    NanoVG context handle
     * @param x1    first vertex x
     * @param y1    first vertex y
     * @param x2    second vertex x
     * @param y2    second vertex y
     * @param x3    third vertex x
     * @param y3    third vertex y
     * @param x4    fourth vertex x
     * @param y4    fourth vertex y
     * @param color fill color
     */
    public static void quad(long vg, float x1, float y1, float x2, float y2,
                            float x3, float y3, float x4, float y4, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        nvgLineTo(vg, x3, y3);
        nvgLineTo(vg, x4, y4);
        nvgClosePath(vg);
        fill(vg, color);
    }

    /**
     * Fills an arbitrary quadrilateral with a paint. Vertices should be given in winding order.
     *
     * @param vg    NanoVG context handle
     * @param x1    first vertex x
     * @param y1    first vertex y
     * @param x2    second vertex x
     * @param y2    second vertex y
     * @param x3    third vertex x
     * @param y3    third vertex y
     * @param x4    fourth vertex x
     * @param y4    fourth vertex y
     * @param paint fill paint
     */
    public static void quad(long vg, float x1, float y1, float x2, float y2,
                            float x3, float y3, float x4, float y4, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        nvgLineTo(vg, x3, y3);
        nvgLineTo(vg, x4, y4);
        nvgClosePath(vg);
        fill(vg, paint);
    }

    /**
     * Fills a triangle with a solid color.
     *
     * @param vg    NanoVG context handle
     * @param x1    first vertex x
     * @param y1    first vertex y
     * @param x2    second vertex x
     * @param y2    second vertex y
     * @param x3    third vertex x
     * @param y3    third vertex y
     * @param color fill color
     */
    public static void triangle(long vg, float x1, float y1, float x2, float y2, float x3, float y3, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        nvgLineTo(vg, x3, y3);
        nvgClosePath(vg);
        fill(vg, color);
    }

    /**
     * Fills a triangle with a paint.
     *
     * @param vg    NanoVG context handle
     * @param x1    first vertex x
     * @param y1    first vertex y
     * @param x2    second vertex x
     * @param y2    second vertex y
     * @param x3    third vertex x
     * @param y3    third vertex y
     * @param paint fill paint
     */
    public static void triangle(long vg, float x1, float y1, float x2, float y2, float x3, float y3, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        nvgLineTo(vg, x3, y3);
        nvgClosePath(vg);
        fill(vg, paint);
    }

    /**
     * Strokes the outline of a triangle.
     *
     * @param vg          NanoVG context handle
     * @param x1          first vertex x
     * @param y1          first vertex y
     * @param x2          second vertex x
     * @param y2          second vertex y
     * @param x3          third vertex x
     * @param y3          third vertex y
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void triangleOutline(long vg, float x1, float y1, float x2, float y2, float x3, float y3,
                                       float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        nvgLineTo(vg, x3, y3);
        nvgClosePath(vg);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes the outline of a triangle with a paint.
     *
     * @param vg          NanoVG context handle
     * @param x1          first vertex x
     * @param y1          first vertex y
     * @param x2          second vertex x
     * @param y2          second vertex y
     * @param x3          third vertex x
     * @param y3          third vertex y
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void triangleOutline(long vg, float x1, float y1, float x2, float y2, float x3, float y3,
                                       float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, x1, y1);
        nvgLineTo(vg, x2, y2);
        nvgLineTo(vg, x3, y3);
        nvgClosePath(vg);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Fills a capsule / pill primitive where corner radius is {@code min(w, h) * 0.5f}.
     *
     * @param vg    NanoVG context handle
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @param color fill color
     */
    public static void pill(long vg, float x, float y, float w, float h, VGColor color) {
        roundedRect(vg, x, y, w, h, Math.min(w, h) * 0.5f, color);
    }

    /**
     * Fills a capsule / pill primitive where corner radius is {@code min(w, h) * 0.5f} with a paint.
     *
     * @param vg    NanoVG context handle
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @param paint fill paint
     */
    public static void pill(long vg, float x, float y, float w, float h, VGPaint paint) {
        roundedRect(vg, x, y, w, h, Math.min(w, h) * 0.5f, paint);
    }

    /**
     * Strokes the outline of a capsule / pill primitive.
     *
     * @param vg          NanoVG context handle
     * @param x           left edge
     * @param y           top edge
     * @param w           width
     * @param h           height
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void pillOutline(long vg, float x, float y, float w, float h, float strokeWidth, VGColor color) {
        roundedRectOutline(vg, x, y, w, h, Math.min(w, h) * 0.5f, strokeWidth, color);
    }

    /**
     * Strokes the outline of a capsule / pill primitive with a paint.
     *
     * @param vg          NanoVG context handle
     * @param x           left edge
     * @param y           top edge
     * @param w           width
     * @param h           height
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void pillOutline(long vg, float x, float y, float w, float h, float strokeWidth, VGPaint paint) {
        roundedRectOutline(vg, x, y, w, h, Math.min(w, h) * 0.5f, strokeWidth, paint);
    }

    /**
     * Strokes a cubic bezier curve segment.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param c1x         first control point x
     * @param c1y         first control point y
     * @param c2x         second control point x
     * @param c2y         second control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void bezier(long vg, float sx, float sy, float c1x, float c1y, float c2x, float c2y,
                              float ex, float ey, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, sx, sy);
        nvgBezierTo(vg, c1x, c1y, c2x, c2y, ex, ey);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes a cubic bezier curve segment with a paint.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param c1x         first control point x
     * @param c1y         first control point y
     * @param c2x         second control point x
     * @param c2y         second control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void bezier(long vg, float sx, float sy, float c1x, float c1y, float c2x, float c2y,
                              float ex, float ey, float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, sx, sy);
        nvgBezierTo(vg, c1x, c1y, c2x, c2y, ex, ey);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Strokes a cubic bezier curve segment with custom line cap and line join styles.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param c1x         first control point x
     * @param c1y         first control point y
     * @param c2x         second control point x
     * @param c2y         second control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void bezier(long vg, float sx, float sy, float c1x, float c1y, float c2x, float c2y,
                              float ex, float ey, float strokeWidth, VGColor color,
                              VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || color == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        bezier(vg, sx, sy, c1x, c1y, c2x, c2y, ex, ey, strokeWidth, color);
    }

    /**
     * Strokes a cubic bezier curve segment with a paint and custom line cap and line join styles.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param c1x         first control point x
     * @param c1y         first control point y
     * @param c2x         second control point x
     * @param c2y         second control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void bezier(long vg, float sx, float sy, float c1x, float c1y, float c2x, float c2y,
                              float ex, float ey, float strokeWidth, VGPaint paint,
                              VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || paint == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        bezier(vg, sx, sy, c1x, c1y, c2x, c2y, ex, ey, strokeWidth, paint);
    }

    /**
     * Strokes a quadratic bezier curve segment.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param cx          control point x
     * @param cy          control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void quadCurve(long vg, float sx, float sy, float cx, float cy,
                                 float ex, float ey, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, sx, sy);
        nvgQuadTo(vg, cx, cy, ex, ey);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes a quadratic bezier curve segment with a paint.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param cx          control point x
     * @param cy          control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void quadCurve(long vg, float sx, float sy, float cx, float cy,
                                 float ex, float ey, float strokeWidth, VGPaint paint) {
        if (vg == 0 || paint == null) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, sx, sy);
        nvgQuadTo(vg, cx, cy, ex, ey);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Strokes a quadratic bezier curve segment with custom line cap and line join styles.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param cx          control point x
     * @param cy          control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void quadCurve(long vg, float sx, float sy, float cx, float cy,
                                 float ex, float ey, float strokeWidth, VGColor color,
                                 VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || color == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        quadCurve(vg, sx, sy, cx, cy, ex, ey, strokeWidth, color);
    }

    /**
     * Strokes a quadratic bezier curve segment with a paint and custom line cap and line join styles.
     *
     * @param vg          NanoVG context handle
     * @param sx          start point x
     * @param sy          start point y
     * @param cx          control point x
     * @param cy          control point y
     * @param ex          end point x
     * @param ey          end point y
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void quadCurve(long vg, float sx, float sy, float cx, float cy,
                                 float ex, float ey, float strokeWidth, VGPaint paint,
                                 VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || paint == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        quadCurve(vg, sx, sy, cx, cy, ex, ey, strokeWidth, paint);
    }

    /**
     * Strokes a connected series of straight line segments.
     *
     * @param vg          NanoVG context handle
     * @param points      flat array of coordinates [x0, y0, x1, y1, ...]
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void polyline(long vg, float[] points, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null || points == null || points.length < 4) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, points[0], points[1]);
        for (int i = 2; i < points.length - 1; i += 2) {
            nvgLineTo(vg, points[i], points[i + 1]);
        }
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes a connected series of line segments with custom line cap and line join styles.
     *
     * @param vg          NanoVG context handle
     * @param points      flat array of coordinates [x0, y0, x1, y1, ...]
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void polyline(long vg, float[] points, float strokeWidth, VGColor color,
                                VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || color == null || points == null || points.length < 4) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        nvgBeginPath(vg);
        nvgMoveTo(vg, points[0], points[1]);
        for (int i = 2; i < points.length - 1; i += 2) {
            nvgLineTo(vg, points[i], points[i + 1]);
        }
        stroke(vg, strokeWidth, color);
    }

    /**
     * Fills a closed polygon with a solid color.
     *
     * @param vg     NanoVG context handle
     * @param points flat array of coordinates [x0, y0, x1, y1, x2, y2, ...]
     * @param color  fill color
     */
    public static void polygon(long vg, float[] points, VGColor color) {
        if (vg == 0 || color == null || points == null || points.length < 6) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, points[0], points[1]);
        for (int i = 2; i < points.length - 1; i += 2) {
            nvgLineTo(vg, points[i], points[i + 1]);
        }
        nvgClosePath(vg);
        fill(vg, color);
    }

    /**
     * Fills a closed polygon with a paint.
     *
     * @param vg     NanoVG context handle
     * @param points flat array of coordinates [x0, y0, x1, y1, x2, y2, ...]
     * @param paint  fill paint
     */
    public static void polygon(long vg, float[] points, VGPaint paint) {
        if (vg == 0 || paint == null || points == null || points.length < 6) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, points[0], points[1]);
        for (int i = 2; i < points.length - 1; i += 2) {
            nvgLineTo(vg, points[i], points[i + 1]);
        }
        nvgClosePath(vg);
        fill(vg, paint);
    }

    /**
     * Strokes the outline of a closed polygon.
     *
     * @param vg          NanoVG context handle
     * @param points      flat array of coordinates [x0, y0, x1, y1, x2, y2, ...]
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void polygonOutline(long vg, float[] points, float strokeWidth, VGColor color) {
        if (vg == 0 || color == null || points == null || points.length < 6) return;
        nvgBeginPath(vg);
        nvgMoveTo(vg, points[0], points[1]);
        for (int i = 2; i < points.length - 1; i += 2) {
            nvgLineTo(vg, points[i], points[i + 1]);
        }
        nvgClosePath(vg);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Draws a soft drop shadow around a rounded rectangle with custom horizontal and vertical offsets.
     *
     * @param vg          NanoVG context handle
     * @param x           box left edge
     * @param y           box top edge
     * @param w           box width
     * @param h           box height
     * @param radius      box corner radius
     * @param blur        shadow blur distance
     * @param spread      shadow spread distance
     * @param offsetX     horizontal shadow offset
     * @param offsetY     vertical shadow offset
     * @param shadowColor shadow color at the box edge
     */
    public static void shadow(long vg, float x, float y, float w, float h,
                              float radius, float blur, float spread,
                              float offsetX, float offsetY, VGColor shadowColor) {
        if (vg == 0 || shadowColor == null) return;
        float bx = x + offsetX - spread;
        float by = y + offsetY - spread;
        float bw = w + 2.0f * spread;
        float bh = h + 2.0f * spread;
        float br = Math.max(0.0f, radius + spread);
        VGPaint paint = VGPaint.boxGradient(vg, bx, by, bw, bh, br, blur, shadowColor, VGColor.TRANSPARENT);

        float minX = Math.min(x, bx - blur);
        float minY = Math.min(y, by - blur);
        float maxX = Math.max(x + w, bx + bw + blur);
        float maxY = Math.max(y + h, by + bh + blur);

        nvgBeginPath(vg);
        nvgRect(vg, minX, minY, maxX - minX, maxY - minY);
        if (radius > 0.0f) {
            nvgRoundedRect(vg, x, y, w, h, radius);
        } else {
            nvgRect(vg, x, y, w, h);
        }
        nvgPathWinding(vg, NVG_HOLE);
        fill(vg, paint);
    }

    /**
     * Draws a soft drop shadow around a rounded rectangle.
     *
     * @param vg          NanoVG context handle
     * @param x           box left edge
     * @param y           box top edge
     * @param w           box width
     * @param h           box height
     * @param radius      box corner radius
     * @param blur        shadow blur distance
     * @param spread      vertical shadow offset
     * @param shadowColor shadow color at the box edge
     */
    public static void shadow(long vg, float x, float y, float w, float h,
                              float radius, float blur, float spread, VGColor shadowColor) {
        shadow(vg, x, y, w, h, radius, blur, 0.0f, 0.0f, spread, shadowColor);
    }

    /**
     * Draws an inset / inner shadow inside a rectangle or rounded rectangle with offsets.
     *
     * @param vg      NanoVG context handle
     * @param x       box left edge
     * @param y       box top edge
     * @param w       box width
     * @param h       box height
     * @param radius  corner radius
     * @param blur    blur distance
     * @param spread  spread distance
     * @param offsetX horizontal shadow offset
     * @param offsetY vertical shadow offset
     * @param color   shadow color
     */
    public static void innerShadow(long vg, float x, float y, float w, float h,
                                   float radius, float blur, float spread,
                                   float offsetX, float offsetY, VGColor color) {
        if (vg == 0 || color == null) return;
        save(vg);
        try {
            nvgIntersectScissor(vg, x, y, w, h);
            float bx = x + offsetX + spread;
            float by = y + offsetY + spread;
            float bw = w - 2.0f * spread;
            float bh = h - 2.0f * spread;
            float br = Math.max(0.0f, radius - spread);
            VGPaint paint = VGPaint.boxGradient(vg, bx, by, bw, bh, br, blur, VGColor.TRANSPARENT, color);
            nvgBeginPath(vg);
            if (radius > 0.0f) {
                nvgRoundedRect(vg, x, y, w, h, radius);
            } else {
                nvgRect(vg, x, y, w, h);
            }
            fill(vg, paint);
        } finally {
            restore(vg);
        }
    }

    /**
     * Draws an inset / inner shadow inside a rectangle or rounded rectangle.
     *
     * @param vg     NanoVG context handle
     * @param x      box left edge
     * @param y      box top edge
     * @param w      box width
     * @param h      box height
     * @param radius corner radius
     * @param blur   blur distance
     * @param spread spread distance
     * @param color  shadow color
     */
    public static void innerShadow(long vg, float x, float y, float w, float h,
                                   float radius, float blur, float spread, VGColor color) {
        innerShadow(vg, x, y, w, h, radius, blur, spread, 0.0f, 0.0f, color);
    }

    /**
     * Draws a conical / angular gradient filled circle using a triangle-fan approximation.
     *
     * @param vg         NanoVG context handle
     * @param cx         center x
     * @param cy         center y
     * @param radius     radius
     * @param startAngle starting angle in radians
     * @param colors     color stops, at least 2 colors
     * @param stops      fractional offsets in {@code [0, 1]}, or {@code null} for evenly spaced
     * @param segments   number of fan segments (e.g. 64)
     */
    public static void conicalGradient(long vg, float cx, float cy, float radius, float startAngle,
                                       VGColor[] colors, float[] stops, int segments) {
        if (vg == 0 || colors == null || colors.length == 0 || radius <= 0.0f || segments <= 0) return;
        if (colors.length == 1) {
            circle(vg, cx, cy, radius, colors[0]);
            return;
        }
        VGGradient gradient = stops != null
                ? VGGradient.linear(0, 0, 1, 0, colors, stops)
                : VGGradient.linear(0, 0, 1, 0, colors);
        float step = (float) (2.0 * Math.PI / segments);
        for (int i = 0; i < segments; i++) {
            float a0 = startAngle + i * step;
            float a1 = a0 + step;
            float t = (i + 0.5f) / segments;
            VGColor c = gradient.evaluate(t);
            nvgBeginPath(vg);
            nvgMoveTo(vg, cx, cy);
            nvgLineTo(vg, cx + (float) Math.cos(a0) * radius, cy + (float) Math.sin(a0) * radius);
            nvgLineTo(vg, cx + (float) Math.cos(a1) * radius, cy + (float) Math.sin(a1) * radius);
            nvgClosePath(vg);
            fill(vg, c);
        }
    }

    /**
     * Draws a conical / angular gradient filled circle using a default 64-segment fan approximation.
     *
     * @param vg         NanoVG context handle
     * @param cx         center x
     * @param cy         center y
     * @param radius     radius
     * @param startAngle starting angle in radians
     * @param colors     color stops, at least 2 colors
     * @param stops      fractional offsets in {@code [0, 1]}, or {@code null} for evenly spaced
     */
    public static void conicalGradient(long vg, float cx, float cy, float radius, float startAngle,
                                       VGColor[] colors, float[] stops) {
        conicalGradient(vg, cx, cy, radius, startAngle, colors, stops, 64);
    }

    /**
     * Draws a conical / angular gradient ring / donut using a quad mesh approximation.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param innerRadius inner radius
     * @param outerRadius outer radius
     * @param startAngle  starting angle in radians
     * @param colors      color stops, at least 2 colors
     * @param stops       fractional offsets in {@code [0, 1]}, or {@code null} for evenly spaced
     * @param segments    number of fan segments (e.g. 64)
     */
    public static void conicalGradientRing(long vg, float cx, float cy, float innerRadius, float outerRadius,
                                           float startAngle, VGColor[] colors, float[] stops, int segments) {
        if (vg == 0 || colors == null || colors.length == 0 || outerRadius <= 0.0f || innerRadius < 0.0f || segments <= 0) return;
        VGGradient gradient = stops != null
                ? VGGradient.linear(0, 0, 1, 0, colors, stops)
                : VGGradient.linear(0, 0, 1, 0, colors);
        float step = (float) (2.0 * Math.PI / segments);
        for (int i = 0; i < segments; i++) {
            float a0 = startAngle + i * step;
            float a1 = a0 + step;
            float cos0 = (float) Math.cos(a0);
            float sin0 = (float) Math.sin(a0);
            float cos1 = (float) Math.cos(a1);
            float sin1 = (float) Math.sin(a1);
            float t = (i + 0.5f) / segments;
            VGColor c = gradient.evaluate(t);
            nvgBeginPath(vg);
            nvgMoveTo(vg, cx + cos0 * innerRadius, cy + sin0 * innerRadius);
            nvgLineTo(vg, cx + cos0 * outerRadius, cy + sin0 * outerRadius);
            nvgLineTo(vg, cx + cos1 * outerRadius, cy + sin1 * outerRadius);
            nvgLineTo(vg, cx + cos1 * innerRadius, cy + sin1 * innerRadius);
            nvgClosePath(vg);
            fill(vg, c);
        }
    }

    /**
     * Draws a conical / angular gradient ring / donut using a default 64-segment mesh approximation.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param innerRadius inner radius
     * @param outerRadius outer radius
     * @param startAngle  starting angle in radians
     * @param colors      color stops, at least 2 colors
     * @param stops       fractional offsets in {@code [0, 1]}, or {@code null} for evenly spaced
     */
    public static void conicalGradientRing(long vg, float cx, float cy, float innerRadius, float outerRadius,
                                           float startAngle, VGColor[] colors, float[] stops) {
        conicalGradientRing(vg, cx, cy, innerRadius, outerRadius, startAngle, colors, stops, 64);
    }

    /**
     * Draws a layered glow around a rounded rectangle.
     *
     * @param vg         NanoVG context handle
     * @param x          box left edge
     * @param y          box top edge
     * @param w          box width
     * @param h          box height
     * @param radius     box corner radius
     * @param color      glow color
     * @param layers     number of layers to draw
     * @param spreadStep distance between consecutive layers
     */
    public static void glow(long vg, float x, float y, float w, float h,
                            float radius, VGColor color, int layers, float spreadStep) {
        if (vg == 0 || color == null || layers <= 0) return;
        float baseAlpha = 0.02f * color.a();
        for (int i = layers; i >= 1; i--) {
            float spread = i * spreadStep;
            float spread2 = spread * 2.0f;
            nvgBeginPath(vg);
            nvgRoundedRect(vg, x - spread, y - spread, w + spread2, h + spread2, radius + spread);
            fill(vg, color.withAlpha(baseAlpha / i));
        }
    }

    /**
     * Draws a subtle 2 layer glow around a rounded rectangle.
     *
     * @param vg     NanoVG context handle
     * @param x      box left edge
     * @param y      box top edge
     * @param w      box width
     * @param h      box height
     * @param radius box corner radius
     * @param color  glow color
     */
    public static void glow(long vg, float x, float y, float w, float h, float radius, VGColor color) {
        glow(vg, x, y, w, h, radius, color, 2, 1.5f);
    }

    /**
     * Draws a single line of text with custom alignment.
     *
     * @param vg    NanoVG context handle
     * @param text  text to draw
     * @param x     anchor x
     * @param y     anchor y
     * @param font  registered font face name, or {@code null}
     * @param size  font size in pixels
     * @param color text color
     * @param align NanoVG alignment flags
     */
    public static void text(long vg, String text, float x, float y, String font, float size,
                            VGColor color, int align) {
        if (vg == 0 || color == null || isEmpty(text)) return;
        applyFont(vg, font, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, color.nvg());
        nvgText(vg, x, y, text);
    }

    /**
     * Draws a single line of text with default top-left alignment.
     *
     * @param vg    NanoVG context handle
     * @param text  text to draw
     * @param x     left edge
     * @param y     top edge
     * @param font  registered font face name, or {@code null}
     * @param size  font size in pixels
     * @param color text color
     */
    public static void text(long vg, String text, float x, float y, String font, float size, VGColor color) {
        text(vg, text, x, y, font, size, color, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Draws a single line of text using a {@link VGFont} with custom alignment.
     *
     * @param vg    NanoVG context handle
     * @param text  text to draw
     * @param x     anchor x
     * @param y     anchor y
     * @param font  registered font handle
     * @param size  font size in pixels
     * @param color text color
     * @param align NanoVG alignment flags
     */
    public static void text(long vg, String text, float x, float y, VGFont font, float size,
                            VGColor color, int align) {
        if (vg == 0 || color == null || isEmpty(text)) return;
        applyFont(vg, font, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, color.nvg());
        nvgText(vg, x, y, text);
    }

    /**
     * Draws a single line of text using a {@link VGFont} with default alignment.
     *
     * @param vg    NanoVG context handle
     * @param text  text to draw
     * @param x     left edge
     * @param y     top edge
     * @param font  registered font handle
     * @param size  font size in pixels
     * @param color text color
     */
    public static void text(long vg, String text, float x, float y, VGFont font, float size, VGColor color) {
        text(vg, text, x, y, font, size, color, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Draws multi-line text split by newlines with custom line spacing and alignment.
     *
     * @param vg                   NanoVG context handle
     * @param text                 multi-line text
     * @param x                    anchor x
     * @param y                    anchor y
     * @param font                 registered font face name, or {@code null}
     * @param size                 font size in pixels
     * @param lineHeightMultiplier line height multiplier (e.g. 1.2f)
     * @param color                text color
     * @param align                NanoVG alignment flags
     */
    public static void textMultiline(long vg, String text, float x, float y, String font, float size,
                                     float lineHeightMultiplier, VGColor color, int align) {
        if (vg == 0 || color == null || isEmpty(text)) return;
        applyFont(vg, font, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, color.nvg());
        float lineAdvance = size * lineHeightMultiplier;
        int start = 0;
        int len = text.length();
        int lineIndex = 0;
        for (int i = 0; i <= len; i++) {
            if (i == len || text.charAt(i) == '\n') {
                String line = text.substring(start, i);
                if (line.endsWith("\r")) line = line.substring(0, line.length() - 1);
                if (!line.isEmpty()) {
                    nvgText(vg, x, y + lineIndex * lineAdvance, line);
                }
                start = i + 1;
                lineIndex++;
            }
        }
    }

    public static void textMultiline(long vg, String text, float x, float y, String font, float size,
                                     float lineHeightMultiplier, VGColor color) {
        textMultiline(vg, text, x, y, font, size, lineHeightMultiplier, color, DEFAULT_TEXT_ALIGN);
    }

    public static void textMultiline(long vg, String text, float x, float y, VGFont font, float size,
                                     float lineHeightMultiplier, VGColor color, int align) {
        if (vg == 0 || color == null || isEmpty(text)) return;
        applyFont(vg, font, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, color.nvg());
        float lineAdvance = size * lineHeightMultiplier;
        int start = 0;
        int len = text.length();
        int lineIndex = 0;
        for (int i = 0; i <= len; i++) {
            if (i == len || text.charAt(i) == '\n') {
                String line = text.substring(start, i);
                if (line.endsWith("\r")) line = line.substring(0, line.length() - 1);
                if (!line.isEmpty()) {
                    nvgText(vg, x, y + lineIndex * lineAdvance, line);
                }
                start = i + 1;
                lineIndex++;
            }
        }
    }

    public static void textMultiline(long vg, String text, float x, float y, VGFont font, float size,
                                     float lineHeightMultiplier, VGColor color) {
        textMultiline(vg, text, x, y, font, size, lineHeightMultiplier, color, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Draws text with a drop shadow offset.
     *
     * @param vg          NanoVG context handle
     * @param text        text to draw
     * @param x           anchor x
     * @param y           anchor y
     * @param font        registered font face name, or {@code null}
     * @param size        font size in pixels
     * @param color       foreground text color
     * @param shadowColor shadow text color
     * @param offsetX     horizontal shadow offset
     * @param offsetY     vertical shadow offset
     * @param align       alignment flags
     */
    public static void textWithShadow(long vg, String text, float x, float y, String font, float size,
                                      VGColor color, VGColor shadowColor, float offsetX, float offsetY, int align) {
        if (vg == 0 || isEmpty(text)) return;
        if (shadowColor != null) {
            text(vg, text, x + offsetX, y + offsetY, font, size, shadowColor, align);
        }
        if (color != null) {
            text(vg, text, x, y, font, size, color, align);
        }
    }

    public static void textWithShadow(long vg, String text, float x, float y, String font, float size,
                                      VGColor color, VGColor shadowColor, float offsetX, float offsetY) {
        textWithShadow(vg, text, x, y, font, size, color, shadowColor, offsetX, offsetY, DEFAULT_TEXT_ALIGN);
    }

    public static void textWithShadow(long vg, String text, float x, float y, VGFont font, float size,
                                      VGColor color, VGColor shadowColor, float offsetX, float offsetY, int align) {
        if (vg == 0 || isEmpty(text)) return;
        if (shadowColor != null) {
            text(vg, text, x + offsetX, y + offsetY, font, size, shadowColor, align);
        }
        if (color != null) {
            text(vg, text, x, y, font, size, color, align);
        }
    }

    public static void textWithShadow(long vg, String text, float x, float y, VGFont font, float size,
                                      VGColor color, VGColor shadowColor, float offsetX, float offsetY) {
        textWithShadow(vg, text, x, y, font, size, color, shadowColor, offsetX, offsetY, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Draws text with a contrasting multi-directional outline.
     *
     * @param vg           NanoVG context handle
     * @param text         text to draw
     * @param x            anchor x
     * @param y            anchor y
     * @param font         registered font face name, or {@code null}
     * @param size         font size in pixels
     * @param color        foreground text color
     * @param outlineColor outline text color
     * @param strokeWidth  outline thickness
     * @param align        alignment flags
     */
    public static void textWithOutline(long vg, String text, float x, float y, String font, float size,
                                       VGColor color, VGColor outlineColor, float strokeWidth, int align) {
        if (vg == 0 || isEmpty(text)) return;
        if (outlineColor != null && strokeWidth > 0.0f) {
            float d = strokeWidth;
            float diag = d * 0.70710677f;
            text(vg, text, x - d, y, font, size, outlineColor, align);
            text(vg, text, x + d, y, font, size, outlineColor, align);
            text(vg, text, x, y - d, font, size, outlineColor, align);
            text(vg, text, x, y + d, font, size, outlineColor, align);
            text(vg, text, x - diag, y - diag, font, size, outlineColor, align);
            text(vg, text, x + diag, y - diag, font, size, outlineColor, align);
            text(vg, text, x - diag, y + diag, font, size, outlineColor, align);
            text(vg, text, x + diag, y + diag, font, size, outlineColor, align);
        }
        if (color != null) {
            text(vg, text, x, y, font, size, color, align);
        }
    }

    public static void textWithOutline(long vg, String text, float x, float y, String font, float size,
                                       VGColor color, VGColor outlineColor, float strokeWidth) {
        textWithOutline(vg, text, x, y, font, size, color, outlineColor, strokeWidth, DEFAULT_TEXT_ALIGN);
    }

    public static void textWithOutline(long vg, String text, float x, float y, VGFont font, float size,
                                       VGColor color, VGColor outlineColor, float strokeWidth, int align) {
        if (vg == 0 || isEmpty(text)) return;
        if (outlineColor != null && strokeWidth > 0.0f) {
            float d = strokeWidth;
            float diag = d * 0.70710677f;
            text(vg, text, x - d, y, font, size, outlineColor, align);
            text(vg, text, x + d, y, font, size, outlineColor, align);
            text(vg, text, x, y - d, font, size, outlineColor, align);
            text(vg, text, x, y + d, font, size, outlineColor, align);
            text(vg, text, x - diag, y - diag, font, size, outlineColor, align);
            text(vg, text, x + diag, y - diag, font, size, outlineColor, align);
            text(vg, text, x - diag, y + diag, font, size, outlineColor, align);
            text(vg, text, x + diag, y + diag, font, size, outlineColor, align);
        }
        if (color != null) {
            text(vg, text, x, y, font, size, color, align);
        }
    }

    public static void textWithOutline(long vg, String text, float x, float y, VGFont font, float size,
                                       VGColor color, VGColor outlineColor, float strokeWidth) {
        textWithOutline(vg, text, x, y, font, size, color, outlineColor, strokeWidth, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Draws multi-line wrapped text inside a bounding column width with custom alignment.
     *
     * @param vg            NanoVG context handle
     * @param text          text to draw
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width before wrapping
     * @param font          registered font face name, or {@code null}
     * @param size          font size in pixels
     * @param color         text color
     * @param align         alignment flags
     */
    public static void textBox(long vg, String text, float x, float y, float breakRowWidth,
                               String font, float size, VGColor color, int align) {
        if (vg == 0 || color == null || isEmpty(text)) return;
        applyFont(vg, font, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, color.nvg());
        nvgTextBox(vg, x, y, breakRowWidth, text);
    }

    /**
     * Draws multi-line wrapped text inside a bounding column width with default alignment.
     *
     * @param vg            NanoVG context handle
     * @param text          text to draw
     * @param x             left edge
     * @param y             top edge
     * @param breakRowWidth maximum line width before wrapping
     * @param font          registered font face name, or {@code null}
     * @param size          font size in pixels
     * @param color         text color
     */
    public static void textBox(long vg, String text, float x, float y, float breakRowWidth,
                               String font, float size, VGColor color) {
        textBox(vg, text, x, y, breakRowWidth, font, size, color, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Draws multi-line wrapped text using a {@link VGFont} with custom alignment.
     *
     * @param vg            NanoVG context handle
     * @param text          text to draw
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width before wrapping
     * @param font          registered font handle
     * @param size          font size in pixels
     * @param color         text color
     * @param align         alignment flags
     */
    public static void textBox(long vg, String text, float x, float y, float breakRowWidth,
                               VGFont font, float size, VGColor color, int align) {
        if (vg == 0 || color == null || isEmpty(text)) return;
        applyFont(vg, font, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, color.nvg());
        nvgTextBox(vg, x, y, breakRowWidth, text);
    }

    /**
     * Draws multi-line wrapped text using a {@link VGFont} with default alignment.
     *
     * @param vg            NanoVG context handle
     * @param text          text to draw
     * @param x             left edge
     * @param y             top edge
     * @param breakRowWidth maximum line width before wrapping
     * @param font          registered font handle
     * @param size          font size in pixels
     * @param color         text color
     */
    public static void textBox(long vg, String text, float x, float y, float breakRowWidth,
                               VGFont font, float size, VGColor color) {
        textBox(vg, text, x, y, breakRowWidth, font, size, color, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Measures the advance width of a line of text.
     *
     * @param vg   NanoVG context handle
     * @param text text to measure
     * @param font registered font face name
     * @param size font size in pixels
     * @return advance width
     */
    public static float textWidth(long vg, String text, String font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        applyFont(vg, font, size);
        return nvgTextBounds(vg, 0.0f, 0.0f, text, (FloatBuffer) null);
    }

    /**
     * Measures the advance width of a line of text using a {@link VGFont}.
     *
     * @param vg   NanoVG context handle
     * @param text text to measure
     * @param font registered font handle
     * @param size font size in pixels
     * @return advance width
     */
    public static float textWidth(long vg, String text, VGFont font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        applyFont(vg, font, size);
        return nvgTextBounds(vg, 0.0f, 0.0f, text, (FloatBuffer) null);
    }

    /**
     * Measures the bounding box of a single line of text drawn at the origin.
     *
     * @param vg        NanoVG context handle
     * @param text      text to measure
     * @param font      registered font face name
     * @param size      font size in pixels
     * @param outBounds destination array [minX, minY, maxX, maxY], or {@code null}
     * @return bounds array
     */
    public static float[] textBounds(long vg, String text, String font, float size, float[] outBounds) {
        float[] target = (outBounds != null && outBounds.length >= 4) ? outBounds : new float[4];
        if (vg == 0 || isEmpty(text)) {
            target[0] = target[1] = target[2] = target[3] = 0.0f;
            return target;
        }
        applyFont(vg, font, size);
        nvgTextBounds(vg, 0.0f, 0.0f, text, target);
        return target;
    }

    /**
     * Measures the bounding box of a single line of text using a {@link VGFont}.
     *
     * @param vg        NanoVG context handle
     * @param text      text to measure
     * @param font      registered font handle
     * @param size      font size in pixels
     * @param outBounds destination array [minX, minY, maxX, maxY], or {@code null}
     * @return bounds array
     */
    public static float[] textBounds(long vg, String text, VGFont font, float size, float[] outBounds) {
        float[] target = (outBounds != null && outBounds.length >= 4) ? outBounds : new float[4];
        if (vg == 0 || isEmpty(text)) {
            target[0] = target[1] = target[2] = target[3] = 0.0f;
            return target;
        }
        applyFont(vg, font, size);
        nvgTextBounds(vg, 0.0f, 0.0f, text, target);
        return target;
    }

    /**
     * Measures the bounding box of multi-line wrapped text.
     *
     * @param vg            NanoVG context handle
     * @param text          text to measure
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width
     * @param font          registered font face name
     * @param size          font size in pixels
     * @param outBounds     destination array [minX, minY, maxX, maxY], or {@code null}
     * @return bounds array
     */
    public static float[] textBoxBounds(long vg, String text, float x, float y, float breakRowWidth,
                                        String font, float size, float[] outBounds) {
        float[] target = (outBounds != null && outBounds.length >= 4) ? outBounds : new float[4];
        if (vg == 0 || isEmpty(text)) {
            target[0] = target[1] = target[2] = target[3] = 0.0f;
            return target;
        }
        applyFont(vg, font, size);
        nvgTextBoxBounds(vg, x, y, breakRowWidth, text, target);
        return target;
    }

    /**
     * Measures the bounding box of multi-line wrapped text using a {@link VGFont}.
     *
     * @param vg            NanoVG context handle
     * @param text          text to measure
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width
     * @param font          registered font handle
     * @param size          font size in pixels
     * @param outBounds     destination array [minX, minY, maxX, maxY], or {@code null}
     * @return bounds array
     */
    public static float[] textBoxBounds(long vg, String text, float x, float y, float breakRowWidth,
                                        VGFont font, float size, float[] outBounds) {
        float[] target = (outBounds != null && outBounds.length >= 4) ? outBounds : new float[4];
        if (vg == 0 || isEmpty(text)) {
            target[0] = target[1] = target[2] = target[3] = 0.0f;
            return target;
        }
        applyFont(vg, font, size);
        nvgTextBoxBounds(vg, x, y, breakRowWidth, text, target);
        return target;
    }

    /**
     * Computes the total rendered height of multi-line wrapped text without per-frame allocations.
     *
     * @param vg            NanoVG context handle
     * @param text          text to measure
     * @param breakRowWidth maximum line width
     * @param font          registered font face name
     * @param size          font size in pixels
     * @return total height in pixels
     */
    public static float textBoxHeight(long vg, String text, float breakRowWidth, String font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        float[] bounds = textBoxBounds(vg, text, 0.0f, 0.0f, breakRowWidth, font, size, SCRATCH_BOUNDS.get());
        return bounds[3] - bounds[1];
    }

    /**
     * Computes the total rendered height of multi-line wrapped text using a {@link VGFont}.
     *
     * @param vg            NanoVG context handle
     * @param text          text to measure
     * @param breakRowWidth maximum line width
     * @param font          registered font handle
     * @param size          font size in pixels
     * @return total height in pixels
     */
    public static float textBoxHeight(long vg, String text, float breakRowWidth, VGFont font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        float[] bounds = textBoxBounds(vg, text, 0.0f, 0.0f, breakRowWidth, font, size, SCRATCH_BOUNDS.get());
        return bounds[3] - bounds[1];
    }

    /**
     * Measures the vertical font metrics (ascender, descender, lineh) for the given font and size.
     *
     * @param vg         NanoVG context handle
     * @param font       registered font face name, or {@code null}
     * @param size       font size in pixels
     * @param outMetrics destination array of length at least 3 {@code [ascent, descent, lineh]}, or {@code null}
     * @return the metrics array
     */
    public static float[] textMetrics(long vg, String font, float size, float[] outMetrics) {
        float[] target = (outMetrics != null && outMetrics.length >= 3) ? outMetrics : new float[3];
        if (vg == 0) {
            target[0] = target[1] = target[2] = 0.0f;
            return target;
        }
        applyFont(vg, font, size);
        float[] asc = SCRATCH_ASC.get();
        float[] desc = SCRATCH_DESC.get();
        float[] lineh = SCRATCH_LINEH.get();
        nvgTextMetrics(vg, asc, desc, lineh);
        target[0] = asc[0];
        target[1] = desc[0];
        target[2] = lineh[0];
        return target;
    }

    /**
     * Measures the vertical font metrics (ascender, descender, lineh) using a {@link VGFont}.
     *
     * @param vg         NanoVG context handle
     * @param font       registered font handle
     * @param size       font size in pixels
     * @param outMetrics destination array of length at least 3 {@code [ascent, descent, lineh]}, or {@code null}
     * @return the metrics array
     */
    public static float[] textMetrics(long vg, VGFont font, float size, float[] outMetrics) {
        float[] target = (outMetrics != null && outMetrics.length >= 3) ? outMetrics : new float[3];
        if (vg == 0) {
            target[0] = target[1] = target[2] = 0.0f;
            return target;
        }
        applyFont(vg, font, size);
        float[] asc = SCRATCH_ASC.get();
        float[] desc = SCRATCH_DESC.get();
        float[] lineh = SCRATCH_LINEH.get();
        nvgTextMetrics(vg, asc, desc, lineh);
        target[0] = asc[0];
        target[1] = desc[0];
        target[2] = lineh[0];
        return target;
    }

    /**
     * Measures the font ascender distance above the baseline.
     *
     * @param vg   NanoVG context handle
     * @param font registered font face name
     * @param size font size in pixels
     * @return font ascender in pixels
     */
    public static float textAscent(long vg, String font, float size) {
        if (vg == 0) return 0.0f;
        applyFont(vg, font, size);
        float[] asc = SCRATCH_ASC.get();
        nvgTextMetrics(vg, asc, (float[]) null, (float[]) null);
        return asc[0];
    }

    public static float textAscent(long vg, VGFont font, float size) {
        if (vg == 0) return 0.0f;
        applyFont(vg, font, size);
        float[] asc = SCRATCH_ASC.get();
        nvgTextMetrics(vg, asc, (float[]) null, (float[]) null);
        return asc[0];
    }

    /**
     * Measures the font descender distance below the baseline (positive value).
     *
     * @param vg   NanoVG context handle
     * @param font registered font face name
     * @param size font size in pixels
     * @return font descender in pixels
     */
    public static float textDescent(long vg, String font, float size) {
        if (vg == 0) return 0.0f;
        applyFont(vg, font, size);
        float[] desc = SCRATCH_DESC.get();
        nvgTextMetrics(vg, (float[]) null, desc, (float[]) null);
        return desc[0];
    }

    public static float textDescent(long vg, VGFont font, float size) {
        if (vg == 0) return 0.0f;
        applyFont(vg, font, size);
        float[] desc = SCRATCH_DESC.get();
        nvgTextMetrics(vg, (float[]) null, desc, (float[]) null);
        return desc[0];
    }

    /**
     * Measures the font line height in pixels from font metrics.
     *
     * @param vg   NanoVG context handle
     * @param font registered font face name
     * @param size font size in pixels
     * @return line height in pixels
     */
    public static float fontLineHeight(long vg, String font, float size) {
        if (vg == 0) return 0.0f;
        applyFont(vg, font, size);
        float[] lineh = SCRATCH_LINEH.get();
        nvgTextMetrics(vg, (float[]) null, (float[]) null, lineh);
        return lineh[0];
    }

    public static float fontLineHeight(long vg, VGFont font, float size) {
        if (vg == 0) return 0.0f;
        applyFont(vg, font, size);
        float[] lineh = SCRATCH_LINEH.get();
        nvgTextMetrics(vg, (float[]) null, (float[]) null, lineh);
        return lineh[0];
    }

    /**
     * Measures the rendered height of a single line of text.
     *
     * @param vg   NanoVG context handle
     * @param text text to measure
     * @param font registered font face name
     * @param size font size in pixels
     * @return height in pixels
     */
    public static float textHeight(long vg, String text, String font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        float[] b = textBounds(vg, text, font, size, SCRATCH_BOUNDS.get());
        return b[3] - b[1];
    }

    public static float textHeight(long vg, String text, VGFont font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        float[] b = textBounds(vg, text, font, size, SCRATCH_BOUNDS.get());
        return b[3] - b[1];
    }

    /**
     * Sets the text line height multiplier.
     *
     * @param vg         NanoVG context handle
     * @param lineHeight line height multiplier (default 1.0)
     */
    public static void textLineHeight(long vg, float lineHeight) {
        if (vg != 0) nvgTextLineHeight(vg, lineHeight);
    }

    /**
     * Sets the text letter spacing in pixels.
     *
     * @param vg      NanoVG context handle
     * @param spacing spacing in pixels
     */
    public static void textLetterSpacing(long vg, float spacing) {
        if (vg != 0) nvgTextLetterSpacing(vg, spacing);
    }

    /**
     * Sets the line cap style for subsequent stroke operations.
     *
     * @param vg  NanoVG context handle
     * @param cap native NanoVG line cap constant
     */
    public static void lineCap(long vg, int cap) {
        if (vg != 0) nvgLineCap(vg, cap);
    }

    /**
     * Sets the line cap style for subsequent stroke operations.
     *
     * @param vg  NanoVG context handle
     * @param cap line cap enum
     */
    public static void lineCap(long vg, VGLineCap cap) {
        if (vg != 0 && cap != null) nvgLineCap(vg, cap.id);
    }

    /**
     * Sets the line join style for subsequent stroke operations.
     *
     * @param vg   NanoVG context handle
     * @param join native NanoVG line join constant
     */
    public static void lineJoin(long vg, int join) {
        if (vg != 0) nvgLineJoin(vg, join);
    }

    /**
     * Sets the line join style for subsequent stroke operations.
     *
     * @param vg   NanoVG context handle
     * @param join line join enum
     */
    public static void lineJoin(long vg, VGLineJoin join) {
        if (vg != 0 && join != null) nvgLineJoin(vg, join.id);
    }

    /**
     * Sets the miter limit for sharp corners.
     *
     * @param vg    NanoVG context handle
     * @param limit miter limit ratio
     */
    public static void miterLimit(long vg, float limit) {
        if (vg != 0) nvgMiterLimit(vg, limit);
    }

    /**
     * Sets the global alpha factor multiplied into all drawing operations.
     *
     * @param vg    NanoVG context handle
     * @param alpha alpha in [0, 1]
     */
    public static void globalAlpha(long vg, float alpha) {
        if (vg != 0) nvgGlobalAlpha(vg, alpha);
    }

    /**
     * Sets the global composite operation for pixel blending.
     *
     * @param vg NanoVG context handle
     * @param op native composite operation constant
     */
    public static void compositeOperation(long vg, int op) {
        if (vg != 0) nvgGlobalCompositeOperation(vg, op);
    }

    /**
     * Sets the global composite operation for pixel blending.
     *
     * @param vg NanoVG context handle
     * @param op composite operation enum
     */
    public static void compositeOperation(long vg, VGCompositeOp op) {
        if (vg != 0 && op != null) nvgGlobalCompositeOperation(vg, op.id);
    }

    /**
     * Sets the global composite blend function factors.
     *
     * @param vg      NanoVG context handle
     * @param sfactor source factor
     * @param dfactor destination factor
     */
    public static void compositeBlendFunc(long vg, int sfactor, int dfactor) {
        if (vg != 0) nvgGlobalCompositeBlendFunc(vg, sfactor, dfactor);
    }

    /**
     * Sets the global composite blend function factors using enums.
     *
     * @param vg      NanoVG context handle
     * @param sfactor source blend factor
     * @param dfactor destination blend factor
     */
    public static void compositeBlendFunc(long vg, VGBlendFactor sfactor, VGBlendFactor dfactor) {
        if (vg != 0 && sfactor != null && dfactor != null) {
            nvgGlobalCompositeBlendFunc(vg, sfactor.id, dfactor.id);
        }
    }

    /**
     * Sets separate blend function factors for color (RGB) and alpha channels.
     *
     * @param vg       NanoVG context handle
     * @param srcRGB   source RGB factor
     * @param dstRGB   destination RGB factor
     * @param srcAlpha source alpha factor
     * @param dstAlpha destination alpha factor
     */
    public static void compositeBlendFuncSeparate(long vg, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
        if (vg != 0) nvgGlobalCompositeBlendFuncSeparate(vg, srcRGB, dstRGB, srcAlpha, dstAlpha);
    }

    /**
     * Sets separate blend function factors for color (RGB) and alpha channels using enums.
     *
     * @param vg       NanoVG context handle
     * @param srcRGB   source RGB factor
     * @param dstRGB   destination RGB factor
     * @param srcAlpha source alpha factor
     * @param dstAlpha destination alpha factor
     */
    public static void compositeBlendFuncSeparate(long vg, VGBlendFactor srcRGB, VGBlendFactor dstRGB,
                                                  VGBlendFactor srcAlpha, VGBlendFactor dstAlpha) {
        if (vg != 0 && srcRGB != null && dstRGB != null && srcAlpha != null && dstAlpha != null) {
            nvgGlobalCompositeBlendFuncSeparate(vg, srcRGB.id, dstRGB.id, srcAlpha.id, dstAlpha.id);
        }
    }

    /**
     * Deletes a texture image on the GPU.
     *
     * @param vg    NanoVG context handle
     * @param image native image handle
     */
    public static void deleteImage(long vg, int image) {
        if (vg != 0 && image > 0) nvgDeleteImage(vg, image);
    }

    /**
     * Deletes a texture image on the GPU using a {@link VGImage}.
     *
     * @param vg    NanoVG context handle
     * @param image image instance
     */
    public static void deleteImage(long vg, VGImage image) {
        if (image != null) image.delete();
    }

    /**
     * Queries the dimensions of an image handle.
     *
     * @param vg      NanoVG context handle
     * @param image   image handle
     * @param outSize destination array [width, height], or {@code null}
     * @return array holding [width, height]
     */
    public static int[] imageSize(long vg, int image, int[] outSize) {
        int[] target = (outSize != null && outSize.length >= 2) ? outSize : new int[2];
        if (vg == 0 || image <= 0) {
            target[0] = target[1] = 0;
            return target;
        }
        int[] w = new int[1];
        int[] h = new int[1];
        nvgImageSize(vg, image, w, h);
        target[0] = w[0];
        target[1] = h[0];
        return target;
    }

    /**
     * Queries the dimensions of an image handle without heap allocation using a scratch buffer.
     *
     * @param vg    NanoVG context handle
     * @param image image handle
     * @return array holding [width, height]
     */
    public static int[] imageSize(long vg, int image) {
        return imageSize(vg, image, SCRATCH_SIZE.get());
    }

    /**
     * Draws an image into a rectangle with optional rounded corners and opacity.
     *
     * @param vg     NanoVG context handle
     * @param image  native image handle
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius (0 for sharp rectangle)
     * @param alpha  opacity in [0, 1]
     */
    public static void image(long vg, int image, float x, float y, float w, float h, float radius, float alpha) {
        if (vg == 0 || image <= 0) return;
        VGPaint paint = VGPaint.imagePattern(vg, x, y, w, h, 0.0f, image, alpha);
        if (radius > 0.0f) {
            roundedRect(vg, x, y, w, h, radius, paint);
        } else {
            rect(vg, x, y, w, h, paint);
        }
    }

    /**
     * Draws an opaque, unrounded image rectangle.
     *
     * @param vg    NanoVG context handle
     * @param image native image handle
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     */
    public static void image(long vg, int image, float x, float y, float w, float h) {
        image(vg, image, x, y, w, h, 0.0f, 1.0f);
    }

    /**
     * Draws an image into a rectangle with optional rounded corners and opacity.
     *
     * @param vg     NanoVG context handle
     * @param image  image instance
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param alpha  opacity in [0, 1]
     */
    public static void image(long vg, VGImage image, float x, float y, float w, float h, float radius, float alpha) {
        if (image != null) {
            image(vg, image.handle(), x, y, w, h, radius, alpha);
        }
    }

    /**
     * Draws an opaque, unrounded image rectangle using a {@link VGImage}.
     *
     * @param vg    NanoVG context handle
     * @param image image instance
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     */
    public static void image(long vg, VGImage image, float x, float y, float w, float h) {
        if (image != null) {
            image(vg, image.handle(), x, y, w, h, 0.0f, 1.0f);
        }
    }

    /**
     * Pushes the current render state onto the state stack.
     *
     * @param vg NanoVG context handle
     */
    public static void save(long vg) {
        if (vg != 0) nvgSave(vg);
    }

    /**
     * Pops the render state from the state stack.
     *
     * @param vg NanoVG context handle
     */
    public static void restore(long vg) {
        if (vg != 0) nvgRestore(vg);
    }

    /**
     * Translates the current coordinate system.
     *
     * @param vg NanoVG context handle
     * @param x  x offset
     * @param y  y offset
     */
    public static void translate(long vg, float x, float y) {
        if (vg != 0) nvgTranslate(vg, x, y);
    }

    /**
     * Rotates the current coordinate system.
     *
     * @param vg       NanoVG context handle
     * @param angleRad angle in radians
     */
    public static void rotate(long vg, float angleRad) {
        if (vg != 0) nvgRotate(vg, angleRad);
    }

    /**
     * Scales the current coordinate system.
     *
     * @param vg NanoVG context handle
     * @param sx x scale factor
     * @param sy y scale factor
     */
    public static void scale(long vg, float sx, float sy) {
        if (vg != 0) nvgScale(vg, sx, sy);
    }

    /**
     * Sets the scissor rectangle, replacing any previous one.
     *
     * @param vg NanoVG context handle
     * @param x  left edge
     * @param y  top edge
     * @param w  width
     * @param h  height
     */
    public static void scissor(long vg, float x, float y, float w, float h) {
        if (vg != 0) nvgScissor(vg, x, y, w, h);
    }

    /**
     * Intersects the current scissor rectangle with the given one.
     *
     * @param vg NanoVG context handle
     * @param x  left edge
     * @param y  top edge
     * @param w  width
     * @param h  height
     */
    public static void intersectScissor(long vg, float x, float y, float w, float h) {
        if (vg != 0) nvgIntersectScissor(vg, x, y, w, h);
    }

    /**
     * Disables scissoring.
     *
     * @param vg NanoVG context handle
     */
    public static void resetScissor(long vg) {
        if (vg != 0) nvgResetScissor(vg);
    }

    /**
     * Executes the given action within a saved canvas state scope, restoring state on exit.
     *
     * @param vg     NanoVG context handle
     * @param action drawing callback
     */
    public static void scoped(long vg, LongConsumer action) {
        if (vg == 0 || action == null) return;
        save(vg);
        try {
            action.accept(vg);
        } finally {
            restore(vg);
        }
    }

    /**
     * Executes the given action within an intersected scissor region, restoring canvas and scissor state on exit.
     *
     * @param vg     NanoVG context handle
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param action drawing callback
     */
    public static void scissored(long vg, float x, float y, float w, float h, LongConsumer action) {
        if (vg == 0 || action == null) return;
        save(vg);
        try {
            nvgIntersectScissor(vg, x, y, w, h);
            action.accept(vg);
        } finally {
            restore(vg);
        }
    }

    /**
     * Clips rendering to a rounded rectangle by intersecting an axis-aligned scissor and
     * erasing the outside corners using {@link NanoVG#NVG_DESTINATION_OUT}.
     * <p>
     * Note: Because this erases to transparency, it is intended for isolated or offscreen renders.
     *
     * @param vg     NanoVG context handle
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param render drawing operations to clip
     */
    public static void clipRoundedRect(long vg, float x, float y, float w, float h, float radius, Runnable render) {
        if (vg == 0 || render == null) return;
        save(vg);
        try {
            nvgIntersectScissor(vg, x, y, w, h);
            render.run();
            if (radius > 0.0f) {
                compositeOperation(vg, VGCompositeOp.DESTINATION_OUT);
                nvgBeginPath(vg);
                nvgRect(vg, x, y, w, h);
                nvgRoundedRect(vg, x, y, w, h, radius);
                nvgPathWinding(vg, NVG_HOLE);
                fill(vg, VGColor.WHITE);
            }
        } finally {
            restore(vg);
        }
    }

    /**
     * Fills an SVG path with a solid color.
     *
     * @param vg    NanoVG context handle
     * @param path  parsed SVG path
     * @param color fill color
     */
    public static void path(long vg, VGPath path, VGColor color) {
        if (vg == 0 || path == null || color == null) return;
        nvgBeginPath(vg);
        path.apply(vg);
        fill(vg, color);
    }

    /**
     * Fills an SVG path with a paint.
     *
     * @param vg    NanoVG context handle
     * @param path  parsed SVG path
     * @param paint fill paint
     */
    public static void path(long vg, VGPath path, VGPaint paint) {
        if (vg == 0 || path == null || paint == null) return;
        nvgBeginPath(vg);
        path.apply(vg);
        fill(vg, paint);
    }

    /**
     * Parses and fills an SVG path data string with a solid color.
     *
     * @param vg      NanoVG context handle
     * @param svgPath SVG path data string
     * @param color   fill color
     */
    public static void path(long vg, String svgPath, VGColor color) {
        if (vg == 0 || svgPath == null || color == null) return;
        path(vg, VGPath.of(svgPath), color);
    }

    /**
     * Parses and fills an SVG path data string with a paint.
     *
     * @param vg      NanoVG context handle
     * @param svgPath SVG path data string
     * @param paint   fill paint
     */
    public static void path(long vg, String svgPath, VGPaint paint) {
        if (vg == 0 || svgPath == null || paint == null) return;
        path(vg, VGPath.of(svgPath), paint);
    }

    /**
     * Strokes an SVG path outline with a solid color.
     *
     * @param vg          NanoVG context handle
     * @param path        parsed SVG path
     * @param strokeWidth stroke width
     * @param color       stroke color
     */
    public static void pathOutline(long vg, VGPath path, float strokeWidth, VGColor color) {
        if (vg == 0 || path == null || color == null) return;
        nvgBeginPath(vg);
        path.apply(vg);
        stroke(vg, strokeWidth, color);
    }

    /**
     * Strokes an SVG path outline with a paint.
     *
     * @param vg          NanoVG context handle
     * @param path        parsed SVG path
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     */
    public static void pathOutline(long vg, VGPath path, float strokeWidth, VGPaint paint) {
        if (vg == 0 || path == null || paint == null) return;
        nvgBeginPath(vg);
        path.apply(vg);
        stroke(vg, strokeWidth, paint);
    }

    /**
     * Strokes an SVG path outline with custom cap and join styles.
     *
     * @param vg          NanoVG context handle
     * @param path        parsed SVG path
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void pathOutline(long vg, VGPath path, float strokeWidth, VGColor color,
                                   VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || path == null || color == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        pathOutline(vg, path, strokeWidth, color);
    }

    /**
     * Strokes an SVG path outline with a paint and custom cap and join styles.
     *
     * @param vg          NanoVG context handle
     * @param path        parsed SVG path
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     * @param cap         line cap style
     * @param join        line join style
     */
    public static void pathOutline(long vg, VGPath path, float strokeWidth, VGPaint paint,
                                   VGLineCap cap, VGLineJoin join) {
        if (vg == 0 || path == null || paint == null) return;
        if (cap != null) nvgLineCap(vg, cap.id);
        if (join != null) nvgLineJoin(vg, join.id);
        pathOutline(vg, path, strokeWidth, paint);
    }

    public static void pathOutline(long vg, String svgPath, float strokeWidth, VGColor color) {
        if (vg == 0 || svgPath == null || color == null) return;
        pathOutline(vg, VGPath.of(svgPath), strokeWidth, color);
    }

    public static void pathOutline(long vg, String svgPath, float strokeWidth, VGPaint paint) {
        if (vg == 0 || svgPath == null || paint == null) return;
        pathOutline(vg, VGPath.of(svgPath), strokeWidth, paint);
    }

    private static void fillGradient(long vg, VGGradient gradient, Runnable pathBuilder) {
        if (gradient.stopCount() == 2) {
            pathBuilder.run();
            fill(vg, gradient.paintSegment(vg, 0));
            return;
        }
        if (gradient.type() == VGGradient.Type.LINEAR) {
            float sx = gradient.x0();
            float sy = gradient.y0();
            float ex = gradient.x1();
            float ey = gradient.y1();
            float dx = ex - sx;
            float dy = ey - sy;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len <= 1e-6f) {
                pathBuilder.run();
                fill(vg, gradient.colors()[0]);
                return;
            }
            float angle = (float) Math.atan2(dy, dx);
            float[] stops = gradient.stops();
            save(vg);
            try {
                for (int i = 0; i < gradient.stopCount() - 1; i++) {
                    save(vg);
                    nvgTranslate(vg, sx, sy);
                    nvgRotate(vg, angle);
                    nvgIntersectScissor(vg, stops[i] * len, -100000.0f, (stops[i + 1] - stops[i]) * len, 200000.0f);
                    nvgResetTransform(vg);
                    pathBuilder.run();
                    fill(vg, gradient.paintSegment(vg, i));
                    restore(vg);
                }
            } finally {
                restore(vg);
            }
        } else {
            for (int i = 0; i < gradient.stopCount() - 1; i++) {
                pathBuilder.run();
                fill(vg, gradient.paintSegment(vg, i));
            }
        }
    }

    private static void fill(long vg, VGColor color) {
        nvgFillColor(vg, color.nvg());
        nvgFill(vg);
    }

    private static void fill(long vg, VGPaint paint) {
        nvgFillPaint(vg, paint.nvg());
        nvgFill(vg);
    }

    private static void stroke(long vg, float strokeWidth, VGColor color) {
        nvgStrokeColor(vg, color.nvg());
        nvgStrokeWidth(vg, strokeWidth);
        nvgStroke(vg);
    }

    private static void stroke(long vg, float strokeWidth, VGPaint paint) {
        nvgStrokePaint(vg, paint.nvg());
        nvgStrokeWidth(vg, strokeWidth);
        nvgStroke(vg);
    }

    private static void applyFont(long vg, String font, float size) {
        if (font != null) {
            nvgFontFace(vg, font);
        }
        nvgFontSize(vg, size);
    }

    private static void applyFont(long vg, VGFont font, float size) {
        if (font != null) {
            nvgFontFaceId(vg, font.handle());
        }
        nvgFontSize(vg, size);
    }

    private static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
