package jp.bragnikita.liferecords.backend.dropfiles;

import org.springframework.util.Assert;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;

public class DropzoneWatcher {

    private Path dropzoneDir;
    private FilenameFilter filenameFilter;
    private FileWatcherEventCallback callback;
    private WatchService currentService;

    public DropzoneWatcher(Path dropzoneDir, FileWatcherEventCallback callback) {
        Assert.notNull(dropzoneDir, "Dropzone directory is required");
        Assert.notNull(callback, "Callback is required");
        this.dropzoneDir = dropzoneDir;
        this.callback = callback;
        this.filenameFilter = (dir, name) -> !name.startsWith(".");
    }

    public void setFilenameFilter(FilenameFilter filenameFilter) {
        this.filenameFilter = filenameFilter;
    }

    public void startWatching() throws IOException {
            this.stop();

            ensureDropzone();

            WatchService watchService = FileSystems.getDefault().newWatchService();
            this.dropzoneDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            this.currentService = watchService;

            execInThread();
    }

    public void stop() {
        if (this.currentService != null) {
            try {
                this.currentService.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void execInThread() {
        Thread t = new Thread(() -> {
            try {
                exec();
            } catch (InterruptedException ignore) {
            } catch (Exception e) {
                this.stop();
            }
        });
        t.setName("dropzone-watcher");
        t.start();
    }

    private void exec() throws InterruptedException {
        WatchKey key;
        while ((key = this.currentService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                Path fileRelPath = (Path) event.context();
                Path fileFullPath = this.dropzoneDir.toAbsolutePath().resolve(fileRelPath);
                if (fileFullPath.toFile().isFile()) {
                    if (this.filenameFilter.accept(
                            fileFullPath.getParent().toFile(),
                            fileFullPath.getFileName().toString())
                    ) {
                        this.callback.onFileCreated(fileFullPath);
                    }
                }
            }
            key.reset();
        }
    }

    private void ensureDropzone() throws IOException {
        File d = this.dropzoneDir.toFile();
        if (!d.exists()) {
            Files.createDirectories(d.toPath());
        }
    }
}
