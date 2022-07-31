package com.tigergraph.tg.model.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsOn {

    private final String from;
    private final String to;
    private final String type;
    private final String id;
    private final String email;
    private final String movie_id;
    private final String text;

    public CommentsOn(boolean dir, String from, String to, String id, String email, String movie_id, String text) {
        this.type = dir ? "Comments_On" : "reverse_Comments_On";
        this.from = from;
        this.to = to;
        this.id = id;
        this.email = email;
        this.movie_id = movie_id;
        this.text = text;
    }
}
