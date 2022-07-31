package com.tigergraph.tg.model.vertex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cast {

    private final String type;
    private final String name;

    public Cast(String name) {
        this.type = "Cast";
        this.name = name;
    }
}
