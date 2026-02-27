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
            // Fallback to individual properties if DATABASE_URL is not set
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/finance_tracker"); // Default local fallback
            config.setUsername(dbUsername);
            config.setPassword(dbPassword);
            config.setDriverClassName("org.postgresql.Driver");
            return new HikariDataSource(config);
        }

        if (databaseUrl.startsWith("postgres://")) {
            URI dbUri = new URI(databaseUrl);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName("org.postgresql.Driver");
            
            // Render specific pool optimizations
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            return new HikariDataSource(config);
        }

        // If it's already a JDBC URL, use it directly
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }
}
