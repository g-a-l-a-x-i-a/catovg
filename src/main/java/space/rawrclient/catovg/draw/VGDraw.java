package space.rawrclient.catovg.draw;

import org.lwjgl.nanovg.NanoVG;
import space.rawrclient.catovg.color.VGColor;
import space.rawrclient.catovg.paint.VGPaint;

import java.nio.FloatBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Stateless immediate-mode drawing helpers on top of NanoVG.
 * <p>
 * Every method accepts a raw NanoVG context handle and silently does nothing
 * when the handle is {@code 0} or the supplied color/paint is {@code null},
 * which makes the helpers safe to call before a context is available.
 */
public final class VGDraw {

    /** Default text alignment: left edge, top of the em box. */
    public static final int DEFAULT_TEXT_ALIGN = NVG_ALIGN_LEFT | NVG_ALIGN_TOP;

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
     * Strokes a circular arc.
     *
     * @param vg          NanoVG context handle
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param a0          start angle in radians
     * @param a1          end angle in radians
     * @param dir         {@link NanoVG#NVG_CW} or {@link NanoVG#NVG_CCW}
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
     * Draws a soft drop shadow around a rounded rectangle. The interior of the
     * rectangle is cut out, so the shadow never darkens translucent content.
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
        if (vg == 0 || shadowColor == null) return;
        VGPaint paint = VGPaint.boxGradient(vg, x, y + spread, w, h, radius, blur, shadowColor, VGColor.TRANSPARENT);
        nvgBeginPath(vg);
        nvgRect(vg, x - blur, y - blur, w + blur * 2.0f, h + blur * 2.0f + spread);
        nvgRoundedRect(vg, x, y, w, h, radius);
        nvgPathWinding(vg, NVG_HOLE);
        fill(vg, paint);
    }

    /**
     * Draws a layered glow around a rounded rectangle. Outer layers are drawn
     * first and fade as they grow.
     *
     * @param vg         NanoVG context handle
     * @param x          box left edge
     * @param y          box top edge
     * @param w          box width
     * @param h          box height
     * @param radius     box corner radius
     * @param color      glow color; its alpha scales the overall intensity
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
     * Draws a single line of text.
     *
     * @param vg    NanoVG context handle
     * @param text  text to draw
     * @param x     anchor x
     * @param y     anchor y
     * @param font  registered font face name, or {@code null} to keep the current face
     * @param size  font size in pixels
     * @param color text color
     * @param align combination of NanoVG {@code NVG_ALIGN_*} flags
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
     * Draws a single line of text aligned to {@link #DEFAULT_TEXT_ALIGN}.
     *
     * @param vg    NanoVG context handle
     * @param text  text to draw
     * @param x     left edge
     * @param y     top edge
     * @param font  registered font face name, or {@code null} to keep the current face
     * @param size  font size in pixels
     * @param color text color
     */
    public static void text(long vg, String text, float x, float y, String font, float size, VGColor color) {
        text(vg, text, x, y, font, size, color, DEFAULT_TEXT_ALIGN);
    }

    /**
     * Measures the horizontal advance of a line of text.
     *
     * @param vg   NanoVG context handle
     * @param text text to measure
     * @param font registered font face name, or {@code null} to keep the current face
     * @param size font size in pixels
     * @return the advance width, or {@code 0} if nothing can be measured
     */
    public static float textWidth(long vg, String text, String font, float size) {
        if (vg == 0 || isEmpty(text)) return 0.0f;
        applyFont(vg, font, size);
        return nvgTextBounds(vg, 0.0f, 0.0f, text, (FloatBuffer) null);
    }

    /**
     * Measures the bounding box of a line of text drawn at the origin.
     *
     * @param vg        NanoVG context handle
     * @param text      text to measure
     * @param font      registered font face name, or {@code null} to keep the current face
     * @param size      font size in pixels
     * @param outBounds destination array of at least 4 elements, or {@code null} to allocate one
     * @return array holding {@code [minX, minY, maxX, maxY]}; zeroed if nothing can be measured
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

    private static void applyFont(long vg, String font, float size) {
        if (font != null) {
            nvgFontFace(vg, font);
        }
        nvgFontSize(vg, size);
    }

    private static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
