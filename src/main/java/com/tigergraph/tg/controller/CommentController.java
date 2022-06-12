package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    // crud for relationships

    // simple GET
    // syntax specified requires a source and a target vertex, (uid = user id, mid  = movie id)
    // since comments only goes from user -comments-> movie, we specify it in such a way
    @GetMapping("/comment/{uid}/{mid}")
    public List<Comment> getComments(@PathVariable("uid") String uid, @PathVariable("mid") String mid) {
        return commentService.getComment(uid, mid);
    }

    // simple POST
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
