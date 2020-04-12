package jp.bragnikita.liferecords.backend.dropfiles;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CopyToDiaryStorageTask {

    private Path storageRoot;

    public CopyToDiaryStorageTask(Path storageRoot) {
        this.storageRoot = storageRoot;
    }

    @Nullable
    public String copy(File target) throws IOException {
        if (!target.exists()) throw new RuntimeException("File not exists: " + target);
        if (!target.isFile()) throw new RuntimeException("File is not regular file: " + target);

        if (isExifProcessableMedia(target)) {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(target);
                ExifIFD0Directory exifTag = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                Date createdAt = exifTag.getDate(ExifSubIFDDirectory.TAG_DATETIME);
                LocalDateTime createdAtLocal = LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("GMT"));
                return this.copyToDate(target, createdAtLocal);
            } catch (ImageProcessingException ipe) {
                throw new IOException(ipe);
            }
        }
        // TODO use filename pattern or ctime
        return null;
    }

    public String copyToDate(File file, LocalDateTime date) throws IOException {
        String dir = date.format(DateTimeFormatter.ofPattern("YYYY_MM"));
        String day = lpadTo2(date.getDayOfMonth());

        Path fillPathToDir = storageRoot.resolve(Path.of(dir, day));
        if (!fillPathToDir.toFile().exists()) {
            fillPathToDir = Files.createDirectories(fillPathToDir);
        }
        Path newFilePath = fillPathToDir.resolve(file.getName());
        Path result = Files.copy(file.toPath(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
        return result.toString();
    }

    private String lpadTo2(Integer i) {
        return i < 10 ? "0" + i : Integer.toString(i);
    }

    public static void printMetadata(File target) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(target);
        for (Directory directory : metadata.getDirectories()) {
            System.out.printf("[%s]\n", directory.getName());
            for (Tag tag : directory.getTags()) {
                System.out.printf("%d | %s (%s) - %s \n", tag.getTagType(), tag.getTagTypeHex(), tag.getTagName(), tag.getDescription());
            }
        }
    }

    private boolean isExifProcessableMedia(File f) {
        String ext = getExtension(f.toPath().getFileName().toString());
        return ext.matches("(png)|(jpe?g)|(tiff)|(bmp)");
    }

    private String getExtension(String filename) {
        String[] split = filename.split(".");
        if (split.length > 1) {
            return split[split.length - 1];
        }
        return "";
    }

}
