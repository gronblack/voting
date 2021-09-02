package ru.topjava.voting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.topjava.voting.util.AppUtil;
import ru.topjava.voting.util.JsonUtil;

import java.sql.SQLException;

@Configuration
public class AppConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return AppUtil.createTcpServer();
    }

    @Autowired
    public void storeObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.setMapper(objectMapper);
    }
}
