package com.tigergraph.tg.model.vertex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {

    private final String type;
    private final String name;

    public Genre(String name) {
        this.type = "Genre";
        this.name = name;
    }
}
