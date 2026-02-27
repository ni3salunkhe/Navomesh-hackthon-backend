package com.FT.FinanceTracker.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_USERNAME:}")
    private String dbUsername;

    @Value("${DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() throws URISyntaxException {
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/finance_tracker");
            if (dbUsername != null && !dbUsername.isEmpty()) config.setUsername(dbUsername);
            if (dbPassword != null && !dbPassword.isEmpty()) config.setPassword(dbPassword);
            config.setDriverClassName("org.postgresql.Driver");
            return new HikariDataSource(config);
        }

        if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            URI dbUri = new URI(databaseUrl);
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
            if (dbUri.getQuery() != null) {
                dbUrl += "?" + dbUri.getQuery();
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            
            if (dbUri.getUserInfo() != null) {
                String[] userInfo = dbUri.getUserInfo().split(":");
                config.setUsername(userInfo[0]);
                if (userInfo.length > 1) {
                    config.setPassword(userInfo[1]);
                }
            } else {
                if (dbUsername != null && !dbUsername.isEmpty()) config.setUsername(dbUsername);
                if (dbPassword != null && !dbPassword.isEmpty()) config.setPassword(dbPassword);
            }
            
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            return new HikariDataSource(config);
        }

        // If it's already a JDBC URL, use it directly
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        if (dbUsername != null && !dbUsername.isEmpty()) config.setUsername(dbUsername);
        if (dbPassword != null && !dbPassword.isEmpty()) config.setPassword(dbPassword);
        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }
}
