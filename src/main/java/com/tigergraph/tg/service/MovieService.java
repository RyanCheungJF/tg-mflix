package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovie(String id) {
        return movieRepository.getMovie(id).orElse(null);
    }

    public List<Movie> getByTitle(String phrase) {
        return movieRepository.getByTitle(phrase).orElse(null);
    }

    public List<Movie> getByScore(Double score) {
        return movieRepository.getByScore(score).orElse(null);
    }

    public Award getAward(String id) {
        Movie m = movieRepository.getMovie(id).orElse(null);
        return AwardService.extractAwardFromMovie(m).orElse(null);
    }
}
