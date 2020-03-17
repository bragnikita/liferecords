package jp.bragnikita.liferecords.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyPostingsService {

    @Value("${app.timeline.storage.root}")
    private String uploadsRoot;

    public DailyFolderControl getDailyPosting(String dayId) throws IOException {
        Path pathToFolder = Path.of(uploadsRoot, dayId);
        if (!pathToFolder.toFile().exists()) {
            pathToFolder = Files.createDirectories(pathToFolder);
        }
        return new DailyFolderImpl(pathToFolder);
    }


    private static class DailyFolderImpl implements DailyFolderControl {

        private final Path pathToFolder;

        public DailyFolderImpl(Path pathToFolder) {
            this.pathToFolder = pathToFolder;
        }

        @Override
        public String attachFile(String filename, InputStream dataStream) throws IOException {
            File target = this.pathToFolder.resolve(filename).toFile();
            if (target.exists()) {
                throw new RuntimeException("Exists");
            }
            Files.copy(dataStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return filename;
        }

        @Override
        public List<String> getAttachments() {
            File[] files = pathToFolder.toFile().listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return !pathname.toPath().toString().equals("index.json");
                }
            });
            if (files != null) {
                return Arrays.stream(files).map(File::getPath).collect(Collectors.toList());
            }
            return List.of();
        }
    }
}
