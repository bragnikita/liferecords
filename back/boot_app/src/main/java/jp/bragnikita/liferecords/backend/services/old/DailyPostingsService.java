package jp.bragnikita.liferecords.backend.services.old;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.bragnikita.liferecords.backend.postings.IndexModel;
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

    private Path uploadsRoot;

    public DailyPostingsService(@Value("${app.timeline.storage.root}") String uploadsRoot) {
        this.uploadsRoot = Path.of(uploadsRoot).toAbsolutePath();
    }

    public DailyFolderControl getDailyPosting(String dayId) throws IOException {
        Path pathToFolder = uploadsRoot.resolve(dayId);
        if (!pathToFolder.toFile().exists()) {
            pathToFolder = Files.createDirectories(pathToFolder);
        }
        return new DailyFolderImpl(pathToFolder);
    }

    public List<String> listMonthDays(Integer year, Integer month) throws IOException {
        Path dir = uploadsRoot.resolve(String.format("%d_%02d", year, month));
        if (!dir.toFile().exists()) return List.of();
        File[] dayDirs = dir.toFile().listFiles((dir1, name) -> {
            String[] dayliDirContent =  dir1.toPath().resolve(name).toFile().list();
            return dayliDirContent != null && dayliDirContent.length > 0;
        });
        if (dayDirs != null) {
            return Arrays.stream(dayDirs).map(File::getName).collect(Collectors.toList());
        }
        return List.of();
    }


    private class DailyFolderImpl implements DailyFolderControl {

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
                    return !pathToFolder.relativize(pathname.toPath()).toString().equals("index.json");
                }
            });
            if (files != null) {
                return Arrays.stream(files).map(File::toPath)
                        .map(pathToFolder::resolve)
                        .map(s -> uploadsRoot.relativize(s).toString())
                        .collect(Collectors.toList());
            }
            return List.of();
        }

        @Override
        public IndexModel getIndex() {
            IndexModel m;
            File f = pathToFolder.resolve("index.json").toFile();
            if (f.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    m = mapper.readValue(f, IndexModel.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                m = new IndexModel();
            }

            return m;
        }

        @Override
        public void deleteAttachment(String id) {
            Path pathToFile = pathToFolder.resolve(id).normalize();
            if (!pathToFile.startsWith(pathToFolder)) {
                throw new RuntimeException("Wrong file id " + id);
            }
            if (pathToFile.toFile().exists()) {
                boolean result = pathToFile.toFile().delete();
                if (!result) {
                    throw new RuntimeException("Could not delete file " + id );
                }
            }
        }

    }
}
