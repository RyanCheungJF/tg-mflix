package com.tigergraph.tg.util;

import com.tigergraph.tg.connection.TigerGraphConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class StatementUtil {

    @Autowired
    TigerGraphConfig cfg;

    public StatementUtil() {}

    public java.sql.PreparedStatement prepareStatement(String query) {
        try {
            return cfg.provideConnection().prepareStatement(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
