package space.rawrclient.catovg.image;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe cache for {@link VGImage} textures to prevent redundant decoding and texture uploads.
 */
public final class VGImageCache {

    private final Map<String, VGImage> cache = new ConcurrentHashMap<>();

    /**
     * Retrieves an image from the cache or loads it from disk if not yet present.
     *
     * @param vg    NanoVG context handle
     * @param path  file path
     * @param flags image creation flags
     * @return the cached or newly loaded image
     */
    public VGImage getOrLoad(long vg, String path, int flags) {
        Objects.requireNonNull(path, "path");
        String key = "file:" + path + ":" + flags;
        return cache.computeIfAbsent(key, k -> VGImage.load(vg, path, flags));
    }

    /**
     * Retrieves an image from the cache or loads it from disk with default flags (0).
     *
     * @param vg   NanoVG context handle
     * @param path file path
     * @return the cached or newly loaded image
     */
    public VGImage getOrLoad(long vg, String path) {
        return getOrLoad(vg, path, 0);
    }

    /**
     * Retrieves an image from the cache or loads it from a classpath resource if not yet present.
     *
     * @param vg           NanoVG context handle
     * @param resourcePath classpath path
     * @param flags        image creation flags
     * @return the cached or newly loaded image
     */
    public VGImage getOrLoadResource(long vg, String resourcePath, int flags) {
        Objects.requireNonNull(resourcePath, "resourcePath");
        String key = "res:" + resourcePath + ":" + flags;
        return cache.computeIfAbsent(key, k -> VGImage.loadResource(vg, resourcePath, flags));
    }

    /**
     * Retrieves an image from the cache or loads it from a classpath resource with default flags (0).
     *
     * @param vg           NanoVG context handle
     * @param resourcePath classpath path
     * @return the cached or newly loaded image
     */
    public VGImage getOrLoadResource(long vg, String resourcePath) {
        return getOrLoadResource(vg, resourcePath, 0);
    }

    /**
     * Evicts an image by key and deletes its native GPU texture handle.
     *
     * @param key cache key
     * @return {@code true} if an entry was removed
     */
    public boolean evict(String key) {
        Objects.requireNonNull(key, "key");
        VGImage image = cache.remove(key);
        if (image != null) {
            image.delete();
            return true;
        }
        return false;
    }

    /**
     * Deletes all cached textures on the GPU and clears the cache.
     */
    public void clear() {
        for (VGImage image : cache.values()) {
            image.delete();
        }
        cache.clear();
    }

    /**
     * Returns the current number of cached images.
     *
     * @return size of the cache
     */
    public int size() {
        return cache.size();
    }
}
