package com.tigergraph.tg.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * For Jackson classes, annotations are needed and getter setters for Jackson to set attributes
 */
@Getter
@Setter
public class Award {

    private Integer awardsReceived;
    private Integer nominations;
    private String text;

    @JsonCreator
    public Award (@JsonProperty("awards") Integer awardsReceived, @JsonProperty("nominations") Integer nominations,
                  @JsonProperty("text") String text) {
        this.awardsReceived = awardsReceived;
        this.nominations = nominations;
        this.text = text;
    }
}

