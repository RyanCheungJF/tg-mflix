package com.tigergraph.tg.repository;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.util.CommentUtil;
import com.tigergraph.tg.util.StatementUtil;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepository implements TigerGraphRepository<Comment, String> {

    private final String INTERPRETED = "run interpreted(u=?)";
    private final StatementUtil stmtUtil;

    public CommentRepository(StatementUtil stmtUtil) {
        this.stmtUtil = stmtUtil;
    }

    public Optional<List<Comment>> getComment(String uid, String mid) {
        String query = "GET EDGE(User, ?, Comments_On, Movie, ?)";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(query)) {
            stmt.setString(1, uid);
            stmt.setString(2, mid);
            java.sql.ResultSet rs = stmt.executeQuery();
            ArrayList<Comment> result = new ArrayList<>();
            while (rs.next()) {
                result.add(CommentUtil.reconstructComment(rs));
            }
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

    public Optional<List<Comment>> getCommentsByUser(String id) {
        /**
         can't initialize a set {u} for edges, it only works for nodes
         to instead get edges, we can use accumulators
         we use global accums (specified with @@) which is made available to any node/ edge traversed in the query
         here we do not care about the target vertex, so it has no type at the end, but just has an alias :t
         we want Comments_On edges only, so we specify it with a query Comments_On:e
         we then add it to our list of edges and return it after, we do this for every outgoing edge from user
         */
        String queryBody = "INTERPRET QUERY (VERTEX<User> u) FOR GRAPH internship {\n"
            + "ListAccum<edge> @@edgeList;\n"
            + "start = {u};\n"
            + "result = SELECT t FROM start:s -(Comments_On:e)- :t ACCUM @@edgeList += e;\n"
            + "PRINT @@edgeList;\n"
            + "}\n";
        try (java.sql.PreparedStatement stmt = stmtUtil.prepareStatement(INTERPRETED)) {
            stmt.setString(1, id);
            stmt.setString(2, queryBody); // The query body is passed as a parameter.
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                ArrayList<Comment> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(CommentUtil.reconstructComment(rs));
                }
                return Optional.of(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
