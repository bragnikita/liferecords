package jp.bragnikita.liferecords.backend.dropfiles;

import com.drew.imaging.ImageProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CopyToDiaryStorageTaskTest {

    CopyToDiaryStorageTask task;
    Path storage;

    @BeforeEach
    void setUp() throws IOException {
        this.storage = Files.createDirectories(Path.of("tmp/storage"));
        task = new CopyToDiaryStorageTask(this.storage);
    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of("tmp/storage"));
    }

    @Test
    void copy() throws IOException {
        String pathToFile = this.task.copy(getFileFromResources("exif_tags_phone_photo.jpg"));
        assertTrue(Path.of(pathToFile).toFile().exists());
    }

    @Test
    void printMetadata() throws IOException, ImageProcessingException {
        CopyToDiaryStorageTask.printMetadata(getFileFromResources("exif_tags_phone_photo.jpg"));
    }

    File getFileFromResources(String resource) throws FileNotFoundException {
        URL url = this.getClass().getClassLoader().getResource(resource);
        if (url == null) {
            throw new FileNotFoundException(resource);
        }
        try {
            return ResourceUtils.getFile(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}