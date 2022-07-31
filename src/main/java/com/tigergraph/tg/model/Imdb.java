package com.tigergraph.tg.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
