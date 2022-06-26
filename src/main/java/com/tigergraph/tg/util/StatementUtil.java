package com.tigergraph.tg.util;

import com.tigergraph.tg.connection.TigerGraphConfig;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class StatementUtil {

    private final TigerGraphConfig tgCfg;

    public StatementUtil(TigerGraphConfig tgCfg) {
        this.tgCfg = tgCfg;
    }

    public java.sql.PreparedStatement prepareStatement(String query) {
        try {
            return tgCfg.provideConnection().prepareStatement(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
