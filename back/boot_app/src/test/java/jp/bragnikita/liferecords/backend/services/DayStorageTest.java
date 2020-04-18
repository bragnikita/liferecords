package jp.bragnikita.liferecords.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.bragnikita.liferecords.backend.TestUtils;
import jp.bragnikita.liferecords.backend.postings.Posting;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.MimeTypeUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static jp.bragnikita.liferecords.backend.StorageTestUtils.FsStorage;
import static jp.bragnikita.liferecords.backend.StorageTestUtils.createStorage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class DayStorageTest {

    private static final String TEST_STORAGE = Path.of("devfs/test_storage").toAbsolutePath().toString();

    private FsStorage fs;
    private DayKey k;
    private DayStorageService dss;

    @BeforeEach
    void setup() throws IOException {
        k = DayKey.fromParameter("20200112");
        fs = createStorage(TEST_STORAGE).linkCopySource(TestUtils.getFileFromResources("json").toPath());
        dss = new DayStorageServiceImpl(k.resolveDir(TEST_STORAGE), k.getStorageDayId());
        fs.reset();
    }

    @Test
    void testListPostings() throws IOException {
        fs.forDay(k.getMonthFolderName(), k.getDayFolderName())
                .put("01.json", "posting_01.json")
                .put("02.json", "posting_01.json");

        assertThat(dss.listPostings())
                .hasSize(2)
                .allSatisfy(posting -> {
                    assertThat(posting.getBody()).isEqualTo("There was a good day");
                });
    }

    @Test
    void testSavePosting() throws IOException {
        FsStorage.FsDay day = fs.forDay(k.getMonthFolderName(), k.getDayFolderName())
                .put("01.json", "posting_01.json");
        final Posting posting = new ObjectMapper()
                .readValue(TestUtils.getFileFromResources("json/posting_01.json"), Posting.class);
        assertThat(dss.savePosting(posting, null)).satisfies(newPosting -> {
            assertThat(newPosting.getId()).isEqualTo(k.getStorageDayId() + "_02");
            assertThat(newPosting.getBody()).isEqualTo(posting.getBody());
        });
        assertThat(day.hasFile("02.json"));
    }

    @Test
    void testUpdatePosting() throws IOException {
        FsStorage.FsDay day = fs.forDay(k.getMonthFolderName(), k.getDayFolderName())
                .put("01.json", "posting_01.json")
                .put("02.json", "posting_02.json");
        final Posting posting = new ObjectMapper()
                .readValue(TestUtils.getFileFromResources("json/posting_02.json"), Posting.class);
        posting.setBody("The best day!");
        assertThat(dss.savePosting(posting, k.getStorageDayId() + "_02")).satisfies(newPosting -> {
            assertThat(newPosting.getId()).isEqualTo(k.getStorageDayId() + "_02");
            assertThat(newPosting.getBody()).isEqualTo("The best day!");
        });
        assertThat(day.hasFile("02.json"));
    }

    @Test
    void testListResources() throws IOException {
        fs.forDay(k).put("file1.png").put("file2.png").put("01.json");
        assertThat(dss.listResources())
                .hasSize(2)
                .anySatisfy(resource -> assertThat(resource.getOriginalFilename()).isEqualTo("file1.png"))
                .anySatisfy(resource -> assertThat(resource.getOriginalFilename()).isEqualTo("file2.png"))
                .allSatisfy(resource -> {
                    assertThat(resource.getContentType()).isEqualTo(MimeTypeUtils.IMAGE_PNG_VALUE);
                    assertThatCode(resource::getStream).doesNotThrowAnyException();
                    assertThat(resource.getStream()).isNotNull();
                });
    }

    @Test
    void testSaveResource() throws IOException {
        fs.forDay(k).put("file1.png").put("file2.png").put("01.json");
        assertThat(dss.saveResource(new ByteArrayInputStream(new byte[]{0xa, 0xa, 0xa}), "file1.png"))
                .isNotNull()
                .satisfies(resource -> {
                    assertThat(resource.getOriginalFilename()).isEqualTo("file1(1).png");
                    try {
                        assertThat(IOUtils.toByteArray(resource.getStream()).length).isEqualTo(3);
                    } catch (IOException ignored) {
                    }
                });
    }
}
