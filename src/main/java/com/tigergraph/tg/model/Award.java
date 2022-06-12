package com.tigergraph.tg.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * For Jackson classes, annotations are needed and getter setters for Jackson to set attributes
 */
public class Award {

    private Integer awards;
    private Integer nominations;
    private String text;

    @JsonCreator
    public Award (@JsonProperty("awards") Integer awards, @JsonProperty("nominations") Integer nominations,
                  @JsonProperty("text") String text) {
        this.awards = awards;
        this.nominations = nominations;
        this.text = text;
    }

    public Integer getAwards() {
        return awards;
    }

    public Integer getNominations() {
        return nominations;
    }

    public String getText() {
        return text;
    }

    public void setAwards(Integer awards) {
        this.awards = awards;
    }

    public void setNominations(Integer nominations) {
        this.nominations = nominations;
    }

    public void setText(String text) {
        this.text = text;
    }
}

