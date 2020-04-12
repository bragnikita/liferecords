package jp.bragnikita.liferecords.backend.services;

import jp.bragnikita.liferecords.backend.dropfiles.DropzoneWatcher;
import jp.bragnikita.liferecords.backend.dropfiles.FileWatcherEventCallback;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class DropzoneServer implements ApplicationRunner, DisposableBean {


    private String dropZoneLocation;

    private DropzoneWatcher watcher;

    private final FileWatcherEventCallback callback;

    public DropzoneServer(@Value(value = "${app.timeline.dropzone.path}") String dropZoneLocation,
                          @Autowired FileWatcherEventCallback callback) {
        this.dropZoneLocation = dropZoneLocation;
        this.callback = callback;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (this.dropZoneLocation == null) {
            return;
        }
        this.watcher = new DropzoneWatcher(Path.of(dropZoneLocation).toAbsolutePath(), this.callback);
        this.watcher.startWatching();
    }

    @Override
    public void destroy() throws Exception {
        if (this.watcher != null) {
            this.watcher.stop();
        }
    }
}
