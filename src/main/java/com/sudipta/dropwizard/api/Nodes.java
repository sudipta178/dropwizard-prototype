package com.sudipta.dropwizard.api;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Nodes implements Serializable{
    private static final long serialVersionUID = 1026951382786013599L;

    private int id;
    private String name;
    private String route;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Nodes> children;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private boolean visited;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<Nodes> getChildren() {
        return children;
    }

    public void setChildren(List<Nodes> children) {
        this.children = children;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
