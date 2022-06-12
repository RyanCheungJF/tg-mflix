package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Imdb;
import com.tigergraph.tg.repository.ImdbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ImdbService {

    @Autowired
    private ImdbRepository imdbRepository;

    // returns in order of:
    // vertex id, rating, votes, id
    public static Imdb reconstructImdb(java.sql.ResultSet rs) throws SQLException {
        return new Imdb(rs.getInt(4), rs.getDouble(2), rs.getInt(3));
    }
}
