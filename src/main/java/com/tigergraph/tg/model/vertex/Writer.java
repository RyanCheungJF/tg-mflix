package com.tigergraph.tg.model.vertex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Writer {

    private final String type;
    private final String name;

    public Writer(String name) {
        this.type = "Writer";
        this.name = name;
    }
}
