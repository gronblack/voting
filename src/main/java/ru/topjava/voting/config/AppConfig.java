package ru.topjava.voting.config;

import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@Configuration
public class AppConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // https://stackoverflow.com/a/44427746
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server tcpServer(@Value("${db.path}") String path,
                            @Value("${db.name}") String name,
                            @Value("${db.port}") String port) throws ServerAcl.AclFormatException, IOException {
        log.info("Start TCP server");

        Properties props = new Properties();
        props.setProperty("server.database.0", path);
        props.setProperty("server.dbname.0", name);
        props.setProperty("server.port", port);

        Server server = new Server();
        server.setLogWriter(slf4jPrintWriter());
        server.setErrWriter(slf4jPrintWriter());
        server.setProperties(props);
        return server;
    }

    private PrintWriter slf4jPrintWriter() {
        return new PrintWriter(new ByteArrayOutputStream()) {
            @Override
            public void println(final String x) {
                log.debug(x);
            }
        };
    }
}
