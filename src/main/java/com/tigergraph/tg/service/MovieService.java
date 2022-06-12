package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie getMovie(String id) {
        return movieRepository.getMovie(id).orElse(null);
    }

    public List<Movie> getByTitle(String phrase) {
        return movieRepository.getByTitle(phrase).orElse(null);
    }

    public Award getAward(String id) {
        return movieRepository.getAward(id).orElse(null);
    }

    // returns in order of:
    // vertex id, lastUpdated, t.rating, t.numReviews, plot, awards, id, comments, title, t.meter
    public static Movie reconstructMovie(java.sql.ResultSet rs) throws SQLException {
        return new Movie(rs.getString(7), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5),
                rs.getString(6), rs.getInt(8), rs.getString(9), rs.getInt(10));
    }
}
