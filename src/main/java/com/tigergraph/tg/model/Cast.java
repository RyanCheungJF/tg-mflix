package com.tigergraph.tg.model;

public class Cast {

    private final String type;
    private final String name;

    public Cast(String name) {
        this.type = "cast";
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
