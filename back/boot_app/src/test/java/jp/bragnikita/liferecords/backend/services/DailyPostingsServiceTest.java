package jp.bragnikita.liferecords.backend.services;

import jp.bragnikita.liferecords.backend.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DailyPostingsServiceTest {

    Path storage;
    DailyPostingsService service;

    @BeforeEach
    void setUp() {
        this.storage = Path.of("tmp/storage").toAbsolutePath();
        this.service = new DailyPostingsService(this.storage.toString());
    }

    @Test
    void getDailyPosting() throws IOException {
        DailyFolderControl dailyPosting = this.service.getDailyPosting("2019_12/07");
        List<String> attachments = dailyPosting.getAttachments();
        assertEquals(1, attachments.size());
        String file = attachments.get(0);
        assertEquals("2019_12/07/file.jpg", file);
    }
}