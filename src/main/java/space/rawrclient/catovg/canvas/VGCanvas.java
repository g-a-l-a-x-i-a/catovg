package space.rawrclient.catovg.canvas;

import space.rawrclient.catovg.color.VGColor;
import space.rawrclient.catovg.draw.VGDraw;
import space.rawrclient.catovg.paint.VGPaint;

/**
 * Fluent, object-oriented wrapper around a NanoVG context handle.
 * <p>
 * Each drawing method delegates to {@link VGDraw} and returns {@code this},
 * allowing calls to be chained:
 * <pre>{@code
 * VGCanvas.of(vg)
 *         .save()
 *         .translate(10, 10)
 *         .roundedRect(0, 0, 100, 40, 6, VGColor.BLACK)
 *         .text("hello", 8, 8, "sans", 16, VGColor.WHITE)
 *         .restore();
 * }</pre>
 * Instances are cheap, immutable and hold no native resources; the caller
 * remains responsible for the lifetime of the underlying context.
 */
public final class VGCanvas {

    private final long vg;

    private VGCanvas(long vg) {
        this.vg = vg;
    }

    /**
     * Wraps an existing NanoVG context.
     *
     * @param vg NanoVG context handle
     * @return a canvas bound to {@code vg}
     */
    public static VGCanvas of(long vg) {
        return new VGCanvas(vg);
    }

    /**
     * Returns the wrapped NanoVG context handle.
     *
     * @return the context handle
     */
    public long handle() {
        return vg;
    }

    /**
     * Pushes the current render state.
     *
     * @return this canvas
     * @see VGDraw#save(long)
     */
    public VGCanvas save() {
        VGDraw.save(vg);
        return this;
    }

    /**
     * Pops the last saved render state.
     *
     * @return this canvas
     * @see VGDraw#restore(long)
     */
    public VGCanvas restore() {
        VGDraw.restore(vg);
        return this;
    }

    /**
     * Translates the coordinate system.
     *
     * @param x x offset
     * @param y y offset
     * @return this canvas
     * @see VGDraw#translate(long, float, float)
     */
    public VGCanvas translate(float x, float y) {
        VGDraw.translate(vg, x, y);
        return this;
    }

    /**
     * Rotates the coordinate system.
     *
     * @param rad angle in radians
     * @return this canvas
     * @see VGDraw#rotate(long, float)
     */
    public VGCanvas rotate(float rad) {
        VGDraw.rotate(vg, rad);
        return this;
    }

    /**
     * Scales the coordinate system.
     *
     * @param sx x scale factor
     * @param sy y scale factor
     * @return this canvas
     * @see VGDraw#scale(long, float, float)
     */
    public VGCanvas scale(float sx, float sy) {
        VGDraw.scale(vg, sx, sy);
        return this;
    }

    /**
     * Sets the scissor rectangle.
     *
     * @param x left edge
     * @param y top edge
     * @param w width
     * @param h height
     * @return this canvas
     * @see VGDraw#scissor(long, float, float, float, float)
     */
    public VGCanvas scissor(float x, float y, float w, float h) {
        VGDraw.scissor(vg, x, y, w, h);
        return this;
    }

    /**
     * Intersects the scissor rectangle.
     *
     * @param x left edge
     * @param y top edge
     * @param w width
     * @param h height
     * @return this canvas
     * @see VGDraw#intersectScissor(long, float, float, float, float)
     */
    public VGCanvas intersectScissor(float x, float y, float w, float h) {
        VGDraw.intersectScissor(vg, x, y, w, h);
        return this;
    }

    /**
     * Disables scissoring.
     *
     * @return this canvas
     * @see VGDraw#resetScissor(long)
     */
    public VGCanvas resetScissor() {
        VGDraw.resetScissor(vg);
        return this;
    }

    /**
     * Fills a rectangle.
     *
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @param color fill color
     * @return this canvas
     * @see VGDraw#rect(long, float, float, float, float, VGColor)
     */
    public VGCanvas rect(float x, float y, float w, float h, VGColor color) {
        VGDraw.rect(vg, x, y, w, h, color);
        return this;
    }

    /**
     * Fills a rectangle.
     *
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @param paint fill paint
     * @return this canvas
     * @see VGDraw#rect(long, float, float, float, float, VGPaint)
     */
    public VGCanvas rect(float x, float y, float w, float h, VGPaint paint) {
        VGDraw.rect(vg, x, y, w, h, paint);
        return this;
    }

    /**
     * Strokes a rectangle outline.
     *
     * @param x           left edge
     * @param y           top edge
     * @param w           width
     * @param h           height
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#rectOutline(long, float, float, float, float, float, VGColor)
     */
    public VGCanvas rectOutline(float x, float y, float w, float h, float strokeWidth, VGColor color) {
        VGDraw.rectOutline(vg, x, y, w, h, strokeWidth, color);
        return this;
    }

    /**
     * Fills a rounded rectangle.
     *
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param color  fill color
     * @return this canvas
     * @see VGDraw#roundedRect(long, float, float, float, float, float, VGColor)
     */
    public VGCanvas roundedRect(float x, float y, float w, float h, float radius, VGColor color) {
        VGDraw.roundedRect(vg, x, y, w, h, radius, color);
        return this;
    }

    /**
     * Fills a rounded rectangle.
     *
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param paint  fill paint
     * @return this canvas
     * @see VGDraw#roundedRect(long, float, float, float, float, float, VGPaint)
     */
    public VGCanvas roundedRect(float x, float y, float w, float h, float radius, VGPaint paint) {
        VGDraw.roundedRect(vg, x, y, w, h, radius, paint);
        return this;
    }

    /**
     * Strokes a rounded rectangle outline.
     *
     * @param x           left edge
     * @param y           top edge
     * @param w           width
     * @param h           height
     * @param radius      corner radius
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#roundedRectOutline(long, float, float, float, float, float, float, VGColor)
     */
    public VGCanvas roundedRectOutline(float x, float y, float w, float h, float radius,
                                       float strokeWidth, VGColor color) {
        VGDraw.roundedRectOutline(vg, x, y, w, h, radius, strokeWidth, color);
        return this;
    }

    /**
     * Fills a rounded rectangle with per-corner radii.
     *
     * @param x              left edge
     * @param y              top edge
     * @param w              width
     * @param h              height
     * @param radTopLeft     top-left radius
     * @param radTopRight    top-right radius
     * @param radBottomRight bottom-right radius
     * @param radBottomLeft  bottom-left radius
     * @param color          fill color
     * @return this canvas
     * @see VGDraw#roundedRectVarying(long, float, float, float, float, float, float, float, float, VGColor)
     */
    public VGCanvas roundedRectVarying(float x, float y, float w, float h,
                                       float radTopLeft, float radTopRight,
                                       float radBottomRight, float radBottomLeft, VGColor color) {
        VGDraw.roundedRectVarying(vg, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft, color);
        return this;
    }

    /**
     * Fills a rounded rectangle with per-corner radii.
     *
     * @param x              left edge
     * @param y              top edge
     * @param w              width
     * @param h              height
     * @param radTopLeft     top-left radius
     * @param radTopRight    top-right radius
     * @param radBottomRight bottom-right radius
     * @param radBottomLeft  bottom-left radius
     * @param paint          fill paint
     * @return this canvas
     * @see VGDraw#roundedRectVarying(long, float, float, float, float, float, float, float, float, VGPaint)
     */
    public VGCanvas roundedRectVarying(float x, float y, float w, float h,
                                       float radTopLeft, float radTopRight,
                                       float radBottomRight, float radBottomLeft, VGPaint paint) {
        VGDraw.roundedRectVarying(vg, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft, paint);
        return this;
    }

    /**
     * Fills a circle.
     *
     * @param cx     center x
     * @param cy     center y
     * @param radius radius
     * @param color  fill color
     * @return this canvas
     * @see VGDraw#circle(long, float, float, float, VGColor)
     */
    public VGCanvas circle(float cx, float cy, float radius, VGColor color) {
        VGDraw.circle(vg, cx, cy, radius, color);
        return this;
    }

    /**
     * Fills a circle.
     *
     * @param cx     center x
     * @param cy     center y
     * @param radius radius
     * @param paint  fill paint
     * @return this canvas
     * @see VGDraw#circle(long, float, float, float, VGPaint)
     */
    public VGCanvas circle(float cx, float cy, float radius, VGPaint paint) {
        VGDraw.circle(vg, cx, cy, radius, paint);
        return this;
    }

    /**
     * Strokes a circle outline.
     *
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#circleOutline(long, float, float, float, float, VGColor)
     */
    public VGCanvas circleOutline(float cx, float cy, float radius, float strokeWidth, VGColor color) {
        VGDraw.circleOutline(vg, cx, cy, radius, strokeWidth, color);
        return this;
    }

    /**
     * Fills an ellipse.
     *
     * @param cx    center x
     * @param cy    center y
     * @param rx    horizontal radius
     * @param ry    vertical radius
     * @param color fill color
     * @return this canvas
     * @see VGDraw#ellipse(long, float, float, float, float, VGColor)
     */
    public VGCanvas ellipse(float cx, float cy, float rx, float ry, VGColor color) {
        VGDraw.ellipse(vg, cx, cy, rx, ry, color);
        return this;
    }

    /**
     * Strokes a line segment.
     *
     * @param x1          start x
     * @param y1          start y
     * @param x2          end x
     * @param y2          end y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#line(long, float, float, float, float, float, VGColor)
     */
    public VGCanvas line(float x1, float y1, float x2, float y2, float strokeWidth, VGColor color) {
        VGDraw.line(vg, x1, y1, x2, y2, strokeWidth, color);
        return this;
    }

    /**
     * Strokes a circular arc.
     *
     * @param cx          center x
     * @param cy          center y
     * @param radius      radius
     * @param a0          start angle in radians
     * @param a1          end angle in radians
     * @param dir         winding direction
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#arc(long, float, float, float, float, float, int, float, VGColor)
     */
    public VGCanvas arc(float cx, float cy, float radius, float a0, float a1, int dir,
                        float strokeWidth, VGColor color) {
        VGDraw.arc(vg, cx, cy, radius, a0, a1, dir, strokeWidth, color);
        return this;
    }

    /**
     * Fills a quadrilateral.
     *
     * @param x1    first vertex x
     * @param y1    first vertex y
     * @param x2    second vertex x
     * @param y2    second vertex y
     * @param x3    third vertex x
     * @param y3    third vertex y
     * @param x4    fourth vertex x
     * @param y4    fourth vertex y
     * @param color fill color
     * @return this canvas
     * @see VGDraw#quad(long, float, float, float, float, float, float, float, float, VGColor)
     */
    public VGCanvas quad(float x1, float y1, float x2, float y2,
                         float x3, float y3, float x4, float y4, VGColor color) {
        VGDraw.quad(vg, x1, y1, x2, y2, x3, y3, x4, y4, color);
        return this;
    }

    /**
     * Draws a drop shadow around a rounded rectangle.
     *
     * @param x           box left edge
     * @param y           box top edge
     * @param w           box width
     * @param h           box height
     * @param radius      box corner radius
     * @param blur        shadow blur distance
     * @param spread      vertical shadow offset
     * @param shadowColor shadow color
     * @return this canvas
     * @see VGDraw#shadow(long, float, float, float, float, float, float, float, VGColor)
     */
    public VGCanvas shadow(float x, float y, float w, float h, float radius, float blur, float spread,
                           VGColor shadowColor) {
        VGDraw.shadow(vg, x, y, w, h, radius, blur, spread, shadowColor);
        return this;
    }

    /**
     * Draws a layered glow around a rounded rectangle.
     *
     * @param x          box left edge
     * @param y          box top edge
     * @param w          box width
     * @param h          box height
     * @param radius     box corner radius
     * @param color      glow color
     * @param layers     number of layers
     * @param spreadStep distance between layers
     * @return this canvas
     * @see VGDraw#glow(long, float, float, float, float, float, VGColor, int, float)
     */
    public VGCanvas glow(float x, float y, float w, float h, float radius, VGColor color,
                         int layers, float spreadStep) {
        VGDraw.glow(vg, x, y, w, h, radius, color, layers, spreadStep);
        return this;
    }

    /**
     * Draws a layered glow around a rounded rectangle.
     *
     * @param x      box left edge
     * @param y      box top edge
     * @param w      box width
     * @param h      box height
     * @param radius box corner radius
     * @param color  glow color
     * @return this canvas
     * @see VGDraw#glow(long, float, float, float, float, float, VGColor)
     */
    public VGCanvas glow(float x, float y, float w, float h, float radius, VGColor color) {
        VGDraw.glow(vg, x, y, w, h, radius, color);
        return this;
    }

    /**
     * Draws a line of text.
     *
     * @param text  text to draw
     * @param x     anchor x
     * @param y     anchor y
     * @param font  font face name, or {@code null}
     * @param size  font size in pixels
     * @param color text color
     * @param align NanoVG alignment flags
     * @return this canvas
     * @see VGDraw#text(long, String, float, float, String, float, VGColor, int)
     */
    public VGCanvas text(String text, float x, float y, String font, float size, VGColor color, int align) {
        VGDraw.text(vg, text, x, y, font, size, color, align);
        return this;
    }

    /**
     * Draws a line of text.
     *
     * @param text  text to draw
     * @param x     left edge
     * @param y     top edge
     * @param font  font face name, or {@code null}
     * @param size  font size in pixels
     * @param color text color
     * @return this canvas
     * @see VGDraw#text(long, String, float, float, String, float, VGColor)
     */
    public VGCanvas text(String text, float x, float y, String font, float size, VGColor color) {
        VGDraw.text(vg, text, x, y, font, size, color);
        return this;
    }

    /**
     * Measures the advance width of a line of text.
     *
     * @param text text to measure
     * @param font font face name, or {@code null}
     * @param size font size in pixels
     * @return the advance width
     * @see VGDraw#textWidth(long, String, String, float)
     */
    public float textWidth(String text, String font, float size) {
        return VGDraw.textWidth(vg, text, font, size);
    }

    /**
     * Measures the bounding box of a line of text.
     *
     * @param text      text to measure
     * @param font      font face name, or {@code null}
     * @param size      font size in pixels
     * @param outBounds destination array, or {@code null}
     * @return {@code [minX, minY, maxX, maxY]}
     * @see VGDraw#textBounds(long, String, String, float, float[])
     */
    public float[] textBounds(String text, String font, float size, float[] outBounds) {
        return VGDraw.textBounds(vg, text, font, size, outBounds);
    }
}
