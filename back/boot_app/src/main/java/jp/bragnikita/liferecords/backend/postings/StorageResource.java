package jp.bragnikita.liferecords.backend.postings;

import java.io.InputStream;

public interface StorageResource {

    InputStream getStream();

    String getOriginalFilename();

    String getContentType();

}
