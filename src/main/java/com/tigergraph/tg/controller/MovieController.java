package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.Award;
import com.tigergraph.tg.model.Movie;
import com.tigergraph.tg.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    // simple GET
    @GetMapping("/movie/{id}")
    public Movie getMovie(@PathVariable("id") String id) {
        return movieService.getMovie(id);
    }

    // GET using LIKE, gets movie with the search word in its title
    @GetMapping("/movietitle/{phrase}")
    public List<Movie> getByTitle(@PathVariable("phrase") String phrase) {
        return movieService.getByTitle(phrase);
    }

    // deserialization of award (nested attr modelled as a node)
    // use this endpoint on a movie to get back an Award object
    @GetMapping("/award/{id}")
    public Award getAward(@PathVariable("id") String id) {
        return movieService.getAward(id);
    }
}
