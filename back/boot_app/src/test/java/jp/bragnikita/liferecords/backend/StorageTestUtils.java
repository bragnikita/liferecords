package jp.bragnikita.liferecords.backend;

import jp.bragnikita.liferecords.backend.services.DayKey;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class StorageTestUtils {

    public static class FsStorage {

        Path root;
        Path copySource;

        private FsStorage(Path root) {
            this.root = root;
        }

        public FsStorage reset() throws IOException {
            if (!root.toFile().exists()) {
                Files.createDirectories(root);
            } else {
                Files.list(this.root).map(Path::toFile).forEach(FileUtils::deleteQuietly);
            }
            return this;
        }

        public FsStorage linkCopySource(Path src) throws IOException {
            this.copySource =src;
            return this;
        }
        
        public FsDay forDay(DayKey key) throws IOException {
            return forDay(key.getMonthFolderName(), key.getDay());
        }

        public FsDay forDay(String yyyy_mm, String day) throws IOException {
            Path path = Files.createDirectories(root.resolve(Path.of(yyyy_mm, day)));
            return new FsDay(path);
        }

        public class FsDay {
            Path path;

            public FsDay(Path path) {
                this.path = path;
            }

            public FsStorage toStorage() {
                return FsStorage.this;
            }

            public FsDay put(String name) throws IOException {
                byte[] b = new byte[100];
                Arrays.fill(b, (byte) 0);
                Files.copy(new ByteArrayInputStream(b), path.resolve(name));
                return this;
            }

            public FsDay put(String name, File file) throws IOException {
                Files.copy(file.toPath(), path.resolve(name));
                return this;
            }

            public FsDay put(String name, String srcName) throws IOException {
                Assert.notNull(FsStorage.this.copySource, "");
                Files.copy(FsStorage.this.copySource.resolve(srcName), path.resolve(name));
                return this;
            }

            public boolean hasFile(String filename) {
                return path.resolve(filename).toFile().exists();
            }
        }

    }

    public static FsStorage createStorage(String rootDir) {
        return new FsStorage(Path.of(rootDir).toAbsolutePath());
    }


}
