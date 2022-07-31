package com.tigergraph.tg.model.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorizedAs {

    private final String type;
    private final String from;
    private final String to;

    public CategorizedAs(boolean dir, String from, String to) {
        this.type = dir ? "Categorized_As" : "reverse_Categorized_As";
        this.from = from;
        this.to = to;
    }
}
