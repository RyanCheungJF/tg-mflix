package com.tigergraph.tg.model.requestbody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents part of the request body, specifies only the type of node
 */
@Getter
@Setter
@AllArgsConstructor
public class EntityType {

    private final String type;
}
