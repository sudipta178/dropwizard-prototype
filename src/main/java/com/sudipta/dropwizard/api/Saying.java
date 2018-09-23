package com.sudipta.dropwizard.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

public class Saying implements Serializable {
    private static final long serialVersionUID = 4426066871335346015L;

    private long id;
    @Length(max = 3)
    private String content;

    public Saying() {
        // Jackson deserialization
    }

    public Saying(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
