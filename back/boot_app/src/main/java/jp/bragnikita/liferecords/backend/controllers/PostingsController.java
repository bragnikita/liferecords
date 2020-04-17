package jp.bragnikita.liferecords.backend.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.bragnikita.liferecords.backend.controllers.utils.PathWildcard;
import jp.bragnikita.liferecords.backend.postings.IndexModel;
import jp.bragnikita.liferecords.backend.services.old.DailyFolderControl;
import jp.bragnikita.liferecords.backend.services.old.DailyPostingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/postings")
public class PostingsController {

    @Value("${app.timeline.storage.root}")
    private String uploadsRoot;

    private final DailyPostingsService service;

    public PostingsController(@Autowired DailyPostingsService service) {
        this.service = service;
    }

    @PostMapping("/**/resources")
    @ResponseBody
    public ImageUploadedResponse uploadImage(@RequestParam("file") MultipartFile uploadedFile,
                                             @PathWildcard String collection) throws IOException {
        DailyFolderControl dailyPosting = service.getDailyPosting(collection);
        String fileAccessId = dailyPosting.attachFile(uploadedFile.getOriginalFilename(), uploadedFile.getInputStream());
        String id = String.join("/", collection, fileAccessId);
        return new ImageUploadedResponse(id, id);
    }

    @GetMapping("/**/resources")
    @ResponseBody
    public ResourcesResponse list(@PathWildcard String dayId) throws IOException {
        DailyFolderControl dailyPosting = service.getDailyPosting(dayId);
        ResourcesResponse response = new ResourcesResponse();
        response.getAttachments().addAll(dailyPosting.getAttachments());
        return response;
    }

    @DeleteMapping("/**/resource/{id}")
    public ResponseEntity<Void> deleteImage(@PathWildcard String dayId, @PathVariable("id") String id) throws IOException {
        DailyFolderControl dailyPostings = service.getDailyPosting(dayId);
        dailyPostings.deleteAttachment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/days")
    @ResponseBody
    public List<String> monthDaysWithPostings(@RequestParam("month") String month) throws IOException {
        if (month.equals("")) {
            return List.of();
        }
        return service.listMonthDays(LocalDateTime.now().getYear(), Integer.parseInt(month));
    }
}

class Requests {
    static class DailyPostin {
        String body;
        String access;
    }

}

class ImageUploadedResponse {
    String id;
    String url;

    ImageUploadedResponse(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}


class DailyPostingsResponse {
    @JsonProperty("postings")
    List<IndexModel> postings = new ArrayList<>();

    public List<IndexModel> getPostings() {
        return postings;
    }
}

class ResourcesResponse {
    @JsonProperty("attachments")
    List<String> attachments = new ArrayList<>();

    public List<String> getAttachments() {
        return attachments;
    }
}
