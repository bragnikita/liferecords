package jp.bragnikita.liferecords.backend.dropfiles;

import jp.bragnikita.liferecords.backend.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class DropzoneWatcherTest {

    private DropzoneWatcher watcher;
    private Path dropZone;

    private Path droppedFile;

    @BeforeEach
    void setUp() throws IOException {
        this.dropZone = Path.of("tmp/dropzone").toAbsolutePath();
        Files.createDirectories(this.dropZone);
        watcher = new DropzoneWatcher(this.dropZone, pathToFile -> {
            droppedFile = pathToFile;
        });
    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of("tmp/dropzone"));
    }

    @Test
    void startWatching() throws IOException, InterruptedException {
        watcher.startWatching();
        Thread.sleep(1000);
        Files.copy(
                TestUtils.getFileFromResources("exif_tags_phone_photo.jpg").toPath(),
                dropZone.resolve( "target.jpg")
        );
        Thread.sleep(10000);
        Assert.notNull(this.droppedFile, "");
    }
}