package com.tigergraph.tg.model.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectedBy {

    private final String type;
    private final String from;
    private final String to;

    public DirectedBy(boolean dir, String from, String to) {
        this.type = dir ? "Directed_By" : "reverse_Directed_By";
        this.from = from;
        this.to = to;
    }
}
