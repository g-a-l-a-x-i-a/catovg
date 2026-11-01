package space.rawrclient.catovg.image;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VGImageCacheTest {

    @Test
    public void testCacheLifecycle() {
        VGImageCache cache = new VGImageCache();
        assertEquals(0, cache.size());

        assertFalse(cache.evict("non-existent"));

        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    public void testNullKeyHandling() {
        VGImageCache cache = new VGImageCache();
        assertThrows(NullPointerException.class, () -> cache.evict(null));
        assertThrows(NullPointerException.class, () -> cache.getOrLoad(0, null));
        assertThrows(NullPointerException.class, () -> cache.getOrLoadResource(0, null));
    }
}
