package jp.bragnikita.liferecords.backend.dropfiles;

import java.nio.file.Path;

public interface FileWatcherEventCallback {

    void onFileCreated(Path pathToFile);

}
