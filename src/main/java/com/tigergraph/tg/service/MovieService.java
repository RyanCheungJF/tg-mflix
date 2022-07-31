package com.tigergraph.tg.service;

import com.tigergraph.tg.model.vertex.Award;
import com.tigergraph.tg.model.vertex.Movie;
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

    public List<Movie> getByPlot(String[] jsonPayload) {
        return movieRepository.getByPlot(jsonPayload).orElse(null);
    }

    public List<Movie> getByImdbId(Integer id) {
        return movieRepository.getByImdbId(id).orElse(null);
    }

    public List<Movie> getByImdbScore(Double score) {
        return movieRepository.getByImdbScore(score).orElse(null);
    }

    public Award getAward(String id) {
        Movie m = movieRepository.getMovie(id).orElse(null);
        return AwardService.extractAwardFromMovie(m).orElse(null);
    }
}
