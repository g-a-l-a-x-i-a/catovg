# catovg

a lightweight, zero allocation 2d rendering library wrapping nanovg for java 21+. used by starware.

## features

- fluent `vgcanvas` wrapper around raw nanovg context handles with immediate-mode chaining and path construction.
- stateless `vgdraw` primitives (rectangles, rounded rectangles, circles, lines, arcs, beziers, quadratic curves, polygons, drop shadows, glows).
- multiline & wrapped text (`textbox`, `textboxbounds`, `textboxheight`) with zero-allocation measurement via thread-local scratch buffers.
- safe font loading & handle management (`vgfont`) with automatic off-heap memory pinning.
- gpu image loading, deletion, and caching (`vgimage`, `vgimagecache`, `vgimageflags`).
- stroke styling with line caps (`vglinecap`) and line joins (`vglinejoin`).
- global alpha and blend modes (`vgcompositeop`, `vgblendfactor`).
- immutable `vgcolor` record with rgb, rgba, argb, and hex parsing utilities.
- gradient and image pattern generation with `vgpaint`.
- zero per frame heap allocations using thread local ring buffers for native structs.

## building

requires jdk 21+.

```bash
git clone https://github.com/g-a-l-a-x-i-a/catovg.git
cd catovg
./gradlew build
```
