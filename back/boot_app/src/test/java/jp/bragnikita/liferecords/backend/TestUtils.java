package jp.bragnikita.liferecords.backend;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;

public class TestUtils {

    public static File getFileFromResources(String resource)  {
        URL url = TestUtils.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new RuntimeException(resource, new FileNotFoundException(resource));
        }
        try {
            return ResourceUtils.getFile(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] loadFileBytesFromResources(String resource) {
        final File file = TestUtils.getFileFromResources(resource);
        try (InputStream is = new FileInputStream(file)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadFileAsString(String resource) {
        final File file = TestUtils.getFileFromResources(resource);
        try (InputStream is = new FileInputStream(file)) {
            return FileCopyUtils.copyToString(new InputStreamReader(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
