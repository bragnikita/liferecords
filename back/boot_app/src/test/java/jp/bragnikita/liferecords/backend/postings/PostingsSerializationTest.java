package jp.bragnikita.liferecords.backend.postings;

import jp.bragnikita.liferecords.backend.TestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PostingsSerializationTest {

    @Autowired
    private JacksonTester<Posting> json;

    @Test
    @Order(1)
    public void testDeserialize() throws IOException {
        Posting posting = json.readObject(TestUtils.getFileFromResources("json/posting_01.json"));
        assertThat(posting.getBody()).isEqualTo("There was a good day");
        assertThat(posting.getFormat()).isEqualTo("plain");
        assertThat(posting.getAccess()).isEqualTo(Posting.Access.PUBLIC);
        assertThat(posting.getAttachments()).hasSize(2);
        assertThat(posting.getAttachments()).hasOnlyElementsOfType(Attachment.class);
        assertThat(posting.getAttachments()).allSatisfy(attachment -> assertThat(attachment.getType()).isNotNull());
    }

    @Test
    @Order(2)
    public void testSerialize() throws IOException {
        Posting posting = json.readObject(TestUtils.getFileFromResources("json/posting_01.json"));
        assertThat(json.write(posting)).isEqualToJson(TestUtils.getFileFromResources("json/posting_01.json"));
    }

}
