package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cast {

    private final String type;
    private final String name;

    public Cast(String name) {
        this.type = "cast";
        this.name = name;
    }
}
