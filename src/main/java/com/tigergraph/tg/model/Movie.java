package com.tigergraph.tg.model;

public class Movie {

    private final String type;
    private final String id;
    private final String lastUpdated;
    private final Double tomatoesRating;
    private final Integer tomatoesReviews;
    private final String plot;
    private final String awards;
    private final Integer mflixComments;
    private final String title;
    private final Integer tomatoesMeter;

    public Movie(String id, String lastUpdated, Double tomatoesRating, Integer tomatoesReviews, String plot,
                 String awards, Integer mflixComments, String title, Integer tomatoesMeter) {
        this.type = "movie";
        this.id = id;
        this.lastUpdated = lastUpdated;
        this.tomatoesRating = tomatoesRating;
        this.tomatoesReviews = tomatoesReviews;
        this.plot = plot;
        this.awards = awards;
        this.mflixComments = mflixComments;
        this.title = title;
        this.tomatoesMeter = tomatoesMeter;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public Double getTomatoesRating() {
        return tomatoesRating;
    }

    public Integer getTomatoesReviews() {
        return tomatoesReviews;
    }

    public String getPlot() {
        return plot;
    }

    public String getAwards() {
        return awards;
    }

    public Integer getMflixComments() {
        return mflixComments;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTomatoesMeter() {
        return tomatoesMeter;
    }
}
