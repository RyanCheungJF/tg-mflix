package com.tigergraph.tg.model.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Casts {

    private final String type;
    private final String from;
    private final String to;

    public Casts(boolean dir, String from, String to) {
        this.type = dir ? "Casts" : "reverse_Casts";
        this.from = from;
        this.to = to;
    }
}
