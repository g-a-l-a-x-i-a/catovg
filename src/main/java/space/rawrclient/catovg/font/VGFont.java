package space.rawrclient.catovg.font;

import org.lwjgl.nanovg.NanoVG;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a registered font face in NanoVG with automatic off-heap memory pinning.
 *
 * @param handle native font handle returned by NanoVG
 * @param name   registered font face name
 */
public record VGFont(int handle, String name) {

    private static final Map<String, ByteBuffer> PINNED_BUFFERS = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code VGFont} record and validates that the handle is non-negative.
     *
     * @param handle native font handle
     * @param name   registered font face name
     * @throws IllegalArgumentException if {@code handle < 0}
     * @throws NullPointerException     if {@code name} is null
     */
    public VGFont {
        Objects.requireNonNull(name, "name");
        if (handle < 0) {
            throw new IllegalArgumentException("Invalid font handle: " + handle);
        }
    }

    /**
     * Loads a font face from an on-disk file path.
     *
     * @param vg       NanoVG context handle
     * @param name     font face name to register
     * @param filePath path to the TrueType or OpenType font file
     * @return the loaded font
     * @throws IllegalArgumentException if NanoVG fails to load the font
     */
    public static VGFont load(long vg, String name, String filePath) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(filePath, "filePath");
        int handle = NanoVG.nvgCreateFont(vg, name, filePath);
        if (handle == -1) {
            throw new IllegalArgumentException("Failed to load font '" + name + "' from: " + filePath);
        }
        return new VGFont(handle, name);
    }

    /**
     * Loads a font face from an input stream.
     *
     * @param vg     NanoVG context handle
     * @param name   font face name to register
     * @param stream input stream delivering font data bytes
     * @return the loaded font
     * @throws IllegalArgumentException if reading the stream fails or NanoVG rejects the font
     */
    public static VGFont load(long vg, String name, InputStream stream) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(stream, "stream");
        try {
            byte[] bytes = stream.readAllBytes();
            return loadMem(vg, name, bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read font stream for: " + name, e);
        }
    }

    /**
     * Loads a font face from a classpath resource.
     *
     * @param vg           NanoVG context handle
     * @param name         font face name to register
     * @param resourcePath classpath path to the font resource
     * @return the loaded font
     * @throws IllegalArgumentException if the resource is not found or fails to load
     */
    public static VGFont loadResource(long vg, String name, String resourcePath) {
        Objects.requireNonNull(resourcePath, "resourcePath");
        InputStream is = VGFont.class.getResourceAsStream(resourcePath);
        if (is == null) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                is = cl.getResourceAsStream(resourcePath);
            }
        }
        if (is == null) {
            throw new IllegalArgumentException("Font resource not found: " + resourcePath);
        }
        try (InputStream stream = is) {
            return load(vg, name, stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to close font stream for: " + name, e);
        }
    }

    /**
     * Loads a font face from off-heap memory.
     *
     * @param vg     NanoVG context handle
     * @param name   font face name to register
     * @param buffer byte buffer holding font data
     * @return the loaded font
     * @throws IllegalArgumentException if NanoVG fails to parse the font
     */
    public static VGFont loadMem(long vg, String name, ByteBuffer buffer) {
        Objects.requireNonNull(name, "name");
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
        PINNED_BUFFERS.put(vg + ":" + name, direct);
        int handle = NanoVG.nvgCreateFontMem(vg, name, direct, false);
        if (handle == -1) {
            PINNED_BUFFERS.remove(vg + ":" + name);
            throw new IllegalArgumentException("Failed to create font in memory for: " + name);
        }
        return new VGFont(handle, name);
    }

    /**
     * Loads a font face from a raw byte array.
     *
     * @param vg    NanoVG context handle
     * @param name  font face name to register
     * @param bytes raw font data
     * @return the loaded font
     * @throws IllegalArgumentException if NanoVG fails to parse the font
     */
    public static VGFont loadMem(long vg, String name, byte[] bytes) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(bytes, "bytes");
        ByteBuffer direct = ByteBuffer.allocateDirect(bytes.length);
        direct.put(bytes);
        direct.flip();
        PINNED_BUFFERS.put(vg + ":" + name, direct);
        int handle = NanoVG.nvgCreateFontMem(vg, name, direct, false);
        if (handle == -1) {
            PINNED_BUFFERS.remove(vg + ":" + name);
            throw new IllegalArgumentException("Failed to create font from byte array for: " + name);
        }
        return new VGFont(handle, name);
    }

    /**
     * Checks if this font has a valid non-negative native handle.
     *
     * @return {@code true} if valid
     */
    public boolean isValid() {
        return handle >= 0;
    }
}
