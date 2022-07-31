package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.Movie;

import java.sql.SQLException;

public class MovieUtil {

    public static Movie reconstructMovie(java.sql.ResultSet rs) throws SQLException {
        return new Movie(rs.getString("id"), rs.getString("plot"), rs.getInt("num_mflix_comments"),
            rs.getString("title"), rs.getString("awards"), rs.getString("lastUpdated"), rs.getInt("tomatoes_meter"),
            rs.getDouble("tomatoes_rating"), rs.getInt("tomatoes_numReviews"));
    }
}
