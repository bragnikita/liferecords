package jp.bragnikita.liferecords.backend.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DailyFolderControl {

    String attachFile(String filename, InputStream dataStream) throws IOException;

    List<String> getAttachments();

}
