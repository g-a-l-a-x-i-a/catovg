package space.rawrclient.catovg.image;

import org.lwjgl.nanovg.NanoVG;
import space.rawrclient.catovg.paint.VGPaint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Handle to a GPU texture image allocated in NanoVG.
 * Implements {@link AutoCloseable} to enable deterministic cleanup via {@link #close()}.
 *
 * @param vg     NanoVG context handle
 * @param handle native image handle
 * @param width  image width in pixels
 * @param height image height in pixels
 */
public record VGImage(long vg, int handle, int width, int height) implements AutoCloseable {

    private static final ThreadLocal<int[]> SCRATCH_DIM = ThreadLocal.withInitial(() -> new int[2]);

    /**
     * Constructs a {@code VGImage} record and ensures the handle is positive.
     *
     * @param vg     context handle
     * @param handle native image handle
     * @param width  width
     * @param height height
     * @throws IllegalArgumentException if {@code handle <= 0}
     */
    public VGImage {
        if (handle <= 0) {
            throw new IllegalArgumentException("Invalid image handle: " + handle);
        }
    }

    /**
     * Loads an image from an on-disk file path with the specified flags.
     *
     * @param vg       NanoVG context handle
     * @param filePath path to the image file
     * @param flags    image creation flags from {@link VGImageFlags}
     * @return the loaded image
     * @throws IllegalArgumentException if loading fails
     */
    public static VGImage load(long vg, String filePath, int flags) {
        Objects.requireNonNull(filePath, "filePath");
        int handle = NanoVG.nvgCreateImage(vg, filePath, flags);
        if (handle <= 0) {
            throw new IllegalArgumentException("Failed to load image from path: " + filePath);
        }
        int[] size = queryDimensions(vg, handle);
        return new VGImage(vg, handle, size[0], size[1]);
    }

    /**
     * Loads an image from an on-disk file path with default flags (0).
     *
     * @param vg       NanoVG context handle
     * @param filePath path to the image file
     * @return the loaded image
     */
    public static VGImage load(long vg, String filePath) {
        return load(vg, filePath, 0);
    }

    /**
     * Loads an image from an input stream with the specified flags.
     *
     * @param vg     NanoVG context handle
     * @param stream input stream containing encoded image bytes (PNG, JPEG, etc.)
     * @param flags  image creation flags
     * @return the loaded image
     * @throws IllegalArgumentException if reading or decoding fails
     */
    public static VGImage load(long vg, InputStream stream, int flags) {
        Objects.requireNonNull(stream, "stream");
        try {
            byte[] bytes = stream.readAllBytes();
            return loadMem(vg, bytes, flags);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read image stream", e);
        }
    }

    /**
     * Loads an image from an input stream with default flags (0).
     *
     * @param vg     NanoVG context handle
     * @param stream input stream containing encoded image bytes
     * @return the loaded image
     */
    public static VGImage load(long vg, InputStream stream) {
        return load(vg, stream, 0);
    }

    /**
     * Loads an image from a classpath resource with the specified flags.
     *
     * @param vg           NanoVG context handle
     * @param resourcePath classpath resource path
     * @param flags        image creation flags
     * @return the loaded image
     * @throws IllegalArgumentException if the resource cannot be found or read
     */
    public static VGImage loadResource(long vg, String resourcePath, int flags) {
        Objects.requireNonNull(resourcePath, "resourcePath");
        InputStream is = VGImage.class.getResourceAsStream(resourcePath);
        if (is == null) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                is = cl.getResourceAsStream(resourcePath);
            }
        }
        if (is == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }
        try (InputStream stream = is) {
            return load(vg, stream, flags);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to close image resource stream: " + resourcePath, e);
        }
    }

    /**
     * Loads an image from a classpath resource with default flags (0).
     *
     * @param vg           NanoVG context handle
     * @param resourcePath classpath resource path
     * @return the loaded image
     */
    public static VGImage loadResource(long vg, String resourcePath) {
        return loadResource(vg, resourcePath, 0);
    }

    /**
     * Decodes and loads an image from an off-heap or direct byte buffer.
     *
     * @param vg     NanoVG context handle
     * @param buffer byte buffer holding encoded image data
     * @param flags  image creation flags
     * @return the loaded image
     * @throws IllegalArgumentException if decoding fails
     */
    public static VGImage loadMem(long vg, ByteBuffer buffer, int flags) {
        Objects.requireNonNull(buffer, "buffer");
        ByteBuffer direct;
        if (buffer.isDirect()) {
            direct = buffer;
        } else {
            direct = ByteBuffer.allocateDirect(buffer.remaining());
            int pos = buffer.position();
            direct.put(buffer);
            buffer.position(pos);
            direct.flip();
        }
        int handle = NanoVG.nvgCreateImageMem(vg, flags, direct);
        if (handle <= 0) {
            throw new IllegalArgumentException("Failed to load image from memory buffer");
        }
        int[] size = queryDimensions(vg, handle);
        return new VGImage(vg, handle, size[0], size[1]);
    }

    /**
     * Decodes and loads an image from a raw byte array.
     *
     * @param vg    NanoVG context handle
     * @param bytes raw encoded image bytes
     * @param flags image creation flags
     * @return the loaded image
     * @throws IllegalArgumentException if decoding fails
     */
    public static VGImage loadMem(long vg, byte[] bytes, int flags) {
        Objects.requireNonNull(bytes, "bytes");
        ByteBuffer direct = ByteBuffer.allocateDirect(bytes.length);
        direct.put(bytes);
        direct.flip();
        int handle = NanoVG.nvgCreateImageMem(vg, flags, direct);
        if (handle <= 0) {
            throw new IllegalArgumentException("Failed to load image from byte array");
        }
        int[] size = queryDimensions(vg, handle);
        return new VGImage(vg, handle, size[0], size[1]);
    }

    /**
     * Decodes and loads an image from a raw byte array with default flags (0).
     *
     * @param vg    NanoVG context handle
     * @param bytes raw encoded image bytes
     * @return the loaded image
     */
    public static VGImage loadMem(long vg, byte[] bytes) {
        return loadMem(vg, bytes, 0);
    }

    /**
     * Creates an image from raw 32-bit RGBA pixel memory.
     *
     * @param vg     NanoVG context handle
     * @param width  pixel width
     * @param height pixel height
     * @param flags  image creation flags
     * @param pixels byte buffer holding width * height * 4 RGBA bytes
     * @return the created image
     * @throws IllegalArgumentException if image creation fails
     */
    public static VGImage createRGBA(long vg, int width, int height, int flags, ByteBuffer pixels) {
        Objects.requireNonNull(pixels, "pixels");
        ByteBuffer direct;
        if (pixels.isDirect()) {
            direct = pixels;
        } else {
            direct = ByteBuffer.allocateDirect(pixels.remaining());
            int pos = pixels.position();
            direct.put(pixels);
            pixels.position(pos);
            direct.flip();
        }
        int handle = NanoVG.nvgCreateImageRGBA(vg, width, height, flags, direct);
        if (handle <= 0) {
            throw new IllegalArgumentException("Failed to create RGBA image: " + width + "x" + height);
        }
        return new VGImage(vg, handle, width, height);
    }

    private static int[] queryDimensions(long vg, int handle) {
        int[] dim = SCRATCH_DIM.get();
        int[] w = new int[1];
        int[] h = new int[1];
        NanoVG.nvgImageSize(vg, handle, w, h);
        dim[0] = w[0];
        dim[1] = h[0];
        return dim;
    }

    /**
     * Returns true if this image has a valid positive handle.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return handle > 0;
    }

    /**
     * Creates an image pattern paint using this image texture.
     *
     * @param ox    pattern origin x
     * @param oy    pattern origin y
     * @param w     tile width
     * @param h     tile height
     * @param angle rotation angle in radians
     * @param alpha opacity in [0, 1]
     * @return the paint
     */
    public VGPaint pattern(float ox, float oy, float w, float h, float angle, float alpha) {
        return VGPaint.imagePattern(vg, ox, oy, w, h, angle, handle, alpha);
    }

    /**
     * Creates an unrotated, opaque image pattern paint at the origin with the given dimensions.
     *
     * @param w tile width
     * @param h tile height
     * @return the paint
     */
    public VGPaint pattern(float w, float h) {
        return pattern(0.0f, 0.0f, w, h, 0.0f, 1.0f);
    }

    /**
     * Creates an unrotated, opaque image pattern paint matching the native dimensions.
     *
     * @return the paint
     */
    public VGPaint pattern() {
        return pattern(0.0f, 0.0f, width, height, 0.0f, 1.0f);
    }

    /**
     * Deletes the underlying texture on the GPU.
     */
    public void delete() {
        if (handle > 0 && vg != 0) {
            NanoVG.nvgDeleteImage(vg, handle);
        }
    }

    /**
     * Alias for {@link #delete()} allowing try-with-resources.
     */
    @Override
    public void close() {
        delete();
    }
}
