package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.vertex.Award;
import com.tigergraph.tg.model.vertex.Movie;
import com.tigergraph.tg.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Gets a movie by its unqiue id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found movie",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Movie.class))}),
        @ApiResponse(responseCode = "404", description = "Movie does not exist",
            content = @Content)
    })
    @GetMapping(value = "/movie/{id}")
    public Movie getMovie(@Parameter(description = "Movie id") @PathVariable("id") String id) {
        return movieService.getMovie(id);
    }

    @Operation(summary = "Gets a movie by regex strings matching its plot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found movie",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Movie does not exist",
            content = @Content)
    })
    @GetMapping(value = "/movie/plot", params = {"filter"})
    public List<Movie> getByPlot(@Parameter(description = "Array of regex strings") @RequestParam String[] filter) {
        return movieService.getByPlot(filter);
    }

    @Operation(summary = "Gets movies by the imdb id tagged to it")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found movie",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Movie.class))}),
        @ApiResponse(responseCode = "404", description = "Movie does not exist",
            content = @Content)
    })
    @GetMapping(value = "/movie/imdb/{id}")
    public List<Movie> getByImdbId(@Parameter(description = "Imdb id") @PathVariable("id") Integer id) {
        return movieService.getByImdbId(id);
    }

    @Operation(summary = "Gets a list of movies which have a higher imdb score than the specified number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found movie",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Movie.class))}),
        @ApiResponse(responseCode = "404", description = "Movie does not exist",
            content = @Content)
    })
    @GetMapping(value = "/movies", params = "score")
    public List<Movie> getByImdbScore(@Parameter(description = "Imdb score") @RequestParam Double score) {
        return movieService.getByImdbScore(score);
    }

    @Operation(summary = "Gets the nested award object serialized as a string attribute in movie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully obtained award",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Award.class))}),
        @ApiResponse(responseCode = "404", description = "Unable to obtain award information",
            content = @Content)
    })
    @GetMapping(value = "/movie/award", params = "id")
    public Award getAward(@Parameter(description = "Movie id") @RequestParam String id) {
        return movieService.getAward(id);
    }
}
