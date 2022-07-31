package com.tigergraph.tg.util;

import com.tigergraph.tg.model.edge.CommentsOn;

import java.sql.SQLException;

public class CommentUtil {

    public static CommentsOn reconstructComment(java.sql.ResultSet rs) throws SQLException {
        return new CommentsOn(true, rs.getString("User"), rs.getString("Movie"), rs.getString("comment_id"),
            rs.getString("email"), rs.getString("movie_id"), rs.getString("text"));
    }
}
