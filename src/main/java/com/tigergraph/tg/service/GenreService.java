package com.tigergraph.tg.service;

import com.tigergraph.tg.repository.GenreRepository;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
}
