package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getComment(String uid, String mid) {
        return commentRepository.getComment(uid, mid).orElse(null);
    }

    public Comment addComment(String uid, String mid, Comment newComment) {
        return commentRepository.addComment(uid, mid, newComment).orElse(null);
    }

    public String deleteComment(String uid, String mid) {
        return commentRepository.deleteComment(uid, mid).orElse("Failed to delete any edge");
    }

    // returns in order of:
    // user id, movie pk, text, comment_id, movie_id, email
    public static Comment reconstructComment(java.sql.ResultSet rs) throws SQLException {
        return new Comment(rs.getString(4), rs.getString(6), rs.getString(5), rs.getString(3));
    }
}
