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

    /**
     * Takes the query we have and calls the connection object to provide a prepared query statement
     *
     * @param query interpreted GSQL query we want to run
     * @return a statement ready to be run
     */
    public java.sql.PreparedStatement prepareStatement(String query) {
        try {
            return tgCfg.provideConnection().prepareStatement(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
