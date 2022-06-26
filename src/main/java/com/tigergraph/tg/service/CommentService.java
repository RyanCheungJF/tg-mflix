package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getComment(String uid, String mid) {
        return commentRepository.getComment(uid, mid).orElse(null);
    }

    public Comment addComment(String uid, String mid, Comment newComment) {
        return commentRepository.addComment(uid, mid, newComment).orElse(null);
    }

    public String deleteComment(String uid, String mid) {
        return commentRepository.deleteComment(uid, mid).orElse("Failed to delete any edge");
    }
}
