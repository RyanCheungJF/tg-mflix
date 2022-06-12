package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.service.CommentService;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepository implements TigerGraphRepository<Comment, String> {

    @Autowired
    StatementUtil stmtUtil;

    public Optional<List<Comment>> getComment(String uid, String mid) {
        String query = "GET EDGE(User, ?, Comments_On, Movie, ?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, uid);
            stmt.setString(2, mid);
            java.sql.ResultSet rs = stmt.executeQuery();
            ArrayList<Comment> result = new ArrayList<>();
            do {
                while (rs.next()) {
                    result.add(CommentService.reconstructComment(rs));
                }
            } while (!rs.isLast());
            return Optional.of(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Comment> addComment(String uid, String mid, Comment newComment) {
        String query = "INSERT INTO EDGE Comments_On(User, Movie, comment_id, email, movie_id, text) " +
            "VALUES(?, ?, ?, ?, ?, ?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, uid);
            stmt.setString(2, mid);
            stmt.setString(3, newComment.getId());
            stmt.setString(4, newComment.getEmail());
            stmt.setString(5, newComment.getMovie_id());
            stmt.setString(6, newComment.getText());
            stmt.addBatch();
            stmt.executeBatch();
            return Optional.of(newComment);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<String> deleteComment(String uid, String mid) {
        String query = "DELETE EDGE(User, ?, Comments_On, Movie, ?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, uid);
            stmt.setString(2, mid);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                rs.next();
                // First parameter specifies how many edges are deleted. If zero, means edge isn't in graph
                if (rs.getObject(1).equals(0)) {
                    return Optional.of(String.format("No edge from user %s to movie %s is deleted", uid, mid));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(rs.getObject(2)).append(" edge from user ").append(uid).append(" deleted");
                return Optional.of(sb.toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
