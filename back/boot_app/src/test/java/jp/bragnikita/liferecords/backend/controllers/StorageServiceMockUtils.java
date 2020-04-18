package jp.bragnikita.liferecords.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.bragnikita.liferecords.backend.TestUtils;
import jp.bragnikita.liferecords.backend.postings.Posting;
import jp.bragnikita.liferecords.backend.postings.StorageResource;
import jp.bragnikita.liferecords.backend.services.DayStorageService;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;

public class StorageServiceMockUtils {

    public static class DailyStorages {
        public static DayStorageService forListing(Posting... postings) {
            DayStorageService dss = Mockito.mock(DayStorageService.class);
            return Mockito.when(dss.listPostings()).thenReturn(postings).getMock();
        }

        public static DayStorageService forCreate(Posting posting) {
            DayStorageService dss = Mockito.mock(DayStorageService.class);
            return Mockito.when(dss.savePosting(Mockito.any(Posting.class), Mockito.nullable(String.class))).thenReturn(posting).getMock();
        }

        public static DayStorageService forDayPhotosListings(StorageResource... resources) {
            DayStorageService dss = Mockito.mock(DayStorageService.class);
            Mockito.when(dss.listResources()).thenReturn(resources);
            return dss;
        }
    }

    public static class Postings {

        public static Posting fromJson(String resource) {
            try {
                return new ObjectMapper().readValue(TestUtils.getFileFromResources(resource), Posting.class);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

    }

    public static class Resources {
        public static StorageResource fromResource(String resource) {
            final ByteArrayInputStream bis = new ByteArrayInputStream(TestUtils.loadFileBytesFromResources(resource));
            final String filename = Path.of(resource).getFileName().toString();
            final String contentType = URLConnection.guessContentTypeFromName(filename);
            final StorageResource mock = Mockito.mock(StorageResource.class);

            Mockito.when(mock.getStream()).thenReturn(bis);
            Mockito.when(mock.getOriginalFilename()).thenReturn(filename);
            Mockito.when(mock.getContentType()).thenReturn(contentType);

            return mock;
        }
    }

}
