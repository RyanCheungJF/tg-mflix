package com.tigergraph.tg.connection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("tigergraph.datasource")
public class TigerGraphProperties {

    private String ipAddr;
    private String port;
    private String username;
    private String password;
    private String graphName;
    private String driver;

}
