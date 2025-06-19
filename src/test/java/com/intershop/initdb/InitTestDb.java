package com.intershop.initdb;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@ActiveProfiles("test")
public class InitTestDb {

    protected Long cartId = 2L;
    protected Long itemInCartId = 2L;
    protected Long orderId = 1L;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setupSchemaAndData() throws Exception {
        executeSqlFromFile("schema.sql");
        executeSqlFromFile("data.sql");
    }

    private void executeSqlFromFile(String filename) throws Exception {
        String sql;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        }

        for (String statement : sql.split(";")) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                databaseClient
                        .sql(trimmed)
                        .then()
                        .block();
            }
        }
    }
}
