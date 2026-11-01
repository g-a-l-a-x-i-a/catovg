package space.rawrclient.catovg.paint;

import space.rawrclient.catovg.color.VGColor;

import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable descriptor for multi-stop linear and radial gradients.
 */
public final class VGGradient {

    public enum Type {
        LINEAR,
        RADIAL
    }

    private final Type type;
    private final float x0;
    private final float y0;
    private final float x1;
    private final float y1;
    private final VGColor[] colors;
    private final float[] stops;

    private VGGradient(Type type, float x0, float y0, float x1, float y1, VGColor[] colors, float[] stops) {
        this.type = type;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.colors = colors;
        this.stops = stops;
    }

    /**
     * Creates a multi-stop linear gradient between two points with custom stop offsets.
     *
     * @param sx     start x
     * @param sy     start y
     * @param ex     end x
     * @param ey     end y
     * @param colors color stops, at least 2 non-null colors
     * @param stops  fractional stop offsets in {@code [0, 1]}, matching colors length
     * @return the gradient
     */
    public static VGGradient linear(float sx, float sy, float ex, float ey, VGColor[] colors, float[] stops) {
        validate(colors, stops);
        return new VGGradient(Type.LINEAR, sx, sy, ex, ey, colors.clone(), stops.clone());
    }

    /**
     * Creates a multi-stop linear gradient between two points with evenly distributed stops.
     *
     * @param sx     start x
     * @param sy     start y
     * @param ex     end x
     * @param ey     end y
     * @param colors color stops, at least 2 non-null colors
     * @return the gradient
     */
    public static VGGradient linear(float sx, float sy, float ex, float ey, VGColor... colors) {
        Objects.requireNonNull(colors, "colors");
        float[] stops = evenStops(colors.length);
        return linear(sx, sy, ex, ey, colors, stops);
    }

    /**
     * Creates a multi-stop radial gradient with custom stop offsets.
     *
     * @param cx     center x
     * @param cy     center y
     * @param inR    inner radius
     * @param outR   outer radius
     * @param colors color stops, at least 2 non-null colors
     * @param stops  fractional stop offsets in {@code [0, 1]}, matching colors length
     * @return the gradient
     */
    public static VGGradient radial(float cx, float cy, float inR, float outR, VGColor[] colors, float[] stops) {
        validate(colors, stops);
        return new VGGradient(Type.RADIAL, cx, cy, inR, outR, colors.clone(), stops.clone());
    }

    /**
     * Creates a multi-stop radial gradient with evenly distributed stops.
     *
     * @param cx     center x
     * @param cy     center y
     * @param inR    inner radius
     * @param outR   outer radius
     * @param colors color stops, at least 2 non-null colors
     * @return the gradient
     */
    public static VGGradient radial(float cx, float cy, float inR, float outR, VGColor... colors) {
        Objects.requireNonNull(colors, "colors");
        float[] stops = evenStops(colors.length);
        return radial(cx, cy, inR, outR, colors, stops);
    }

    /**
     * Evaluates the color at position {@code t} in {@code [0, 1]} along the gradient.
     *
     * @param t fractional position
     * @return the interpolated color
     */
    public VGColor evaluate(float t) {
        if (t <= stops[0]) return colors[0];
        int last = stops.length - 1;
        if (t >= stops[last]) return colors[last];

        for (int i = 0; i < last; i++) {
            float s0 = stops[i];
            float s1 = stops[i + 1];
            if (t >= s0 && t <= s1) {
                float range = s1 - s0;
                float localT = range > 1e-6f ? (t - s0) / range : 0.0f;
                return colors[i].lerp(colors[i + 1], localT);
            }
        }
        return colors[last];
    }

    /**
     * Creates a {@link VGPaint} for a specific segment between {@code stops[segmentIndex]} and {@code stops[segmentIndex + 1]}.
     *
     * @param vg           NanoVG context handle
     * @param segmentIndex index in {@code [0, stopCount() - 2]}
     * @return the segment paint, or {@code null} if vg is 0
     */
    public VGPaint paintSegment(long vg, int segmentIndex) {
        if (vg == 0) return null;
        if (segmentIndex < 0 || segmentIndex >= colors.length - 1) {
            throw new IndexOutOfBoundsException("Segment index out of range: " + segmentIndex);
        }
        float s0 = stops[segmentIndex];
        float s1 = stops[segmentIndex + 1];
        VGColor c0 = colors[segmentIndex];
        VGColor c1 = colors[segmentIndex + 1];

        if (type == Type.LINEAR) {
            float sx = x0 + (x1 - x0) * s0;
            float sy = y0 + (y1 - y0) * s0;
            float ex = x0 + (x1 - x0) * s1;
            float ey = y0 + (y1 - y0) * s1;
            return VGPaint.linearGradient(vg, sx, sy, ex, ey, c0, c1);
        } else {
            float inR = x1 + (y1 - x1) * s0;
            float outR = x1 + (y1 - x1) * s1;
            return VGPaint.radialGradient(vg, x0, y0, inR, outR, c0, c1);
        }
    }

    public Type type() { return type; }
    public float x0() { return x0; }
    public float y0() { return y0; }
    public float x1() { return x1; }
    public float y1() { return y1; }
    public VGColor[] colors() { return colors.clone(); }
    public float[] stops() { return stops.clone(); }
    public int stopCount() { return colors.length; }

    private static void validate(VGColor[] colors, float[] stops) {
        Objects.requireNonNull(colors, "colors");
        Objects.requireNonNull(stops, "stops");
        if (colors.length < 2) {
            throw new IllegalArgumentException("Gradient must contain at least 2 color stops");
        }
        if (colors.length != stops.length) {
            throw new IllegalArgumentException("Colors and stops length mismatch: " + colors.length + " vs " + stops.length);
        }
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == null) {
                throw new NullPointerException("Null color stop at index " + i);
            }
        }
    }

    private static float[] evenStops(int length) {
        if (length < 2) {
            throw new IllegalArgumentException("Gradient must contain at least 2 color stops");
        }
        float[] stops = new float[length];
        float step = 1.0f / (length - 1);
        for (int i = 0; i < length; i++) {
            stops[i] = i * step;
        }
        stops[length - 1] = 1.0f;
        return stops;
    }
}
