package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.requestbody.AllLinksRequestBody;
import com.tigergraph.tg.service.CastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CastController {

    private final CastService castService;

    public CastController(CastService castService) {
        this.castService = castService;
    }

    @Operation(summary = "Gets all paths from a source set to a target set")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = AllLinksRequestBody.class))}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @PostMapping(value = "/cast/links")
    public List<List<Object>> getAllPathsFromASourceSetToTargetSet(@RequestBody AllLinksRequestBody jsonPayload) {
        return castService.getAllPathsFromASourceSetToTargetSet(jsonPayload);
    }

    @Operation(summary = "Gets all paths in between all cast members")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = AllLinksRequestBody.class))}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @GetMapping(value = "/cast/links", params = "len")
    public List<List<Object>> getAllLinksForCasts(@Parameter(description = "Max length of the path")
                                                      @RequestParam Integer len) {
        return castService.getAllLinksForCasts(len);
    }

    @Operation(summary = "Gets all paths in between all cast members only using movies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = AllLinksRequestBody.class))}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @GetMapping(value = "/cast/movie/links", params = "len")
    public List<List<Object>> getAllLinksForCastsTraverseMovie(@Parameter(description = "Max length of the path")
                                                  @RequestParam Integer len) {
        return castService.getAllLinksForCastsTraverseMovie(len);
    }

    @Operation(summary = "Gets all paths between cast nodes that have a higher tomato meter than specified")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @GetMapping(value = "/cast/links/tomato", params = {"score", "len"})
    public List<List<Object>> getAllLinksWithTomatoMeter(@Parameter(description = "Tomatoes meter score")
                                                              @RequestParam Integer score,
                                                          @Parameter(description = "Max length of the path")
                                                              @RequestParam Integer len) {
        return castService.getAllLinksWithTomatoMeter(score, len);
    }

    @Operation(summary = "Gets all paths between cast nodes that have won more awards than specified")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @GetMapping(value = "/cast/links/award/direct", params = {"awards", "len"})
    public List<List<Object>> getAllLinksWithAwardNumber(@Parameter(description = "Number of awards")
                                                             @RequestParam Integer awards,
                                                         @Parameter(description = "Max length of the path")
                                                             @RequestParam Integer len) {
        return castService.getAllLinksWithAwardNumber(awards, len);
    }

    @Operation(summary = "Gets all paths between cast nodes that have a higher imdb rating than specified")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @GetMapping(value = "/cast/links/rating", params = {"rating", "len"})
    public List<List<Object>> getAllLinksWithImdbRatingNumber(@Parameter(description = "Imdb rating")
                                                                  @RequestParam Double rating,
                                                              @Parameter(description = "Max length of the path")
                                                                  @RequestParam Integer len) {
        return castService.getAllLinksWithImdbRatingNumber(rating, len);
    }

    @Operation(summary = "Gets all paths between cast nodes that won a certain number of awards " +
        "and paths must only use the movie nodes as specified")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found nodes and edges while traversing all paths",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any path",
            content = @Content)
    })
    @PostMapping(value = "/cast/links/awards/indirect", params = "awards")
    public List<List<Object>> getAllIndirectLinksWithAwardNumber(@Parameter(description = "Number of awards")
                                                                         @RequestParam Integer awards,
                                                                 @RequestBody AllLinksRequestBody jsonPayload) {
        return castService.getAllIndirectLinksWithAwardNumber(awards, jsonPayload);
    }

    @Operation(summary = "Gets all links between two cast members")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found a direct link",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any direct link",
            content = @Content)
    })
    @GetMapping(value = "/cast/link", params = {"src", "tgt", "len"})
    public List<List<Object>> getLinkBetweenTwoCastsTraverseMovie(@Parameter(description = "Source cast node")
                                                                        @RequestParam String src,
                                                                  @Parameter(description = "Target cast node")
                                                                        @RequestParam String tgt,
                                                                  @Parameter(description =  "Length of path")
                                                                        @RequestParam Integer len)  {
        return castService.getLinkBetweenTwoCastsTraverseMovie(src, tgt, len);
    }

    @Operation(summary = "Gets the direct link of a movie in between two cast members")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found a direct link",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any direct link",
            content = @Content)
    })
    @GetMapping(value = "/cast/link/direct", params = {"src", "tgt"})
    public List<List<Object>> getDirectLinkBetweenTwoCastsTraverseMovie(@Parameter(description = "Source cast node")
                                                                            @RequestParam String src,
                                                                        @Parameter(description = "Target cast node")
                                                                            @RequestParam String tgt)  {
        return castService.getDirectLinkBetweenTwoCastsTraverseMovie(src, tgt);
    }

    @Operation(summary = "Gets all links between a cast and a movie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found a direct link",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Unable to find any direct link",
            content = @Content)
    })
    @GetMapping(value = "/cast/movie/link", params = {"src", "tgt"})
    public List<List<Object>> getLinkBetweenCastAndMovieTraverseMovie(@Parameter(description = "Source cast node")
                                                                          @RequestParam String src,
                                                                      @Parameter(description = "Target movie node")
                                                                          @RequestParam String tgt,
                                                                      @Parameter(description =  "Length of path")
                                                                          @RequestParam Integer len)   {
        return castService.getLinkBetweenCastAndMovieTraverseMovie(src, tgt, len);
    }
}
