package com.dp.ishare.properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories("com.dp.ishare.dao")
public class JDBCConfig {
}
