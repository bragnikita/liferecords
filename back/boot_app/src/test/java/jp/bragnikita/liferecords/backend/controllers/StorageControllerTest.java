package jp.bragnikita.liferecords.backend.controllers;

import jp.bragnikita.liferecords.backend.BackendApplication;
import jp.bragnikita.liferecords.backend.TestUtils;
import jp.bragnikita.liferecords.backend.postings.Posting;
import jp.bragnikita.liferecords.backend.postings.StorageResource;
import jp.bragnikita.liferecords.backend.services.DayStorageService;
import jp.bragnikita.liferecords.backend.services.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StorageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StorageService storage;

    @Test
    void testListPostings() throws Exception {
        DayStorageService dayStorageService = StorageServiceMockUtils.DailyStorages
                .forListing(StorageServiceMockUtils.Postings
                        .fromJson("json/posting_01.json"));
        when(storage.getDayStorageService("20200112"))
                .thenReturn(dayStorageService);

        mvc.perform(get("/postings").queryParam("day", "20200112"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dayStorageService).listPostings();
        verify(storage).getDayStorageService("20200112");
    }

    @Test
    void testListDayPhotos() throws Exception {
        StorageResource resource = StorageServiceMockUtils.Resources.fromResource("exif_tags_phone_photo.jpg");
        DayStorageService dss = StorageServiceMockUtils.DailyStorages.forDayPhotosListings(resource);
        when(storage.getDayStorageService("20200112")).thenReturn(dss);

        mvc.perform(get("/day/{day}/photo", "20200112"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0]")
                        .value("2020_01/12/exif_tags_phone_photo.jpg"));

        verify(dss).listResources();
    }

    @Test
    void testUploadPhotoToDay() throws Exception {
        // TODO

        mvc.perform(multipart("/day/{day}/photo", "20200112")
                .file("file", TestUtils.loadFileBytesFromResources("exif_tags_phone_photo.jpg")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.item").isString());
    }

    @Test
    void testNewPostToDay() throws Exception {
        Posting posting = StorageServiceMockUtils.Postings
                .fromJson("json/posting_01.json");
        DayStorageService dayStorageService = StorageServiceMockUtils.DailyStorages
                .forCreate(posting);
        when(storage.getDayStorageService("20200112"))
                .thenReturn(dayStorageService);

        mvc.perform(post("/day/{day}/post", "20200112")
                .content(TestUtils.loadFileAsString("json/posting_01.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.body_content").value("There was a good day"));

        verify(storage).getDayStorageService("20200112");
    }

    @Test
    void testEditPost() throws Exception {
        // TODO
        mvc.perform(put("/postings/{id}", "20200112_01")
                .content(TestUtils.loadFileAsString("json/posting_01.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.body_content").value("There was a good day"));
    }

}

