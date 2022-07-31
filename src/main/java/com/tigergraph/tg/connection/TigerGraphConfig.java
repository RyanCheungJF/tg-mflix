package com.tigergraph.tg.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Sets up the connection with the database
 */
@Configuration
public class TigerGraphConfig {

    private final TigerGraphProperties tgProps;

    public TigerGraphConfig(TigerGraphProperties tgProps) {
        this.tgProps = tgProps;
    }

    /**
     * Tries connecting to the database using JDBC
     *
     * @return a connection object used to make queries to the database
     * @throws SQLException if attempted connection is unsuccessful
     */
    @Bean
    public Connection provideConnection() throws SQLException {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(tgProps.getDriver());
        config.setUsername(tgProps.getUsername());
        config.setPassword(tgProps.getPassword());
        config.addDataSourceProperty("graph", tgProps.getGraphName());

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:tg:http://").append(tgProps.getIpAddr()).append(":").append(tgProps.getPort());
        config.setJdbcUrl(sb.toString());
        try {
            HikariDataSource ds = new HikariDataSource(config);
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
