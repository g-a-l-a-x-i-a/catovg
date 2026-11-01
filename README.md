# catovg

a lightweight 2d rendering library wrapping nanovg for java 21+, aimed at immediate mode UI. used by starware.

## features

- fluent `vgcanvas` wrapper around raw nanovg context handles with immediate mode chaining, path construction, and auto-closing scopes (`scope()`, `scoped(...)`, `scissored(...)`).
- stateless `vgdraw` primitives (rectangles, rounded rectangles, varying rounded rectangles with outlines, pills, circles, ellipses, triangles, lines, arcs, beziers, quadratic curves, polygons, svg paths).
- multi-stop linear gradients and radial gradients (`vggradient`), conical / angular gradient circles and rings, drop shadows with custom xy offsets, and inner / inset shadows.
- svg vector path parser (`vgpath`) with support for m, l, h, v, c, s, q, t, a, z commands, including elliptical arcs converted to cubic beziers.
- rich text and typography: single line, multiline (`textmultiline`), wrapped text (`textbox`), shadow text (`textwithshadow`), outline text (`textwithoutline`), and full vertical font metrics (`textmetrics`, `textascent`, `textdescent`, `fontlineheight`, `textheight`).
- safe font loading & handle management (`vgfont`) from a file path, classpath resource, byte array or `bytebuffer`, with off-heap copies pinned for nanovg's lifetime.
- gpu image loading, deletion, and caching (`vgimage`, `vgimagecache`, `vgimageflags`).
- stroke styling with line caps (`vglinecap`) and line joins (`vglinejoin`).
- global alpha and blend modes (`vgcompositeop`, `vgblendfactor`).
- immutable `vgcolor` record with rgb, rgba, argb, hex, hsb, and hsl parsing and interpolation (`lerp`, `mix`).
- gradient and image pattern generation with `vgpaint`.
- native struct reuse through per thread ring buffers, so drawing does not allocate a fresh `nvgcolor` / `nvgpaint` per call. measurement helpers (`bounds`, `textmetrics`) accept caller-supplied out arrays for the same reason.

## building

requires jdk 21+.

```bash
git clone https://github.com/g-a-l-a-x-i-a/catovg.git
cd catovg
./gradlew build
```
