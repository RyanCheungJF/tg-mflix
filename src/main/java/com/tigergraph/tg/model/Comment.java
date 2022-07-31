package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {

    private final String type;
    private final String id;
    private final String email;
    private final String movie_id;
    private final String text;

    public Comment(String id, String email, String movie_id, String text) {
        this.type = "comment";
        this.id = id;
        this.email = email;
        this.movie_id = movie_id;
        this.text = text;
    }
}
