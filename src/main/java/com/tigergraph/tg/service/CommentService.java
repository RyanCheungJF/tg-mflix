package com.tigergraph.tg.service;

import com.tigergraph.tg.model.edge.CommentsOn;
import com.tigergraph.tg.repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentsOn> getComment(String uid, String mid) {
        return commentRepository.getComment(uid, mid).orElse(null);
    }

    public CommentsOn addComment(String uid, String mid, CommentsOn newCommentsOn) {
        return commentRepository.addComment(uid, mid, newCommentsOn).orElse(null);
    }

    public ResponseEntity<String> deleteComment(String uid, String mid) {
        return commentRepository.deleteComment(uid, mid).orElse(null);
    }
}
