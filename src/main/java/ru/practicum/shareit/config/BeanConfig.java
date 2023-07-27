package ru.practicum.shareit.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
@Profile("test")
public class BeanConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("test")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
