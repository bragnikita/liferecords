package jp.bragnikita.liferecords.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages the file-based storage.
 * Format:
 * <pre>
 * - [root]
 *   - 2020_01
 *     - 02
 *       - 001.json
 *       - 002.json
 *       - photo1.jpg
 *     - 12
 * </pre>
 */
@Service
@Scope("singleton")
public class StorageServiceImpl implements StorageService {

    private Path root;

    public StorageServiceImpl(@Value("app.timeline.storage.root") String rootDir) {
        root = Path.of(rootDir);
        if (!root.isAbsolute()) {
            root = root.toAbsolutePath();
        }
    }

    @PostConstruct
    public void init() {
        final File f = root.toFile();
        if (!f.exists()) {
            createDir(f.toPath());
        } else {
            if (!f.isDirectory()) {
                throw new IllegalArgumentException(String.format("'%s' is not directory", f.toString()));
            }
        }
    }

    @Override
    public DayStorageService getDayStorageService(String day) {
        final StorageDayId key = DayKey.fromParameter(day);
        final Path dayPath = root.resolve(Path.of(key.getStorageDayPath()));
        final File f = dayPath.toFile();
        if (!f.exists()) {
            createDir(dayPath);
        }
        return new DayStorageServiceImpl(dayPath, key.getStorageDayId());
    }

    private static void createDir(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException ioe) {
            throw new StorageException(
                    String.format("Could not create directory %s", path.toString()),
                    ioe
            );
        }
    }
}
