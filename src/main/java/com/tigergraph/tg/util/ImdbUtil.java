package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.Imdb;

import java.sql.SQLException;

public class ImdbUtil {

    public static Imdb reconstructImdb(java.sql.ResultSet rs) throws SQLException {
        return new Imdb(rs.getInt("id"), rs.getDouble("rating"), rs.getInt("votes"));
    }
}
