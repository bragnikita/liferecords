package jp.bragnikita.liferecords.backend.postings;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.bragnikita.liferecords.backend.TestUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import static org.assertj.core.api.Assertions.*;
import org.springframework.boot.test.json.ObjectContent;

import org.springframework.boot.test.json.*;

import java.io.File;
import java.io.IOException;

@JsonTest
public class AttachmentSerializationTest {

    @Autowired
    private JacksonTester<Attachment> json;

    @Test
    @Order(1)
    public void testSerializeLink() throws IOException {
        Attachment o = json.read(TestUtils.getFileFromResources("json/attachment_link_01.json")).getObject();
        assertThat(o.getTitle()).isEqualTo("Jackson is cool!");
        assertThat(o.getUrl()).isEqualTo("https://www.baeldung.com/jackson-mapping-dynamic-object");
        assertThat(o.getType()).isEqualTo(Attachment.TYPE.LINK);
        assertThat(o.getMeta()).isNotNull();
        assertThat(o.getMeta().required("keywords").textValue()).isEqualTo("json");
    }

    @Test
    @Order(2)
    public void testDeserializeLink() throws IOException {
        final File jsonFile = TestUtils.getFileFromResources("json/attachment_link_01.json");
        Attachment o = json.read(jsonFile).getObject();
        assertThat(json.write(o)).isEqualToJson(jsonFile);
    }

}
