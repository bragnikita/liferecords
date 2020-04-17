package jp.bragnikita.liferecords.backend.controllers;

import jp.bragnikita.liferecords.backend.BackendApplication;
import jp.bragnikita.liferecords.backend.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StorageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testListPostings() throws Exception {
        mvc.perform(get("/postings"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testListDayPhotos() throws Exception {
        mvc.perform(get("/day/{day}/photo", "20200112"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void testUploadPhotoToDay() throws Exception {
        mvc.perform(multipart("/day/{day}/photo", "20200112")
                .file("file", TestUtils.loadFileBytesFromResources("exif_tags_phone_photo.jpg")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.item").isString());
    }

    @Test
    void testNewPostToDay() throws Exception {
        mvc.perform(post("/day/{day}/post", "20200112")
                .content(TestUtils.loadFileAsString("json/posting_01.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.body_content").value("There was a good day"));
    }

    @Test
    void testEditPost() throws Exception {
        mvc.perform(put("/postings/{id}", "1")
                .content(TestUtils.loadFileAsString("json/posting_01.json"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.body_content").value("There was a good day"));
    }

}

