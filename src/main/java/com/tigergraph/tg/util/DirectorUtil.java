package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.Director;

import java.sql.SQLException;

public class DirectorUtil {

    public static Director reconstructDirector(java.sql.ResultSet rs) throws SQLException {
        return new Director(rs.getString("name"));
    }
}
