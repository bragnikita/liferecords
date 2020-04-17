package jp.bragnikita.liferecords.backend.services;

import jp.bragnikita.liferecords.backend.postings.Posting;
import jp.bragnikita.liferecords.backend.postings.StorageResource;

import java.io.InputStream;

public interface DayStorageService {

    Posting[] listPostings();

    StorageResource[] listResources();

    Posting savePosting(Posting posting);

    StorageResource saveResource(InputStream data, String originalFileName);

}
