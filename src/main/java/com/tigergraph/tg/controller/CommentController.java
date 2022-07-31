package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.edge.CommentsOn;
import com.tigergraph.tg.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Gets a comment specified by user id (source) and movie id (target)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found comment",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = CommentsOn.class))}),
        @ApiResponse(responseCode = "404", description = "Comment does not exist",
            content = @Content)
    })
    @GetMapping(value = "/comment/{uid}/{mid}")
    public List<CommentsOn> getComments(@Parameter(description = "User id", required = true)
                                            @PathVariable("id") String uid,
                                        @Parameter(description = "Movie id", required = true)
                                            @PathVariable("id") String mid) {
        return commentService.getComment(uid, mid);
    }

    @Operation(summary = "Adds a new comment between the specified user id (source) and movie id (target)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment successfully added",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = CommentsOn.class))}),
        @ApiResponse(responseCode = "404", description = "Failed to add comment",
            content = @Content)
    })
    @PostMapping(value = "/comment/{uid}/{mid}")
    public CommentsOn addComment(@Parameter(description = "User id", required = true)
                                     @PathVariable("id") String uid,
                                 @Parameter(description = "Movie id", required = true)
                                    @PathVariable("id") String mid,
                                 @RequestBody CommentsOn newCommentsOn) {
        return commentService.addComment(uid, mid, newCommentsOn);
    }

    @Operation(summary = "Deletes comment between the specified user id (source) and movie id (target)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment successfully deleted",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = CommentsOn.class))}),
        @ApiResponse(responseCode = "404", description = "Failed to delete comment",
            content = @Content)
    })
    @DeleteMapping(value = "/comment/{uid}/{mid}")
    public ResponseEntity<String> deleteComment(@Parameter(description = "User id", required = true)
                                                    @PathVariable("id") String uid,
                                                @Parameter(description = "Movie id", required = true)
                                                    @PathVariable("id") String mid) {
        return commentService.deleteComment(uid, mid);
    }
}
