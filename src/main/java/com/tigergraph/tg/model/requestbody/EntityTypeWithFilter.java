package com.tigergraph.tg.model.requestbody;

import lombok.Getter;
import lombok.Setter;


/**
 * Represents part of the request body and acts as a filter by the specified condition
 */
@Getter
@Setter
public class EntityTypeWithFilter extends EntityType {

    private final String condition;

    public EntityTypeWithFilter(String type, String condition) {
        super(type);
        this.condition = condition;
    }
}
