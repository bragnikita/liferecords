package jp.bragnikita.liferecords.backend.services;

import jp.bragnikita.liferecords.backend.postings.IndexModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DailyFolderControl {

    String attachFile(String filename, InputStream dataStream) throws IOException;

    List<String> getAttachments();

    IndexModel getIndex();

    void deleteAttachment(String id);


}
