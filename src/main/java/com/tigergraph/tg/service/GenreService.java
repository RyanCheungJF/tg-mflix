package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Director;
import com.tigergraph.tg.model.Genre;
import com.tigergraph.tg.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public static Genre reconstructGenre(java.sql.ResultSet rs) throws SQLException {
        return new Genre(rs.getString(1));
    }
}
