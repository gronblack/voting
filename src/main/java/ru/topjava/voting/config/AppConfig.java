package ru.topjava.voting.config;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

@Configuration
public class AppConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // https://stackoverflow.com/a/44427746
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public Server tcpServer(@Value("${db.path}") String path,
//                            @Value("${db.name}") String name,
//                            @Value("${db.port}") String port) throws ServerAcl.AclFormatException, IOException {
//        log.info("Start TCP server");
//
//        Properties props = new Properties();
//        props.setProperty("server.database.0", path);
//        props.setProperty("server.dbname.0", name);
//        props.setProperty("server.port", port);
//
//        Server server = new Server();
//        server.setLogWriter(slf4jPrintWriter());
//        server.setErrWriter(slf4jPrintWriter());
//        server.setProperties(props);
//        return server;
//    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

//    private PrintWriter slf4jPrintWriter() {
//        return new PrintWriter(new ByteArrayOutputStream()) {
//            @Override
//            public void println(final String x) {
//                log.debug(x);
//            }
//        };
//    }
}
