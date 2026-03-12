package data;

import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    private final Map<Path, WeakReference<FileData>> cache = new HashMap<>();

    public void put(Path path, FileData data) {
        cache.put(path, new WeakReference<>(data));
    }

    public FileData get(Path path) {
        WeakReference<FileData> ref = cache.get(path);
        return ref != null ? ref.get() : null;
    }

    public void clear() {
        cache.clear();
    }
}
