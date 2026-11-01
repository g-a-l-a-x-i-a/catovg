package space.rawrclient.catovg.path;

import org.lwjgl.nanovg.NanoVG;

import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable parsed SVG vector path representation.
 * <p>
 * Parses SVG path data strings containing commands {@code M, m, L, l, H, h, V, v, C, c, S, s, Q, q, T, t, A, a, Z, z}
 * into a compact sequence of native NanoVG drawing primitives.
 */
public final class VGPath {

    public static final byte OP_MOVE_TO = 0;
    public static final byte OP_LINE_TO = 1;
    public static final byte OP_BEZIER_TO = 2;
    public static final byte OP_QUAD_TO = 3;
    public static final byte OP_CLOSE = 4;

    private final float[] data;
    private final float minX;
    private final float minY;
    private final float maxX;
    private final float maxY;

    private VGPath(float[] data, float minX, float minY, float maxX, float maxY) {
        this.data = data;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Parses an SVG path data string.
     *
     * @param svgPath SVG path data string, non-null
     * @return the parsed path
     * @throws IllegalArgumentException if the path syntax is invalid
     */
    public static VGPath of(String svgPath) {
        Objects.requireNonNull(svgPath, "svgPath");
        return Parser.parse(svgPath);
    }

    /**
     * Emits the path commands to the given NanoVG context handle.
     * Does not call {@link NanoVG#nvgBeginPath(long)}, allowing this path to be used as a subpath.
     *
     * @param vg NanoVG context handle
     */
    public void apply(long vg) {
        if (vg == 0 || data.length == 0) return;
        int i = 0;
        int len = data.length;
        while (i < len) {
            int op = (int) data[i++];
            switch (op) {
                case OP_MOVE_TO -> {
                    NanoVG.nvgMoveTo(vg, data[i++], data[i++]);
                }
                case OP_LINE_TO -> {
                    NanoVG.nvgLineTo(vg, data[i++], data[i++]);
                }
                case OP_BEZIER_TO -> {
                    NanoVG.nvgBezierTo(vg, data[i++], data[i++], data[i++], data[i++], data[i++], data[i++]);
                }
                case OP_QUAD_TO -> {
                    NanoVG.nvgQuadTo(vg, data[i++], data[i++], data[i++], data[i++]);
                }
                case OP_CLOSE -> {
                    NanoVG.nvgClosePath(vg);
                }
                default -> throw new IllegalStateException("Unknown path opcode: " + op);
            }
        }
    }

    /**
     * Returns the bounding box of all control points and endpoints in the path.
     *
     * @param out destination array of length at least 4 {@code [minX, minY, maxX, maxY]}, or {@code null}
     * @return the bounding box array
     */
    public float[] bounds(float[] out) {
        float[] res = (out != null && out.length >= 4) ? out : new float[4];
        res[0] = minX;
        res[1] = minY;
        res[2] = maxX;
        res[3] = maxY;
        return res;
    }

    public float minX() { return minX; }
    public float minY() { return minY; }
    public float maxX() { return maxX; }
    public float maxY() { return maxY; }
    public float width() { return Math.max(0.0f, maxX - minX); }
    public float height() { return Math.max(0.0f, maxY - minY); }

    private static final class Parser {
        private final String s;
        private final int len;
        private int pos;

        private float[] buf = new float[64];
        private int count = 0;

        private float curX = 0.0f;
        private float curY = 0.0f;
        private float startX = 0.0f;
        private float startY = 0.0f;

        private float lastCtrlX = 0.0f;
        private float lastCtrlY = 0.0f;
        private char lastCmd = 0;

        private float minX = Float.POSITIVE_INFINITY;
        private float minY = Float.POSITIVE_INFINITY;
        private float maxX = Float.NEGATIVE_INFINITY;
        private float maxY = Float.NEGATIVE_INFINITY;

        private Parser(String s) {
            this.s = s;
            this.len = s.length();
        }

        static VGPath parse(String path) {
            Parser p = new Parser(path);
            p.execute();
            if (p.count == 0 || Float.isInfinite(p.minX)) {
                return new VGPath(new float[0], 0.0f, 0.0f, 0.0f, 0.0f);
            }
            float[] finalData = Arrays.copyOf(p.buf, p.count);
            return new VGPath(finalData, p.minX, p.minY, p.maxX, p.maxY);
        }

        private void execute() {
            skipWhitespaceAndSeparators();
            char currentCmd = 0;

            while (pos < len) {
                char c = s.charAt(pos);
                if (isCommand(c)) {
                    currentCmd = c;
                    pos++;
                } else if (currentCmd == 0) {
                    throw new IllegalArgumentException("Expected SVG path command at index " + pos);
                }

                skipWhitespaceAndSeparators();

                switch (currentCmd) {
                    case 'M', 'm' -> {
                        boolean rel = currentCmd == 'm';
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel && lastCmd != 0) {
                            x += curX;
                            y += curY;
                        }
                        emitMoveTo(x, y);
                        startX = curX;
                        startY = curY;
                        currentCmd = rel ? 'l' : 'L';
                    }
                    case 'L', 'l' -> {
                        boolean rel = currentCmd == 'l';
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel) {
                            x += curX;
                            y += curY;
                        }
                        emitLineTo(x, y);
                    }
                    case 'H', 'h' -> {
                        boolean rel = currentCmd == 'h';
                        float x = nextNumber();
                        if (rel) x += curX;
                        emitLineTo(x, curY);
                    }
                    case 'V', 'v' -> {
                        boolean rel = currentCmd == 'v';
                        float y = nextNumber();
                        if (rel) y += curY;
                        emitLineTo(curX, y);
                    }
                    case 'C', 'c' -> {
                        boolean rel = currentCmd == 'c';
                        float c1x = nextNumber();
                        float c1y = nextNumber();
                        float c2x = nextNumber();
                        float c2y = nextNumber();
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel) {
                            c1x += curX; c1y += curY;
                            c2x += curX; c2y += curY;
                            x += curX; y += curY;
                        }
                        emitBezierTo(c1x, c1y, c2x, c2y, x, y);
                    }
                    case 'S', 's' -> {
                        boolean rel = currentCmd == 's';
                        float c2x = nextNumber();
                        float c2y = nextNumber();
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel) {
                            c2x += curX; c2y += curY;
                            x += curX; y += curY;
                        }
                        float c1x = curX;
                        float c1y = curY;
                        if (lastCmd == 'C' || lastCmd == 'c' || lastCmd == 'S' || lastCmd == 's') {
                            c1x = 2.0f * curX - lastCtrlX;
                            c1y = 2.0f * curY - lastCtrlY;
                        }
                        emitBezierTo(c1x, c1y, c2x, c2y, x, y);
                    }
                    case 'Q', 'q' -> {
                        boolean rel = currentCmd == 'q';
                        float cx = nextNumber();
                        float cy = nextNumber();
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel) {
                            cx += curX; cy += curY;
                            x += curX; y += curY;
                        }
                        emitQuadTo(cx, cy, x, y);
                    }
                    case 'T', 't' -> {
                        boolean rel = currentCmd == 't';
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel) {
                            x += curX; y += curY;
                        }
                        float cx = curX;
                        float cy = curY;
                        if (lastCmd == 'Q' || lastCmd == 'q' || lastCmd == 'T' || lastCmd == 't') {
                            cx = 2.0f * curX - lastCtrlX;
                            cy = 2.0f * curY - lastCtrlY;
                        }
                        emitQuadTo(cx, cy, x, y);
                    }
                    case 'A', 'a' -> {
                        boolean rel = currentCmd == 'a';
                        float rx = nextNumber();
                        float ry = nextNumber();
                        float angle = nextNumber();
                        float largeArc = nextFlag();
                        float sweep = nextFlag();
                        float x = nextNumber();
                        float y = nextNumber();
                        if (rel) {
                            x += curX;
                            y += curY;
                        }
                        emitArcTo(rx, ry, angle, largeArc != 0.0f, sweep != 0.0f, x, y);
                    }
                    case 'Z', 'z' -> {
                        emitClose();
                        curX = startX;
                        curY = startY;
                    }
                    default -> throw new IllegalArgumentException("Unsupported SVG path command: " + currentCmd);
                }

                lastCmd = currentCmd;
                skipWhitespaceAndSeparators();
            }
        }

        private void ensureCap(int additional) {
            if (count + additional > buf.length) {
                buf = Arrays.copyOf(buf, Math.max(buf.length * 2, count + additional));
            }
        }

        private void updateBounds(float x, float y) {
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }

        private void emitMoveTo(float x, float y) {
            ensureCap(3);
            buf[count++] = OP_MOVE_TO;
            buf[count++] = x;
            buf[count++] = y;
            curX = x;
            curY = y;
            updateBounds(x, y);
        }

        private void emitLineTo(float x, float y) {
            ensureCap(3);
            buf[count++] = OP_LINE_TO;
            buf[count++] = x;
            buf[count++] = y;
            curX = x;
            curY = y;
            updateBounds(x, y);
        }

        private void emitBezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
            ensureCap(7);
            buf[count++] = OP_BEZIER_TO;
            buf[count++] = c1x;
            buf[count++] = c1y;
            buf[count++] = c2x;
            buf[count++] = c2y;
            buf[count++] = x;
            buf[count++] = y;
            lastCtrlX = c2x;
            lastCtrlY = c2y;
            curX = x;
            curY = y;
            updateBounds(c1x, c1y);
            updateBounds(c2x, c2y);
            updateBounds(x, y);
        }

        private void emitQuadTo(float cx, float cy, float x, float y) {
            ensureCap(5);
            buf[count++] = OP_QUAD_TO;
            buf[count++] = cx;
            buf[count++] = cy;
            buf[count++] = x;
            buf[count++] = y;
            lastCtrlX = cx;
            lastCtrlY = cy;
            curX = x;
            curY = y;
            updateBounds(cx, cy);
            updateBounds(x, y);
        }

        private void emitClose() {
            ensureCap(1);
            buf[count++] = OP_CLOSE;
        }

        private void emitArcTo(float rx, float ry, float angleDeg, boolean largeArc, boolean sweep, float x2, float y2) {
            float x1 = curX;
            float y1 = curY;
            if (x1 == x2 && y1 == y2) return;
            rx = Math.abs(rx);
            ry = Math.abs(ry);
            if (rx <= 1e-6f || ry <= 1e-6f) {
                emitLineTo(x2, y2);
                return;
            }

            float phi = (float) Math.toRadians(angleDeg % 360.0);
            float cosPhi = (float) Math.cos(phi);
            float sinPhi = (float) Math.sin(phi);

            float dx = (x1 - x2) * 0.5f;
            float dy = (y1 - y2) * 0.5f;
            float x1p = cosPhi * dx + sinPhi * dy;
            float y1p = -sinPhi * dx + cosPhi * dy;

            float x1pSq = x1p * x1p;
            float y1pSq = y1p * y1p;
            float rxSq = rx * rx;
            float rySq = ry * ry;

            float lambda = (x1pSq / rxSq) + (y1pSq / rySq);
            if (lambda > 1.0f) {
                float sqrtLambda = (float) Math.sqrt(lambda);
                rx *= sqrtLambda;
                ry *= sqrtLambda;
                rxSq = rx * rx;
                rySq = ry * ry;
            }

            float sign = (largeArc == sweep) ? -1.0f : 1.0f;
            float num = (rxSq * rySq) - (rxSq * y1pSq) - (rySq * x1pSq);
            float den = (rxSq * y1pSq) + (rySq * x1pSq);
            float sq = den <= 0.0f ? 0.0f : Math.max(0.0f, num / den);
            float coef = sign * (float) Math.sqrt(sq);

            float cxp = coef * ((rx * y1p) / ry);
            float cyp = coef * (-(ry * x1p) / rx);

            float cx = cosPhi * cxp - sinPhi * cyp + (x1 + x2) * 0.5f;
            float cy = sinPhi * cxp + cosPhi * cyp + (y1 + y2) * 0.5f;

            float ux = (x1p - cxp) / rx;
            float uy = (y1p - cyp) / ry;
            float vx = (-x1p - cxp) / rx;
            float vy = (-y1p - cyp) / ry;

            float theta1 = angleBetween(1.0f, 0.0f, ux, uy);
            float dTheta = angleBetween(ux, uy, vx, vy);

            if (!sweep && dTheta > 0.0f) {
                dTheta -= (float) (2.0 * Math.PI);
            } else if (sweep && dTheta < 0.0f) {
                dTheta += (float) (2.0 * Math.PI);
            }

            int segments = (int) Math.ceil(Math.abs(dTheta) / (Math.PI * 0.5));
            float dSegment = dTheta / segments;
            float halfD = dSegment * 0.5f;
            float alpha = (float) (Math.sin(dSegment) * (Math.sqrt(4.0 + 3.0 * Math.tan(halfD) * Math.tan(halfD)) - 1.0) / 3.0);

            float theta = theta1;
            for (int i = 0; i < segments; i++) {
                float nextTheta = theta + dSegment;
                float cosT = (float) Math.cos(theta);
                float sinT = (float) Math.sin(theta);
                float cosNT = (float) Math.cos(nextTheta);
                float sinNT = (float) Math.sin(nextTheta);

                float ep1x = cosT * rx;
                float ep1y = sinT * ry;
                float ep2x = cosNT * rx;
                float ep2y = sinNT * ry;

                float eq1x = ep1x - alpha * sinT * rx;
                float eq1y = ep1y + alpha * cosT * ry;
                float eq2x = ep2x + alpha * sinNT * rx;
                float eq2y = ep2y - alpha * cosNT * ry;

                float c1x = cosPhi * eq1x - sinPhi * eq1y + cx;
                float c1y = sinPhi * eq1x + cosPhi * eq1y + cy;
                float c2x = cosPhi * eq2x - sinPhi * eq2y + cx;
                float c2y = sinPhi * eq2x + cosPhi * eq2y + cy;
                float ex = (i == segments - 1) ? x2 : (cosPhi * ep2x - sinPhi * ep2y + cx);
                float ey = (i == segments - 1) ? y2 : (sinPhi * ep2x + cosPhi * ep2y + cy);

                emitBezierTo(c1x, c1y, c2x, c2y, ex, ey);
                theta = nextTheta;
            }
        }

        private static float angleBetween(float ux, float uy, float vx, float vy) {
            float dot = ux * vx + uy * vy;
            float len = (float) (Math.sqrt(ux * ux + uy * uy) * Math.sqrt(vx * vx + vy * vy));
            if (len <= 0.0f) return 0.0f;
            float val = Math.clamp(dot / len, -1.0f, 1.0f);
            float angle = (float) Math.acos(val);
            return (ux * vy - uy * vx < 0.0f) ? -angle : angle;
        }

        private boolean isCommand(char c) {
            return switch (c) {
                case 'M', 'm', 'L', 'l', 'H', 'h', 'V', 'v',
                     'C', 'c', 'S', 's', 'Q', 'q', 'T', 't',
                     'A', 'a', 'Z', 'z' -> true;
                default -> false;
            };
        }

        private void skipWhitespaceAndSeparators() {
            while (pos < len) {
                char c = s.charAt(pos);
                if (c <= ' ' || c == ',') {
                    pos++;
                } else {
                    break;
                }
            }
        }

        private float nextFlag() {
            skipWhitespaceAndSeparators();
            if (pos >= len) throw new IllegalArgumentException("Expected flag at end of path");
            char c = s.charAt(pos++);
            if (c == '0') return 0.0f;
            if (c == '1') return 1.0f;
            throw new IllegalArgumentException("Invalid arc flag: " + c + " at index " + (pos - 1));
        }

        private float nextNumber() {
            skipWhitespaceAndSeparators();
            if (pos >= len) {
                throw new IllegalArgumentException("Expected number at end of path string");
            }

            int start = pos;
            char first = s.charAt(pos);
            if (first == '+' || first == '-') {
                pos++;
            }

            boolean hasDigits = false;
            while (pos < len && Character.isDigit(s.charAt(pos))) {
                hasDigits = true;
                pos++;
            }

            if (pos < len && s.charAt(pos) == '.') {
                pos++;
                while (pos < len && Character.isDigit(s.charAt(pos))) {
                    hasDigits = true;
                    pos++;
                }
            }

            if (!hasDigits) {
                throw new IllegalArgumentException("Invalid number at index " + start + " in \"" + s + "\"");
            }

            if (pos < len && (s.charAt(pos) == 'e' || s.charAt(pos) == 'E')) {
                int expStart = pos++;
                if (pos < len && (s.charAt(pos) == '+' || s.charAt(pos) == '-')) {
                    pos++;
                }
                boolean hasExpDigits = false;
                while (pos < len && Character.isDigit(s.charAt(pos))) {
                    hasExpDigits = true;
                    pos++;
                }
                if (!hasExpDigits) {
                    pos = expStart; // rollback exponent
                }
            }

            try {
                return Float.parseFloat(s.substring(start, pos));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Malformed number at index " + start, e);
            }
        }
    }
}
