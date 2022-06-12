package com.tigergraph.tg.model;

public class Imdb {

    private final String type;
    private final Integer id;
    private final Double rating;
    private final Integer votes;

    public Imdb(Integer id, Double rating, Integer votes) {
        this.type = "imdb";
        this.id = id;
        this.rating = rating;
        this.votes = votes;
    }

    public String getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getVotes() {
        return votes;
    }
}
