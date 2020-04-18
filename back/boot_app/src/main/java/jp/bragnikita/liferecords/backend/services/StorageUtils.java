package jp.bragnikita.liferecords.backend.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.core.parameters.P;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StorageUtils {

    public static FilenameFilter filterByExtension(String... extensions) {
        return (dir, name) -> FilenameUtils.isExtension(name, extensions);
    }

    public static String[] list(Path dir, FilenameFilter filter) {
        String[] list = dir.toFile().list(filter);
        if (list == null) {
            throw new StorageException("%s is not directory", dir.toString());
        }
        return list;
    }

    public static FilenameFilter reverseFilter(FilenameFilter filter) {
        return (dir, name) -> !filter.accept(dir, name);
    }

    public static String lookForUniqueName(Path dir, String initialName) {
        if (!dir.resolve(initialName).toFile().exists()) {
            return initialName;
        }
        final String filename = FilenameUtils.getBaseName(initialName);
        final String ext = FilenameUtils.getExtension(initialName);
        int counter = 0;
        File f;
        String nextFilename;
        do {
            counter += 1;
            nextFilename = String.format("%s(%d).%s", filename, counter, ext);
            f = dir.resolve(nextFilename).toFile();
        } while (f.exists());
        return nextFilename;
    }

    public static String lookForNextPostingId(Path dir) {
        Optional<Integer> max = Arrays.stream(list(dir, filterByExtension("json")))
                .map(FilenameUtils::getBaseName)
                .map(s -> StringUtils.stripStart(s, "0"))
                .filter(NumberUtils::isParsable)
                .map(NumberUtils::createInteger)
                .max(Integer::compareTo);
        int next = max.orElse(0) + 1;
        return String.format("%02d", next);
    }
}
