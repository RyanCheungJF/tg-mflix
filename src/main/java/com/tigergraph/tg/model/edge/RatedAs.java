package com.tigergraph.tg.model.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatedAs {

    private final String type;
    private final String from;
    private final String to;

    public RatedAs(boolean dir, String from, String to) {
        this.type = dir ? "Rated_As" : "reverse_Rated_As";
        this.from = from;
        this.to = to;
    }
}
