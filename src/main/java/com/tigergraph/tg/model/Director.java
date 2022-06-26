package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Director {

    private final String type;
    private final String name;

    public Director(String name) {
        this.type = "director";
        this.name = name;
    }
}
