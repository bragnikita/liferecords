package jp.bragnikita.liferecords.backend.postings;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class IndexModel {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Posting[] content;

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Posting[] getContent() {
        return content;
    }

    public void setContent(Posting[] content) {
        this.content = content;
    }
}
