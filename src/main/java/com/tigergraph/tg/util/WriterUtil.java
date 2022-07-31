package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.Writer;

import java.sql.SQLException;

public class WriterUtil {

    public static Writer reconstructWriter(java.sql.ResultSet rs) throws SQLException {
        return new Writer(rs.getString("name"));
    }
}
