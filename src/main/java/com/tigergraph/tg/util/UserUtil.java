package com.tigergraph.tg.util;

import com.tigergraph.tg.model.vertex.User;

import java.sql.SQLException;

public class UserUtil {

    public static User reconstructUser(java.sql.ResultSet rs) throws SQLException {
        return new User(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
    }
}
