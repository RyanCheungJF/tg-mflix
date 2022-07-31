package com.tigergraph.tg.model.requestbody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class AllLinksRequestBody {

    @NonNull
    private final ArrayList<EntityTypeWithPrimaryKey> sources;
    @NonNull
    private final ArrayList<EntityTypeWithPrimaryKey> targets;
    @NonNull
    private final ArrayList<? super EntityTypeWithFilter> vertexFilters;
    @NonNull
    private final ArrayList<? super EntityTypeWithFilter> edgeFilters;
    private final Integer maxLength;
}

