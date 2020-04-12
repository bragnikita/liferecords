package jp.bragnikita.liferecords.backend;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class TestUtils {

    public static File getFileFromResources(String resource) throws FileNotFoundException {
        URL url = TestUtils.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new FileNotFoundException(resource);
        }
        try {
            return ResourceUtils.getFile(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
