package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.vertex.Genre;
import org.springframework.stereotype.Repository;

@Repository
public class GenreRepository implements TigerGraphRepository<Genre, String> {
}
