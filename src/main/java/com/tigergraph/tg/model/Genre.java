package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {

    private final String type;
    private final String name;

    public Genre(String name) {
        this.type = "genre";
        this.name = name;
    }
}
