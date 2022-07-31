package com.tigergraph.tg.model.requestbody;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents part of the request body, specifies which exact node by specifying its type and pk
 */
@Getter
@Setter
public class EntityTypeWithPrimaryKey extends EntityType {

    private final Object id;

    public EntityTypeWithPrimaryKey(String type, Object id) {
        super(type);
        this.id = id;
    }
}
