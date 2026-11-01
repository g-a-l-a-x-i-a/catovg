package space.rawrclient.catovg.color;

import org.lwjgl.nanovg.NVGColor;

import java.awt.Color;

/**
 * Immutable RGBA color with float components in the range {@code [0, 1]}.
 * <p>
 * All components are clamped on construction. Conversion to the native
 * {@link NVGColor} struct is allocation-free through either
 * {@link #toNVG(NVGColor)} or the thread-local pool used by {@link #nvg()}.
 *
 * @param r red component
 * @param g green component
 * @param b blue component
 * @param a alpha component
 */
public record VGColor(float r, float g, float b, float a) {

    /** Opaque white. */
    public static final VGColor WHITE = new VGColor(1.0f, 1.0f, 1.0f, 1.0f);
    /** Opaque black. */
    public static final VGColor BLACK = new VGColor(0.0f, 0.0f, 0.0f, 1.0f);
    /** Fully transparent black. */
    public static final VGColor TRANSPARENT = new VGColor(0.0f, 0.0f, 0.0f, 0.0f);
    /** Opaque red. */
    public static final VGColor RED = new VGColor(1.0f, 0.0f, 0.0f, 1.0f);
    /** Opaque green. */
    public static final VGColor GREEN = new VGColor(0.0f, 1.0f, 0.0f, 1.0f);
    /** Opaque blue. */
    public static final VGColor BLUE = new VGColor(0.0f, 0.0f, 1.0f, 1.0f);

    private static final float INV_255 = 1.0f / 255.0f;

    /** Pool size; must be a power of 2 so the index can wrap with a bit mask. */
    private static final int POOL_SIZE = 32;
    private static final int POOL_MASK = POOL_SIZE - 1;

    /** Per-thread ring buffer of native color structs used by {@link #nvg()}. */
    private static final ThreadLocal<Pool> POOL = ThreadLocal.withInitial(Pool::new);

    /**
     * Canonical constructor. Components outside {@code [0, 1]} are clamped.
     */
    public VGColor {
        r = clamp(r);
        g = clamp(g);
        b = clamp(b);
        a = clamp(a);
    }

    /**
     * Creates an opaque color from float components.
     *
     * @param r red in {@code [0, 1]}
     * @param g green in {@code [0, 1]}
     * @param b blue in {@code [0, 1]}
     * @return the color
     */
    public static VGColor rgb(float r, float g, float b) {
        return new VGColor(r, g, b, 1.0f);
    }

    /**
     * Creates a color from float components.
     *
     * @param r red in {@code [0, 1]}
     * @param g green in {@code [0, 1]}
     * @param b blue in {@code [0, 1]}
     * @param a alpha in {@code [0, 1]}
     * @return the color
     */
    public static VGColor rgba(float r, float g, float b, float a) {
        return new VGColor(r, g, b, a);
    }

    /**
     * Creates an opaque color from 8-bit components.
     *
     * @param r red in {@code [0, 255]}
     * @param g green in {@code [0, 255]}
     * @param b blue in {@code [0, 255]}
     * @return the color
     */
    public static VGColor rgb(int r, int g, int b) {
        return new VGColor(r * INV_255, g * INV_255, b * INV_255, 1.0f);
    }

    /**
     * Creates a color from 8-bit components.
     *
     * @param r red in {@code [0, 255]}
     * @param g green in {@code [0, 255]}
     * @param b blue in {@code [0, 255]}
     * @param a alpha in {@code [0, 255]}
     * @return the color
     */
    public static VGColor rgba(int r, int g, int b, int a) {
        return new VGColor(r * INV_255, g * INV_255, b * INV_255, a * INV_255);
    }

    /**
     * Creates a color from a packed {@code 0xAARRGGBB} integer.
     *
     * @param argb packed color
     * @return the color
     */
    public static VGColor argb(int argb) {
        return argb(argb, 1.0f);
    }

    /**
     * Creates a color from a packed {@code 0xAARRGGBB} integer, scaling its alpha.
     *
     * @param argb     packed color
     * @param alphaMul multiplier applied to the alpha channel
     * @return the color
     */
    public static VGColor argb(int argb, float alphaMul) {
        return new VGColor(
                ((argb >>> 16) & 0xFF) * INV_255,
                ((argb >>> 8) & 0xFF) * INV_255,
                (argb & 0xFF) * INV_255,
                ((argb >>> 24) & 0xFF) * INV_255 * alphaMul
        );
    }

    /**
     * Converts an AWT color.
     *
     * @param color source color, may be {@code null}
     * @return the converted color, or {@link #WHITE} if {@code color} is {@code null}
     */
    public static VGColor of(Color color) {
        if (color == null) {
            return WHITE;
        }
        return argb(color.getRGB());
    }

    /**
     * Parses a hex color string in {@code RGB}, {@code RRGGBB} or {@code RRGGBBAA}
     * form, with or without a leading {@code #}.
     *
     * @param hex hex string, may be {@code null}
     * @return the parsed color, or {@link #WHITE} if the input is missing or malformed
     */
    public static VGColor hex(String hex) {
        if (hex == null || hex.isEmpty()) {
            return WHITE;
        }
        int start = hex.charAt(0) == '#' ? 1 : 0;
        int length = hex.length() - start;
        if (length != 3 && length != 6 && length != 8) {
            return WHITE;
        }

        int value;
        try {
            value = Integer.parseUnsignedInt(hex, start, hex.length(), 16);
        } catch (NumberFormatException e) {
            return WHITE;
        }

        return switch (length) {
            case 3 -> rgb(
                    ((value >>> 8) & 0xF) * 17,
                    ((value >>> 4) & 0xF) * 17,
                    (value & 0xF) * 17);
            case 6 -> rgb(
                    (value >>> 16) & 0xFF,
                    (value >>> 8) & 0xFF,
                    value & 0xFF);
            default -> rgba(
                    (value >>> 24) & 0xFF,
                    (value >>> 16) & 0xFF,
                    (value >>> 8) & 0xFF,
                    value & 0xFF);
        };
    }

    /**
     * Returns a copy of this color with the given alpha.
     *
     * @param alpha new alpha in {@code [0, 1]}
     * @return the new color
     */
    public VGColor withAlpha(float alpha) {
        return new VGColor(r, g, b, alpha);
    }

    /**
     * Returns a copy of this color with its alpha multiplied by {@code factor}.
     *
     * @param factor alpha multiplier
     * @return the new color
     */
    public VGColor multiplyAlpha(float factor) {
        return new VGColor(r, g, b, a * factor);
    }

    /**
     * Returns a copy with the RGB channels multiplied by {@code factor}.
     *
     * @param factor channel multiplier, typically greater than {@code 1}
     * @return the new color
     */
    public VGColor brighter(float factor) {
        return new VGColor(r * factor, g * factor, b * factor, a);
    }

    /**
     * Returns a copy with the RGB channels reduced by {@code factor}.
     *
     * @param factor amount to darken in {@code [0, 1]}; {@code 1} yields black
     * @return the new color
     */
    public VGColor darker(float factor) {
        float scale = 1.0f - factor;
        return new VGColor(r * scale, g * scale, b * scale, a);
    }

    /**
     * Linearly interpolates between this color and {@code to}.
     *
     * @param to target color, may be {@code null}
     * @param t  interpolation factor, clamped to {@code [0, 1]}
     * @return the interpolated color, or {@code this} if {@code to} is {@code null}
     */
    public VGColor lerp(VGColor to, float t) {
        if (to == null) {
            return this;
        }
        float f = clamp(t);
        return new VGColor(
                r + (to.r - r) * f,
                g + (to.g - g) * f,
                b + (to.b - b) * f,
                a + (to.a - a) * f
        );
    }

    /**
     * Linearly interpolates between color {@code a} and color {@code b}.
     *
     * @param a start color, may be {@code null}
     * @param b end color, may be {@code null}
     * @param t interpolation factor, clamped to {@code [0, 1]}
     * @return the interpolated color
     */
    public static VGColor lerp(VGColor a, VGColor b, float t) {
        if (a == null) {
            return b != null ? b : WHITE;
        }
        return a.lerp(b, t);
    }

    /**
     * Mixes this color with {@code other} by the given ratio.
     *
     * @param other target color, may be {@code null}
     * @param ratio mix ratio, clamped to {@code [0, 1]}
     * @return the mixed color
     */
    public VGColor mix(VGColor other, float ratio) {
        return lerp(other, ratio);
    }

    /**
     * Creates a color from HSB (hue, saturation, brightness) components with alpha.
     *
     * @param h hue in {@code [0, 1]}
     * @param s saturation in {@code [0, 1]}
     * @param b brightness in {@code [0, 1]}
     * @param a alpha in {@code [0, 1]}
     * @return the color
     */
    public static VGColor fromHSB(float h, float s, float b, float a) {
        float sat = clamp(s);
        float br = clamp(b);
        if (sat <= 0.0f) {
            return new VGColor(br, br, br, a);
        }
        float hue = wrap01(h) * 6.0f;
        int sector = (int) hue;
        float frac = hue - sector;
        float p = br * (1.0f - sat);
        float q = br * (1.0f - sat * frac);
        float t = br * (1.0f - sat * (1.0f - frac));
        return switch (sector % 6) {
            case 0 -> new VGColor(br, t, p, a);
            case 1 -> new VGColor(q, br, p, a);
            case 2 -> new VGColor(p, br, t, a);
            case 3 -> new VGColor(p, q, br, a);
            case 4 -> new VGColor(t, p, br, a);
            default -> new VGColor(br, p, q, a);
        };
    }

    /**
     * Creates an opaque color from HSB (hue, saturation, brightness) components.
     *
     * @param h hue in {@code [0, 1]}
     * @param s saturation in {@code [0, 1]}
     * @param b brightness in {@code [0, 1]}
     * @return the color
     */
    public static VGColor fromHSB(float h, float s, float b) {
        return fromHSB(h, s, b, 1.0f);
    }

    /**
     * Creates a color from HSL (hue, saturation, lightness) components with alpha.
     *
     * @param h hue in {@code [0, 1]}
     * @param s saturation in {@code [0, 1]}
     * @param l lightness in {@code [0, 1]}
     * @param a alpha in {@code [0, 1]}
     * @return the color
     */
    public static VGColor fromHSL(float h, float s, float l, float a) {
        float sat = clamp(s);
        float light = clamp(l);
        if (sat <= 0.0f) {
            return new VGColor(light, light, light, a);
        }
        float c = (1.0f - Math.abs(2.0f * light - 1.0f)) * sat;
        float hue = wrap01(h) * 6.0f;
        float x = c * (1.0f - Math.abs((hue % 2.0f) - 1.0f));
        float m = light - c * 0.5f;
        int sector = (int) hue;
        float r1, g1, b1;
        switch (sector % 6) {
            case 0 -> { r1 = c; g1 = x; b1 = 0.0f; }
            case 1 -> { r1 = x; g1 = c; b1 = 0.0f; }
            case 2 -> { r1 = 0.0f; g1 = c; b1 = x; }
            case 3 -> { r1 = 0.0f; g1 = x; b1 = c; }
            case 4 -> { r1 = x; g1 = 0.0f; b1 = c; }
            default -> { r1 = c; g1 = 0.0f; b1 = x; }
        }
        return new VGColor(r1 + m, g1 + m, b1 + m, a);
    }

    /**
     * Creates an opaque color from HSL (hue, saturation, lightness) components.
     *
     * @param h hue in {@code [0, 1]}
     * @param s saturation in {@code [0, 1]}
     * @param l lightness in {@code [0, 1]}
     * @return the color
     */
    public static VGColor fromHSL(float h, float s, float l) {
        return fromHSL(h, s, l, 1.0f);
    }

    /**
     * Converts this color to an AWT color.
     *
     * @return the AWT color
     */
    public Color toAwt() {
        return new Color(toARGB(), true);
    }

    /**
     * Packs this color into a {@code 0xAARRGGBB} integer.
     *
     * @return the packed color
     */
    public int toARGB() {
        return (to8Bit(a) << 24) | (to8Bit(r) << 16) | (to8Bit(g) << 8) | to8Bit(b);
    }

    /**
     * Writes this color into the given native struct.
     *
     * @param target destination struct
     * @return {@code target}
     */
    public NVGColor toNVG(NVGColor target) {
        return target.r(r).g(g).b(b).a(a);
    }

    /**
     * Writes this color into a pooled, thread-local native struct.
     * <p>
     * The returned struct is reused after {@value #POOL_SIZE} further calls on
     * the same thread, so it must be consumed immediately and never stored.
     *
     * @return a pooled struct holding this color
     */
    public NVGColor nvg() {
        return toNVG(POOL.get().next());
    }

    @Override
    public String toString() {
        return "VGColor[r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]";
    }

    private static float clamp(float v) {
        return Math.clamp(v, 0.0f, 1.0f);
    }

    private static float wrap01(float v) {
        float f = v % 1.0f;
        return f < 0.0f ? f + 1.0f : f;
    }

    private static int to8Bit(float v) {
        return Math.round(v * 255.0f) & 0xFF;
    }

    /** Ring buffer of native color structs owned by a single thread. */
    private static final class Pool {
        private final NVGColor[] colors = new NVGColor[POOL_SIZE];
        private int index;

        Pool() {
            for (int i = 0; i < POOL_SIZE; i++) {
                colors[i] = NVGColor.create();
            }
        }

        NVGColor next() {
            NVGColor color = colors[index];
            index = (index + 1) & POOL_MASK;
            return color;
        }
    }
}
