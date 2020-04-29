package jp.bragnikita.liferecords.backend.services;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.broadbear.link.preview.SourceContent;
import org.broadbear.link.preview.TextCrawler;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@Scope("singleton")
public class SiteInfoService {

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class SitePreview {
        String title;
        String finalUrl;
        String description;
        String[] images;

        public String getTitle() {
            return title;
        }

        public String getFinalUrl() {
            return finalUrl;
        }

        public String getDescription() {
            return description;
        }

        public String[] getImages() {
            return images;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SiteUnaccessibleException extends Exception {
    }

    public SitePreview fetchPreview(String url) throws SiteUnaccessibleException {
        if (!url.startsWith("http://") && !(url.startsWith("https://"))) {
            url = "https://" + url;
        }
        final SourceContent scrape = TextCrawler.scrape(url, 0);
        if (scrape.isSuccess()) {
            SitePreview preview = new SitePreview();
            preview.title = scrape.getTitle();
            preview.description = scrape.getDescription();
            preview.finalUrl = scrape.getFinalUrl();
            preview.images = scrape.getImages().toArray(new String[0]);
            return preview;
        }
        throw new SiteUnaccessibleException();
    }
}
