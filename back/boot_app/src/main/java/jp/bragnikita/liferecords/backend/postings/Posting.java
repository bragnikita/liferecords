package jp.bragnikita.liferecords.backend.postings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Posting {

    public enum Access {
        PRIVATE, PUBLIC;

        @JsonValue
        public String getValue() {
            return this.name().toLowerCase();
        }
    }

    @JsonProperty("id")
    private String id;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("ref_date")
    private String referenceDate;

    @JsonProperty("access_type")
    private Access access;

    @JsonProperty("body_format")
    private String format;

    @JsonProperty("body_content")
    private String body;

    private Attachment[] attachments = new Attachment[]{};

    public Attachment[] getAttachments() {
        return attachments;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }
}
