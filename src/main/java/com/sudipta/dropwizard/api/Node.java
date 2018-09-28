package com.sudipta.dropwizard.api;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable{
    private static final long serialVersionUID = 6748647006400337195L;

    private Integer id;
    private String displayName;
    private String name;
    private List<Adjacent> adjacents;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Adjacent> getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(List<Adjacent> adjacents) {
        this.adjacents = adjacents;
    }
}
