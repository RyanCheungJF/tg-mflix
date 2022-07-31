package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.Cast;

import java.sql.SQLException;

public class CastUtil {

    public static Cast reconstructCast(java.sql.ResultSet rs) throws SQLException {
        return new Cast(rs.getString("name"));
    }
}
