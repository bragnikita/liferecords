package jp.bragnikita.liferecords.backend.controllers;

import jp.bragnikita.liferecords.backend.controllers.utils.PathWildcard;
import jp.bragnikita.liferecords.backend.services.DailyFolderControl;
import jp.bragnikita.liferecords.backend.services.DailyPostingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/postings")
public class PostingsController {

    @Value("${app.timeline.storage.root}")
    private String uploadsRoot;

    private final DailyPostingsService service;

    public PostingsController(@Autowired DailyPostingsService service) {
        this.service = service;
    }

    @PostMapping("/**/attachments") @ResponseBody
    public ImageUploadedResponse uploadImage(@RequestParam("file")MultipartFile uploadedFile,
                                             @PathWildcard String collection) throws IOException {
        DailyFolderControl dailyPosting = service.getDailyPosting(collection);
        String fileAccessId = dailyPosting.attachFile(uploadedFile.getOriginalFilename(), uploadedFile.getInputStream());
        String id = String.join("/", collection, fileAccessId);
        return new ImageUploadedResponse(id, id);
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
