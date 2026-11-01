# catovg

A lightweight, zero allocation 2d rendering library wrapping NanoVG for java 21+. Used by Starware.

## features

- Fluent `VGCanvas` wrapper around raw NanoVG context handles.
- Stateless `VGDraw` primitives (rectangles, rounded rectangles, outlines, circles, arcs, text, drop shadows, glows).
- Immutable `VGColor` record with rgb, rgba, argb, and hex parsing utilities.
- Gradient and image pattern generation with `VGPaint`.
- Zero per frame heap allocations using thread local ring buffers for native structs.

## building

requires jdk 21+.

```bash
git clone https://github.com/g-a-l-a-x-i-a/catovg.git
cd catovg
./gradlew build
```
