package com.tigergraph.tg.model.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrittenBy {

    private final String type;
    private final String from;
    private final String to;

    public WrittenBy(boolean dir, String from, String to) {
        this.type = dir ? "Written_By" : "reverse_Written_By";
        this.from = from;
        this.to = to;
    }
}
