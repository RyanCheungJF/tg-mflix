package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // simple GET
    @GetMapping("/movie/{id}")
    public Movie getMovie(@PathVariable("id") String id) {
        return movieService.getMovie(id);
    }

    // GET using LIKE, gets movie with the search word in its title
    @GetMapping(value = "/movies", params = "q")
    public List<Movie> getByTitle(@RequestParam String q) {
        return movieService.getByTitle(q);
    }


    // GET movies that have an imdb rating higher or equal to the one specified
    @GetMapping(value = "/movies", params = "s")
    public List<Movie> getByScore(@RequestParam Double s) {
        return movieService.getByScore(s);
    }

    // deserialization of award (nested attr modelled as a node)
    // use this endpoint on a movie to get back an Award object
    @GetMapping("/award/{id}")
    public Award getAward(@PathVariable("id") String id) {
        return movieService.getAward(id);
    }
}
