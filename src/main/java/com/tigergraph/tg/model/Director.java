package com.tigergraph.tg.model;

public class Director {

    private final String type;
    private final String name;

    public Director(String name) {
        this.type = "director";
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
