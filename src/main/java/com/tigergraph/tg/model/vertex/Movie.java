package com.tigergraph.tg.model.vertex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Movie {

    private final String type;
    private final String id;
    private final String plot;
    private final Integer mflixComments;
    private final String title;
    private final String awards;
    private final String lastUpdated;
    private final Integer tomatoesMeter;
    private final Double tomatoesRating;
    private final Integer tomatoesReviews;

    public Movie(String id, String plot, Integer mflixComments, String title, String awards, String lastUpdated,
                 Integer tomatoesMeter, Double tomatoesRating, Integer tomatoesReviews) {
        this.type = "Movie";
        this.id = id;
        this.plot = plot;
        this.mflixComments = mflixComments;
        this.title = title;
        this.awards = awards;
        this.tomatoesMeter = tomatoesMeter;
        this.lastUpdated = lastUpdated;
        this.tomatoesRating = tomatoesRating;
        this.tomatoesReviews = tomatoesReviews;
    }
}
