package com.tigergraph.tg.model;

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

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getText() {
        return text;
    }
}
