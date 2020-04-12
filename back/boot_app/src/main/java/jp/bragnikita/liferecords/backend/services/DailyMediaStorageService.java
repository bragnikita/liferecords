package jp.bragnikita.liferecords.backend.services;

import jp.bragnikita.liferecords.backend.dropfiles.CopyToDiaryStorageTask;
import jp.bragnikita.liferecords.backend.dropfiles.FileWatcherEventCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class DailyMediaStorageService implements FileWatcherEventCallback {

    private Path storageRootPath;

    public DailyMediaStorageService(@Value("${app.timeline.storage.root}") String storageRoot) {
        this.storageRootPath = Path.of(storageRoot).toAbsolutePath();
    }

    @Override
    public void onFileCreated(Path pathToFile) {
        CopyToDiaryStorageTask task = new CopyToDiaryStorageTask(this.storageRootPath);
        try {
            String resultFile = task.copy(pathToFile.toFile());
            logFile(pathToFile, resultFile);
        } catch (Exception ioe) {
            System.err.printf("Copy failure: (%s) -> ? | %s", pathToFile.getFileName(), ioe.getMessage());
            ioe.printStackTrace(System.err);
        }
    }

    private void logFile(Path sourceFile, String resultFile) {
        System.out.printf("Copy (%s) -> (%s)\n", sourceFile.getFileName(), resultFile);
    }

}
