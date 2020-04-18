package jp.bragnikita.liferecords.backend.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import jp.bragnikita.liferecords.backend.postings.Posting;
import jp.bragnikita.liferecords.backend.postings.StorageResource;
import jp.bragnikita.liferecords.backend.services.DayKey;
import jp.bragnikita.liferecords.backend.services.DayStorageService;
import jp.bragnikita.liferecords.backend.services.StorageException;
import jp.bragnikita.liferecords.backend.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StorageController {

    private StorageService storage;

    @Autowired
    private Environment env;

    public StorageController(@Autowired StorageService storage) {
        this.storage = storage;
    }

    @GetMapping("/postings")
    Posting[] listPostings(@RequestParam Map<String, String> filters) {
        System.out.println(filters);
        if (filters.containsKey("day")) {
            // TODO validate query parameter
            return storage.getDayStorageService(filters.get("day")).listPostings();
        }
        return new Posting[]{};
    }

    @GetMapping("/day/{day}/photo")
    ItemResponse listDayPhotos(@PathVariable("day") String day) {
        DayKey dayKey = DayKey.fromParameter(day);
        StorageResource[] resources = storage.getDayStorageService(day).listResources();
        String[] paths = Arrays.stream(resources).map(resource -> {
            final String filename = resource.getOriginalFilename();
            return Path.of(dayKey.getStoragePath(), filename).toString();
        }).toArray(String[]::new);
        return ItemResponse.items(paths);
    }

    @PostMapping("/day/{day}/photo")
    ItemResponse uploadPhotoToDay(@PathVariable("day") String day, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        uploadedFile.transferTo(new File("/dev/null"));
        return ItemResponse.item("");
    }

    @PostMapping("/day/{day}/post")
    Posting newPostToDay(@PathVariable("day") String day, @RequestBody Posting post) {
        return storage.getDayStorageService(day).savePosting(post, null);
    }

    @PutMapping("/postings/{id}")
    Posting editPost(@PathVariable("id") String id, @RequestBody Posting post) {

        return post;
    }

    @ExceptionHandler(StorageException.class)
    ResponseEntity<ApiError> storageException(StorageException ex) {
        ApiError error;
        if (env.acceptsProfiles(Profiles.of("development | test"))) {
            error = new ApiError("storage", "Storage exception", ex.getMessage());
        } else {
            error = new ApiError("storage", "Storage exception", null);
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public static class ItemResponse {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object item;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object[] items;

        static <T> ItemResponse items(T[] items) {
            ItemResponse response = new ItemResponse();
            response.items = items;
            return response;
        }

        static <T> ItemResponse item(T item) {
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

    public static class ApiError {
        String details;
        String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String code;

        ApiError(String code, String message, String details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }
}
