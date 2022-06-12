package com.tigergraph.tg.model;

public class Genre {

    private final String type;
    private final String name;

    public Genre(String name) {
        this.type = "genre";
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
