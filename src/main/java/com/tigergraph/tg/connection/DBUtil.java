package com.tigergraph.tg.connection;

import com.tigergraph.jdbc.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class DBUtil {

    @Value("${spring.datasource.ipAddr}")
    private String ipAddr;
    @Value("${spring.datasource.port}")
    private String port;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.graphName}")
    private String graphName;

    public DBUtil() {}

    public Connection provideConnection() throws SQLException {
        Properties properties = new Properties();

        properties.put("username", username);
        properties.put("password", password);
        properties.put("graph", graphName);

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:tg:http://").append(ipAddr).append(":").append(port);

        try {
            com.tigergraph.jdbc.Driver driver = new Driver();
            try {
                Connection connection = driver.connect(sb.toString(), properties);
                return connection;
            } catch (NullPointerException | SQLException err) {
                err.printStackTrace();
            }
        } catch (NullPointerException | SQLException err) {
            err.printStackTrace();
        }
        throw new SQLException("Failed to initialize connection");
    }
}
