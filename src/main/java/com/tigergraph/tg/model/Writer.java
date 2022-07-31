package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Writer {

    private final String type;
    private final String name;

    public Writer(String name) {
        this.type = "writer";
        this.name = name;
    }
}
