package jp.bragnikita.liferecords.backend.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import jp.bragnikita.liferecords.backend.postings.Posting;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class StorageController {

    @GetMapping("/postings")
    Posting[] listPostings(@RequestParam Map<String, String> filters) {

        return new Posting[]{};
    }

    @GetMapping("/day/{day}/photo")
    ItemResponse listDayPhotos(@PathParam("day") String day) {

        return ItemResponse.items(new String[]{});
    }

    @PostMapping("/day/{day}/photo")
    ItemResponse uploadPhotoToDay(@PathParam("day") String day, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        uploadedFile.transferTo(new File("/dev/null"));
        return ItemResponse.item("");
    }

    @PostMapping("/day/{day}/post")
    Posting newPostToDay(@PathParam("day") String day, @RequestBody Posting post) {
        return post;
    }

    @PutMapping("/postings/{id}")
    Posting editPost(@PathParam("id") String id, @RequestBody Posting post) {

        return post;
    }

    public static class ItemResponse {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object item;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object[] items;

        static <T>ItemResponse items(T[] items) {
            ItemResponse response = new ItemResponse();
            response.items = items;
            return response;
        }

        static <T>ItemResponse item(T item) {
            ItemResponse response = new ItemResponse();
            response.item = item;
            return response;
        }

        public Object getItem() {
            return item;
        }

        public Object[] getItems() {
            return items;
        }
    }
}
