package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.service.CommentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // crud for relationships

    // simple GET
    // syntax specified requires a source and a target vertex, (uid = user id, mid  = movie id)
    // since comments only goes from user -comments-> movie, we specify it in such a way
    @GetMapping("/comment/{uid}/{mid}")
    public List<Comment> getComments(@PathVariable("uid") String uid, @PathVariable("mid") String mid) {
        return commentService.getComment(uid, mid);
    }

    // simple POST, no UPDATE as tg treats them the same, as an 'UPSERT' query
    @PostMapping("/comment/{uid}/{mid}")
    public Comment addComment(@PathVariable("uid") String uid, @PathVariable("mid") String mid,
                              @RequestBody Comment newComment) {
        return commentService.addComment(uid, mid, newComment);
    }

    // simple DELETE
    @DeleteMapping("/comment/{uid}/{mid}")
    public Map<String, String> deleteComment(@PathVariable("uid") String uid, @PathVariable("mid") String mid) {
        return Collections.singletonMap("response", commentService.deleteComment(uid, mid));
    }
}
