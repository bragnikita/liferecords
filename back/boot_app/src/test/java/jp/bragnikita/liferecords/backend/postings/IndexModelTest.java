package jp.bragnikita.liferecords.backend.postings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

public class IndexModelTest {
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testParse() throws IOException {
        IndexModel model = mapper.readValue(
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("index.json")),
                IndexModel.class
        );
        Assertions.assertEquals("2016-01-12", model.getId());
        Assertions.assertEquals(1, model.getContent().length);
        Assertions.assertEquals("vkpost", model.getContent()[0].getPostingType());
        Assertions.assertEquals(Posting.Access.PRIVATE, model.getContent()[0].getAccess());
        Assertions.assertEquals("2012-01-12T07:04:34+09:00", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(model.getContent()[0].getPublishAt()));
        Assertions.assertEquals(12, LocalDate.ofInstant(model.getDate().toInstant(), ZoneId.systemDefault()).getDayOfMonth());

        mapper.writeValue(System.out, model);
        System.out.println("\n-----");
    }

    @Test
    void testSerialize() throws IOException {
        IndexModel model = new IndexModel();
        model.setId("2012_02");
        model.setDate(new Date(LocalDate.of(2012, 2, 1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()));
        Posting p = new Posting();
        p.setPostingType("vkpost");
        p.setCreatedAt(Instant.now().toEpochMilli());
        p.setPublishAt(Date.from(Instant.now()));
        p.setAccess(Posting.Access.PRIVATE);
        Posting[] postings = new Posting[]{
                p,
        };
        model.setContent(postings);
        mapper.writeValue(System.out, model);
        System.out.println("\n-----");
    }

}
