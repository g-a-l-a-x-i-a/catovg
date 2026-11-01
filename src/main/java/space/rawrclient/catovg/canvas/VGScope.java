package space.rawrclient.catovg.canvas;

/**
 * An auto-closeable scope for canvas state restoration.
 * <p>
 * Calling {@link #close()} restores the canvas state saved when this scope was created.
 * Does not throw checked exceptions so it can be cleanly used in try-with-resources.
 */
@FunctionalInterface
public interface VGScope extends AutoCloseable {

    /**
     * Restores the canvas state.
     */
    @Override
    void close();
}
