package jp.bragnikita.liferecords.backend;

import jp.bragnikita.liferecords.backend.services.SiteInfoService;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

public class SiteInfoServiceTest {

    private SiteInfoService service = new SiteInfoService();

    private Consumer<SiteInfoService.SitePreview> previewCheck = sitePreview -> {
        assertThat(sitePreview.getTitle()).isNotBlank();
        assertThat(sitePreview.getDescription()).isNotBlank();
        assertThat(sitePreview.getFinalUrl()).isNotBlank();
    };

    @Test
    void testFetchTwitter() throws SiteInfoService.SiteUnaccessibleException {
        assertThat(service.fetchPreview("https://twitter.com"))
        .isNotNull()
        .satisfies(previewCheck);
    }

    @Test
    void testFetchTwitterWithoutHttp() throws SiteInfoService.SiteUnaccessibleException {
        assertThat(service.fetchPreview("twitter.com"))
                .satisfies(previewCheck);
    }

    @Test
    void testFetchUnaccessible()  {
       assertThatThrownBy(() -> service.fetchPreview("http://not_existed_host"))
               .isInstanceOf(SiteInfoService.SiteUnaccessibleException.class);
    }

    @Test
    void testFetchExampleCom() throws SiteInfoService.SiteUnaccessibleException {
        assertThat(service.fetchPreview("http://example.com"))
                .satisfies(sitePreview -> {
                    assertThat(sitePreview.getTitle()).isEqualTo("Example Domain");
                    assertThat(sitePreview.getDescription()).isEqualTo("Example Domain This domain is for use in illustrative examples in documents. You may use this domain in literature without prior coordination or asking for permission. More information...");
                    assertThat(sitePreview.getImages()).isEmpty();
                    assertThat(sitePreview.getFinalUrl()).isEqualTo("http://example.com");
                });
    }
}
