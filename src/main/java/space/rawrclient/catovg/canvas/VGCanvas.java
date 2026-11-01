package space.rawrclient.catovg.canvas;

import space.rawrclient.catovg.color.VGColor;
import space.rawrclient.catovg.composite.VGBlendFactor;
import space.rawrclient.catovg.composite.VGCompositeOp;
import space.rawrclient.catovg.draw.VGDraw;
import space.rawrclient.catovg.font.VGFont;
import space.rawrclient.catovg.image.VGImage;
import space.rawrclient.catovg.paint.VGPaint;
import space.rawrclient.catovg.stroke.VGLineCap;
import space.rawrclient.catovg.stroke.VGLineJoin;

import java.io.InputStream;

import static org.lwjgl.nanovg.NanoVG.*;

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
     * Fills a rectangle with a paint.
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
     * Fills a rounded rectangle with a paint.
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
     * Fills a rounded rectangle with varying corner radii.
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
     * Fills a rounded rectangle with varying corner radii and paint.
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
     * Fills a circle with color.
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
     * Fills a circle with paint.
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
     * Strokes a line segment with a custom line cap.
     *
     * @param x1          start x
     * @param y1          start y
     * @param x2          end x
     * @param y2          end y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap
     * @return this canvas
     * @see VGDraw#line(long, float, float, float, float, float, VGColor, VGLineCap)
     */
    public VGCanvas line(float x1, float y1, float x2, float y2, float strokeWidth, VGColor color, VGLineCap cap) {
        VGDraw.line(vg, x1, y1, x2, y2, strokeWidth, color, cap);
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
     * @param dir         direction
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
     * @param x1    vertex 1 x
     * @param y1    vertex 1 y
     * @param x2    vertex 2 x
     * @param y2    vertex 2 y
     * @param x3    vertex 3 x
     * @param y3    vertex 3 y
     * @param x4    vertex 4 x
     * @param y4    vertex 4 y
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
     * Strokes a cubic bezier curve.
     *
     * @param sx          start x
     * @param sy          start y
     * @param c1x         control 1 x
     * @param c1y         control 1 y
     * @param c2x         control 2 x
     * @param c2y         control 2 y
     * @param ex          end x
     * @param ey          end y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#bezier(long, float, float, float, float, float, float, float, float, float, VGColor)
     */
    public VGCanvas bezier(float sx, float sy, float c1x, float c1y, float c2x, float c2y,
                          float ex, float ey, float strokeWidth, VGColor color) {
        VGDraw.bezier(vg, sx, sy, c1x, c1y, c2x, c2y, ex, ey, strokeWidth, color);
        return this;
    }

    /**
     * Strokes a quadratic curve.
     *
     * @param sx          start x
     * @param sy          start y
     * @param cx          control x
     * @param cy          control y
     * @param ex          end x
     * @param ey          end y
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#quadCurve(long, float, float, float, float, float, float, float, VGColor)
     */
    public VGCanvas quadCurve(float sx, float sy, float cx, float cy,
                             float ex, float ey, float strokeWidth, VGColor color) {
        VGDraw.quadCurve(vg, sx, sy, cx, cy, ex, ey, strokeWidth, color);
        return this;
    }

    /**
     * Strokes a polyline.
     *
     * @param points      flat array of point coordinates
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#polyline(long, float[], float, VGColor)
     */
    public VGCanvas polyline(float[] points, float strokeWidth, VGColor color) {
        VGDraw.polyline(vg, points, strokeWidth, color);
        return this;
    }

    /**
     * Strokes a polyline with line cap and join styling.
     *
     * @param points      flat array of point coordinates
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @param cap         line cap
     * @param join        line join
     * @return this canvas
     * @see VGDraw#polyline(long, float[], float, VGColor, VGLineCap, VGLineJoin)
     */
    public VGCanvas polyline(float[] points, float strokeWidth, VGColor color,
                             VGLineCap cap, VGLineJoin join) {
        VGDraw.polyline(vg, points, strokeWidth, color, cap, join);
        return this;
    }

    /**
     * Fills a polygon with color.
     *
     * @param points flat array of point coordinates
     * @param color  fill color
     * @return this canvas
     * @see VGDraw#polygon(long, float[], VGColor)
     */
    public VGCanvas polygon(float[] points, VGColor color) {
        VGDraw.polygon(vg, points, color);
        return this;
    }

    /**
     * Fills a polygon with paint.
     *
     * @param points flat array of point coordinates
     * @param paint  fill paint
     * @return this canvas
     * @see VGDraw#polygon(long, float[], VGPaint)
     */
    public VGCanvas polygon(float[] points, VGPaint paint) {
        VGDraw.polygon(vg, points, paint);
        return this;
    }

    /**
     * Strokes a polygon outline.
     *
     * @param points      flat array of point coordinates
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     * @see VGDraw#polygonOutline(long, float[], float, VGColor)
     */
    public VGCanvas polygonOutline(float[] points, float strokeWidth, VGColor color) {
        VGDraw.polygonOutline(vg, points, strokeWidth, color);
        return this;
    }

    /**
     * Begins a new path in NanoVG.
     *
     * @return this canvas
     */
    public VGCanvas beginPath() {
        if (vg != 0) nvgBeginPath(vg);
        return this;
    }

    /**
     * Closes the current path sub-contour.
     *
     * @return this canvas
     */
    public VGCanvas closePath() {
        if (vg != 0) nvgClosePath(vg);
        return this;
    }

    /**
     * Moves path pen to position (x, y).
     *
     * @param x destination x
     * @param y destination y
     * @return this canvas
     */
    public VGCanvas moveTo(float x, float y) {
        if (vg != 0) nvgMoveTo(vg, x, y);
        return this;
    }

    /**
     * Draws a line segment to position (x, y).
     *
     * @param x destination x
     * @param y destination y
     * @return this canvas
     */
    public VGCanvas lineTo(float x, float y) {
        if (vg != 0) nvgLineTo(vg, x, y);
        return this;
    }

    /**
     * Draws a cubic bezier segment to position (x, y).
     *
     * @param c1x control 1 x
     * @param c1y control 1 y
     * @param c2x control 2 x
     * @param c2y control 2 y
     * @param x   destination x
     * @param y   destination y
     * @return this canvas
     */
    public VGCanvas bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
        if (vg != 0) nvgBezierTo(vg, c1x, c1y, c2x, c2y, x, y);
        return this;
    }

    /**
     * Draws a quadratic curve segment to position (x, y).
     *
     * @param cx control point x
     * @param cy control point y
     * @param x  destination x
     * @param y  destination y
     * @return this canvas
     */
    public VGCanvas quadTo(float cx, float cy, float x, float y) {
        if (vg != 0) nvgQuadTo(vg, cx, cy, x, y);
        return this;
    }

    /**
     * Draws an arc segment between two tangent lines with a radius.
     *
     * @param x1     first tangent point x
     * @param y1     first tangent point y
     * @param x2     second tangent point x
     * @param y2     second tangent point y
     * @param radius arc radius
     * @return this canvas
     */
    public VGCanvas arcTo(float x1, float y1, float x2, float y2, float radius) {
        if (vg != 0) nvgArcTo(vg, x1, y1, x2, y2, radius);
        return this;
    }

    /**
     * Fills the active path with a solid color.
     *
     * @param color fill color
     * @return this canvas
     */
    public VGCanvas fill(VGColor color) {
        if (vg != 0 && color != null) {
            nvgFillColor(vg, color.nvg());
            nvgFill(vg);
        }
        return this;
    }

    /**
     * Fills the active path with a paint.
     *
     * @param paint fill paint
     * @return this canvas
     */
    public VGCanvas fill(VGPaint paint) {
        if (vg != 0 && paint != null) {
            nvgFillPaint(vg, paint.nvg());
            nvgFill(vg);
        }
        return this;
    }

    /**
     * Strokes the active path with a solid color.
     *
     * @param strokeWidth stroke width
     * @param color       stroke color
     * @return this canvas
     */
    public VGCanvas stroke(float strokeWidth, VGColor color) {
        if (vg != 0 && color != null) {
            nvgStrokeColor(vg, color.nvg());
            nvgStrokeWidth(vg, strokeWidth);
            nvgStroke(vg);
        }
        return this;
    }

    /**
     * Strokes the active path with a paint.
     *
     * @param strokeWidth stroke width
     * @param paint       stroke paint
     * @return this canvas
     */
    public VGCanvas stroke(float strokeWidth, VGPaint paint) {
        if (vg != 0 && paint != null) {
            nvgStrokePaint(vg, paint.nvg());
            nvgStrokeWidth(vg, strokeWidth);
            nvgStroke(vg);
        }
        return this;
    }

    /**
     * Sets the line cap style.
     *
     * @param cap native NanoVG line cap constant
     * @return this canvas
     * @see VGDraw#lineCap(long, int)
     */
    public VGCanvas lineCap(int cap) {
        VGDraw.lineCap(vg, cap);
        return this;
    }

    /**
     * Sets the line cap style using a {@link VGLineCap}.
     *
     * @param cap line cap enum
     * @return this canvas
     * @see VGDraw#lineCap(long, VGLineCap)
     */
    public VGCanvas lineCap(VGLineCap cap) {
        VGDraw.lineCap(vg, cap);
        return this;
    }

    /**
     * Sets the line join style.
     *
     * @param join native NanoVG line join constant
     * @return this canvas
     * @see VGDraw#lineJoin(long, int)
     */
    public VGCanvas lineJoin(int join) {
        VGDraw.lineJoin(vg, join);
        return this;
    }

    /**
     * Sets the line join style using a {@link VGLineJoin}.
     *
     * @param join line join enum
     * @return this canvas
     * @see VGDraw#lineJoin(long, VGLineJoin)
     */
    public VGCanvas lineJoin(VGLineJoin join) {
        VGDraw.lineJoin(vg, join);
        return this;
    }

    /**
     * Sets the miter limit.
     *
     * @param limit miter limit ratio
     * @return this canvas
     * @see VGDraw#miterLimit(long, float)
     */
    public VGCanvas miterLimit(float limit) {
        VGDraw.miterLimit(vg, limit);
        return this;
    }

    /**
     * Sets the global alpha factor.
     *
     * @param alpha alpha in [0, 1]
     * @return this canvas
     * @see VGDraw#globalAlpha(long, float)
     */
    public VGCanvas globalAlpha(float alpha) {
        VGDraw.globalAlpha(vg, alpha);
        return this;
    }

    /**
     * Sets the global composite operation.
     *
     * @param op native composite operation constant
     * @return this canvas
     * @see VGDraw#compositeOperation(long, int)
     */
    public VGCanvas compositeOperation(int op) {
        VGDraw.compositeOperation(vg, op);
        return this;
    }

    /**
     * Sets the global composite operation using a {@link VGCompositeOp}.
     *
     * @param op composite operation enum
     * @return this canvas
     * @see VGDraw#compositeOperation(long, VGCompositeOp)
     */
    public VGCanvas compositeOperation(VGCompositeOp op) {
        VGDraw.compositeOperation(vg, op);
        return this;
    }

    /**
     * Sets the global composite blend function.
     *
     * @param sfactor source factor
     * @param dfactor destination factor
     * @return this canvas
     * @see VGDraw#compositeBlendFunc(long, int, int)
     */
    public VGCanvas compositeBlendFunc(int sfactor, int dfactor) {
        VGDraw.compositeBlendFunc(vg, sfactor, dfactor);
        return this;
    }

    /**
     * Sets the global composite blend function using {@link VGBlendFactor}.
     *
     * @param sfactor source factor
     * @param dfactor destination factor
     * @return this canvas
     * @see VGDraw#compositeBlendFunc(long, VGBlendFactor, VGBlendFactor)
     */
    public VGCanvas compositeBlendFunc(VGBlendFactor sfactor, VGBlendFactor dfactor) {
        VGDraw.compositeBlendFunc(vg, sfactor, dfactor);
        return this;
    }

    /**
     * Sets separate blend function factors for RGB and alpha channels.
     *
     * @param srcRGB   source RGB factor
     * @param dstRGB   destination RGB factor
     * @param srcAlpha source alpha factor
     * @param dstAlpha destination alpha factor
     * @return this canvas
     * @see VGDraw#compositeBlendFuncSeparate(long, int, int, int, int)
     */
    public VGCanvas compositeBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
        VGDraw.compositeBlendFuncSeparate(vg, srcRGB, dstRGB, srcAlpha, dstAlpha);
        return this;
    }

    /**
     * Sets separate blend function factors for RGB and alpha channels using enums.
     *
     * @param srcRGB   source RGB factor
     * @param dstRGB   destination RGB factor
     * @param srcAlpha source alpha factor
     * @param dstAlpha destination alpha factor
     * @return this canvas
     * @see VGDraw#compositeBlendFuncSeparate(long, VGBlendFactor, VGBlendFactor, VGBlendFactor, VGBlendFactor)
     */
    public VGCanvas compositeBlendFuncSeparate(VGBlendFactor srcRGB, VGBlendFactor dstRGB,
                                              VGBlendFactor srcAlpha, VGBlendFactor dstAlpha) {
        VGDraw.compositeBlendFuncSeparate(vg, srcRGB, dstRGB, srcAlpha, dstAlpha);
        return this;
    }

    /**
     * Draws a drop shadow.
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
     * Draws a subtle 2 layer glow around a rounded rectangle.
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
     * Draws a line of text with default alignment.
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
     * Draws a line of text using a {@link VGFont}.
     *
     * @param text  text to draw
     * @param x     anchor x
     * @param y     anchor y
     * @param font  font handle
     * @param size  font size in pixels
     * @param color text color
     * @param align alignment flags
     * @return this canvas
     * @see VGDraw#text(long, String, float, float, VGFont, float, VGColor, int)
     */
    public VGCanvas text(String text, float x, float y, VGFont font, float size, VGColor color, int align) {
        VGDraw.text(vg, text, x, y, font, size, color, align);
        return this;
    }

    /**
     * Draws a line of text using a {@link VGFont} with default alignment.
     *
     * @param text  text to draw
     * @param x     left edge
     * @param y     top edge
     * @param font  font handle
     * @param size  font size in pixels
     * @param color text color
     * @return this canvas
     * @see VGDraw#text(long, String, float, float, VGFont, float, VGColor)
     */
    public VGCanvas text(String text, float x, float y, VGFont font, float size, VGColor color) {
        VGDraw.text(vg, text, x, y, font, size, color);
        return this;
    }

    /**
     * Draws multi-line wrapped text inside a bounding column width.
     *
     * @param text          text to draw
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width
     * @param font          font face name
     * @param size          font size in pixels
     * @param color         text color
     * @param align         alignment flags
     * @return this canvas
     * @see VGDraw#textBox(long, String, float, float, float, String, float, VGColor, int)
     */
    public VGCanvas textBox(String text, float x, float y, float breakRowWidth,
                            String font, float size, VGColor color, int align) {
        VGDraw.textBox(vg, text, x, y, breakRowWidth, font, size, color, align);
        return this;
    }

    /**
     * Draws multi-line wrapped text inside a bounding column width with default alignment.
     *
     * @param text          text to draw
     * @param x             left edge
     * @param y             top edge
     * @param breakRowWidth maximum line width
     * @param font          font face name
     * @param size          font size in pixels
     * @param color         text color
     * @return this canvas
     * @see VGDraw#textBox(long, String, float, float, float, String, float, VGColor)
     */
    public VGCanvas textBox(String text, float x, float y, float breakRowWidth,
                            String font, float size, VGColor color) {
        VGDraw.textBox(vg, text, x, y, breakRowWidth, font, size, color);
        return this;
    }

    /**
     * Draws multi-line wrapped text using a {@link VGFont}.
     *
     * @param text          text to draw
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width
     * @param font          font handle
     * @param size          font size in pixels
     * @param color         text color
     * @param align         alignment flags
     * @return this canvas
     * @see VGDraw#textBox(long, String, float, float, float, VGFont, float, VGColor, int)
     */
    public VGCanvas textBox(String text, float x, float y, float breakRowWidth,
                            VGFont font, float size, VGColor color, int align) {
        VGDraw.textBox(vg, text, x, y, breakRowWidth, font, size, color, align);
        return this;
    }

    /**
     * Draws multi-line wrapped text using a {@link VGFont} with default alignment.
     *
     * @param text          text to draw
     * @param x             left edge
     * @param y             top edge
     * @param breakRowWidth maximum line width
     * @param font          font handle
     * @param size          font size in pixels
     * @param color         text color
     * @return this canvas
     * @see VGDraw#textBox(long, String, float, float, float, VGFont, float, VGColor)
     */
    public VGCanvas textBox(String text, float x, float y, float breakRowWidth,
                            VGFont font, float size, VGColor color) {
        VGDraw.textBox(vg, text, x, y, breakRowWidth, font, size, color);
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
     * Measures the advance width of a line of text using a {@link VGFont}.
     *
     * @param text text to measure
     * @param font font handle
     * @param size font size in pixels
     * @return the advance width
     * @see VGDraw#textWidth(long, String, VGFont, float)
     */
    public float textWidth(String text, VGFont font, float size) {
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

    /**
     * Measures the bounding box of a line of text using a {@link VGFont}.
     *
     * @param text      text to measure
     * @param font      font handle
     * @param size      font size in pixels
     * @param outBounds destination array, or {@code null}
     * @return {@code [minX, minY, maxX, maxY]}
     * @see VGDraw#textBounds(long, String, VGFont, float, float[])
     */
    public float[] textBounds(String text, VGFont font, float size, float[] outBounds) {
        return VGDraw.textBounds(vg, text, font, size, outBounds);
    }

    /**
     * Measures the bounding box of multi-line wrapped text.
     *
     * @param text          text to measure
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width
     * @param font          font face name
     * @param size          font size in pixels
     * @param outBounds     destination array, or {@code null}
     * @return {@code [minX, minY, maxX, maxY]}
     * @see VGDraw#textBoxBounds(long, String, float, float, float, String, float, float[])
     */
    public float[] textBoxBounds(String text, float x, float y, float breakRowWidth,
                                 String font, float size, float[] outBounds) {
        return VGDraw.textBoxBounds(vg, text, x, y, breakRowWidth, font, size, outBounds);
    }

    /**
     * Measures the bounding box of multi-line wrapped text using a {@link VGFont}.
     *
     * @param text          text to measure
     * @param x             anchor x
     * @param y             anchor y
     * @param breakRowWidth maximum line width
     * @param font          font handle
     * @param size          font size in pixels
     * @param outBounds     destination array, or {@code null}
     * @return {@code [minX, minY, maxX, maxY]}
     * @see VGDraw#textBoxBounds(long, String, float, float, float, VGFont, float, float[])
     */
    public float[] textBoxBounds(String text, float x, float y, float breakRowWidth,
                                 VGFont font, float size, float[] outBounds) {
        return VGDraw.textBoxBounds(vg, text, x, y, breakRowWidth, font, size, outBounds);
    }

    /**
     * Computes the total rendered height of multi-line wrapped text.
     *
     * @param text          text to measure
     * @param breakRowWidth maximum line width
     * @param font          font face name
     * @param size          font size in pixels
     * @return total height in pixels
     * @see VGDraw#textBoxHeight(long, String, float, String, float)
     */
    public float textBoxHeight(String text, float breakRowWidth, String font, float size) {
        return VGDraw.textBoxHeight(vg, text, breakRowWidth, font, size);
    }

    /**
     * Computes the total rendered height of multi-line wrapped text using a {@link VGFont}.
     *
     * @param text          text to measure
     * @param breakRowWidth maximum line width
     * @param font          font handle
     * @param size          font size in pixels
     * @return total height in pixels
     * @see VGDraw#textBoxHeight(long, String, float, VGFont, float)
     */
    public float textBoxHeight(String text, float breakRowWidth, VGFont font, float size) {
        return VGDraw.textBoxHeight(vg, text, breakRowWidth, font, size);
    }

    /**
     * Sets the text line height multiplier.
     *
     * @param lineHeight line height multiplier
     * @return this canvas
     * @see VGDraw#textLineHeight(long, float)
     */
    public VGCanvas textLineHeight(float lineHeight) {
        VGDraw.textLineHeight(vg, lineHeight);
        return this;
    }

    /**
     * Sets the text letter spacing in pixels.
     *
     * @param spacing spacing in pixels
     * @return this canvas
     * @see VGDraw#textLetterSpacing(long, float)
     */
    public VGCanvas textLetterSpacing(float spacing) {
        VGDraw.textLetterSpacing(vg, spacing);
        return this;
    }

    /**
     * Draws an image into a rectangle with optional rounded corners and opacity.
     *
     * @param image  native image handle
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param alpha  opacity in [0, 1]
     * @return this canvas
     * @see VGDraw#image(long, int, float, float, float, float, float, float)
     */
    public VGCanvas image(int image, float x, float y, float w, float h, float radius, float alpha) {
        VGDraw.image(vg, image, x, y, w, h, radius, alpha);
        return this;
    }

    /**
     * Draws an opaque, unrounded image rectangle.
     *
     * @param image native image handle
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @return this canvas
     * @see VGDraw#image(long, int, float, float, float, float)
     */
    public VGCanvas image(int image, float x, float y, float w, float h) {
        VGDraw.image(vg, image, x, y, w, h);
        return this;
    }

    /**
     * Draws an image into a rectangle with optional rounded corners and opacity using a {@link VGImage}.
     *
     * @param image  image instance
     * @param x      left edge
     * @param y      top edge
     * @param w      width
     * @param h      height
     * @param radius corner radius
     * @param alpha  opacity in [0, 1]
     * @return this canvas
     * @see VGDraw#image(long, VGImage, float, float, float, float, float, float)
     */
    public VGCanvas image(VGImage image, float x, float y, float w, float h, float radius, float alpha) {
        VGDraw.image(vg, image, x, y, w, h, radius, alpha);
        return this;
    }

    /**
     * Draws an opaque, unrounded image rectangle using a {@link VGImage}.
     *
     * @param image image instance
     * @param x     left edge
     * @param y     top edge
     * @param w     width
     * @param h     height
     * @return this canvas
     * @see VGDraw#image(long, VGImage, float, float, float, float)
     */
    public VGCanvas image(VGImage image, float x, float y, float w, float h) {
        VGDraw.image(vg, image, x, y, w, h);
        return this;
    }

    /**
     * Deletes a texture image on the GPU.
     *
     * @param image native image handle
     * @return this canvas
     * @see VGDraw#deleteImage(long, int)
     */
    public VGCanvas deleteImage(int image) {
        VGDraw.deleteImage(vg, image);
        return this;
    }

    /**
     * Deletes a texture image on the GPU using a {@link VGImage}.
     *
     * @param image image instance
     * @return this canvas
     * @see VGDraw#deleteImage(long, VGImage)
     */
    public VGCanvas deleteImage(VGImage image) {
        VGDraw.deleteImage(vg, image);
        return this;
    }

    /**
     * Queries the dimensions of an image handle.
     *
     * @param image   image handle
     * @param outSize destination array [width, height], or {@code null}
     * @return array holding [width, height]
     * @see VGDraw#imageSize(long, int, int[])
     */
    public int[] imageSize(int image, int[] outSize) {
        return VGDraw.imageSize(vg, image, outSize);
    }

    /**
     * Queries the dimensions of an image handle without heap allocation.
     *
     * @param image image handle
     * @return array holding [width, height]
     * @see VGDraw#imageSize(long, int)
     */
    public int[] imageSize(int image) {
        return VGDraw.imageSize(vg, image);
    }

    /**
     * Creates and loads an image with flags.
     *
     * @param path  file path
     * @param flags image creation flags
     * @return loaded image
     */
    public VGImage createImage(String path, int flags) {
        return VGImage.load(vg, path, flags);
    }

    /**
     * Creates and loads an image with default flags (0).
     *
     * @param path file path
     * @return loaded image
     */
    public VGImage createImage(String path) {
        return VGImage.load(vg, path);
    }

    /**
     * Creates and loads an image from an input stream with flags.
     *
     * @param stream input stream
     * @param flags  image creation flags
     * @return loaded image
     */
    public VGImage createImage(InputStream stream, int flags) {
        return VGImage.load(vg, stream, flags);
    }

    /**
     * Creates and loads an image from an input stream with default flags (0).
     *
     * @param stream input stream
     * @return loaded image
     */
    public VGImage createImage(InputStream stream) {
        return VGImage.load(vg, stream);
    }

    /**
     * Creates and loads an image from a classpath resource with flags.
     *
     * @param resourcePath classpath path
     * @param flags        image creation flags
     * @return loaded image
     */
    public VGImage createImageFromResource(String resourcePath, int flags) {
        return VGImage.loadResource(vg, resourcePath, flags);
    }

    /**
     * Creates and loads an image from a classpath resource with default flags (0).
     *
     * @param resourcePath classpath path
     * @return loaded image
     */
    public VGImage createImageFromResource(String resourcePath) {
        return VGImage.loadResource(vg, resourcePath);
    }

    /**
     * Creates and registers a font face from an on-disk file path.
     *
     * @param name font face name
     * @param path file path
     * @return loaded font
     */
    public VGFont createFont(String name, String path) {
        return VGFont.load(vg, name, path);
    }

    /**
     * Creates and registers a font face from an input stream.
     *
     * @param name   font face name
     * @param stream input stream
     * @return loaded font
     */
    public VGFont createFont(String name, InputStream stream) {
        return VGFont.load(vg, name, stream);
    }

    /**
     * Creates and registers a font face from a classpath resource.
     *
     * @param name         font face name
     * @param resourcePath resource path
     * @return loaded font
     */
    public VGFont createFontFromResource(String name, String resourcePath) {
        return VGFont.loadResource(vg, name, resourcePath);
    }
}
