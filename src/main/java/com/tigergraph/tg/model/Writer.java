package com.tigergraph.tg.model;

public class Writer {

    private final String type;
    private final String name;

    public Writer(String name) {
        this.type = "writer";
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
