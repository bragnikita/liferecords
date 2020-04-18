package jp.bragnikita.liferecords.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.bragnikita.liferecords.backend.postings.Posting;
import jp.bragnikita.liferecords.backend.postings.StorageResource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.lang.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;

import static jp.bragnikita.liferecords.backend.services.StorageUtils.*;

public class DayStorageServiceImpl implements DayStorageService {

    private Path dayFolder;
    private String day;

    public DayStorageServiceImpl(Path dayFolder, String day) {
        this.dayFolder = dayFolder;
        this.day = day;
    }

    @Override
    public Posting[] listPostings() {
        this.ensureFolderExists();
        return Arrays.stream(list(dayFolder,
                filterByExtension("json"))).map(this::readPosting)
                .toArray(Posting[]::new);
    }

    @Override
    public StorageResource[] listResources() {
        this.ensureFolderExists();
        return Arrays.stream(list(dayFolder, reverseFilter(filterByExtension("json"))))
                .map(this::readResource).toArray(StorageResource[]::new);
    }

    @Override
    public Posting savePosting(Posting posting, @Nullable String id) {
        this.ensureFolderExists();
        if (id == null) {
            posting.setCreatedAt(Instant.now().toEpochMilli());
            posting.setReferenceDate(day);

            String nextPostingId = lookForNextPostingId(dayFolder);
            String postId = String.format("%s_%s", day, nextPostingId);
            posting.setId(postId);

            saveJson(posting, nextPostingId);
        } else {
            String postId = id.split("_")[1];
            saveJson(posting, postId);
        }
        return posting;
    }

    @Override
    public StorageResource saveResource(InputStream data, String originalFileName) {
        this.ensureFolderExists();
        String newFileName = lookForUniqueName(this.dayFolder, originalFileName);
        Path targetPath = this.dayFolder.resolve(newFileName);
        try {
            Files.copy(data, targetPath);
        } catch (IOException e) {
            throw new StorageException("Can not save file %s", targetPath.toString(), e);
        }
        return readResource(newFileName);
    }

    private String saveJson(Object post, String filename) {
        if (!FilenameUtils.isExtension(filename, "json")) {
            filename = filename + ".json";
        }
        final File outputFile = dayFolder.resolve(filename).toFile();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(outputFile, post);
        } catch (IOException e) {
            throw new StorageException(String.format("Could not write post in %s", outputFile.toString()), e);
        }
        return filename;
    }

    private Posting readPosting(String filename) {
        final File f = dayFolder.resolve(filename).toFile();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(f, Posting.class);
        } catch (IOException ioe) {
            throw new StorageException(String.format("Can not read post %s", f.toString()), ioe);
        }
    }

    private void ensureFolderExists() {
        if (!this.dayFolder.toFile().exists()) {
            try {
                Files.createDirectories(this.dayFolder);
            } catch (IOException e) {
                throw new StorageException("Can not create day folder", this.dayFolder.toString(), e);
            }
        }
    }

    private StorageResource readResource(String filaname) {
        final File f = dayFolder.resolve(filaname).toFile();
        return new StorageResource() {

            @Override
            public InputStream getStream() {
                try {
                    return new FileInputStream(f);
                } catch (IOException ioe) {
                    throw new StorageException(String.format("Can not start reading file %s", f.toString()), ioe);
                }
            }

            @Override
            public String getOriginalFilename() {
                return f.toPath().getFileName().toString();
            }

            @Override
            public String getContentType() {
                return URLConnection.guessContentTypeFromName(this.getOriginalFilename());
            }
        };
    }
}
