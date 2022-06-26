package com.tigergraph.tg.util;

import com.tigergraph.tg.model.Comment;

import java.sql.SQLException;

public class CommentUtil {

    public static Comment reconstructComment(java.sql.ResultSet rs) throws SQLException {
        return new Comment(rs.getString("comment_id"), rs.getString("email"), rs.getString("movie_id"),
            rs.getString("text"));
    }
}
