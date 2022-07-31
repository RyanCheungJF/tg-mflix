package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.Genre;

import java.sql.SQLException;

public class GenreUtil {

    public static Genre reconstructGenre(java.sql.ResultSet rs) throws SQLException {
        return new Genre(rs.getString("name"));
    }
}
