package com.sudipta.dropwizard.api;

import java.io.Serializable;

public class Adjacent implements Serializable {
    private static final long serialVersionUID = -8581025956358880671L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
