/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nikitsenka.bankjava;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import javax.sql.DataSource;
import java.io.File;

public final class PostgresqlServerExtension implements BeforeAllCallback, AfterAllCallback {

    private static GenericContainer container = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromFile("Dockerfile", new File("docker/db.dockerfile"))
                    .withFileFromFile("CreateDB.sql", new File("CreateDB.sql")))
            .withEnv("POSTGRES_PASSWORD", "test1234")
            .withExposedPorts(5432);

    private HikariDataSource dataSource;

    private JdbcOperations jdbcOperations;

    @Override
    public void afterAll(ExtensionContext context) {
        this.dataSource.close();
        this.container.stop();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        this.container.start();

        this.dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(String.format("jdbc:postgresql://localhost:%d/postgres", container.getMappedPort(5432)))
                .username("postgres")
                .password("test1234")
                .build();

        this.dataSource.setMaximumPoolSize(1);

        this.jdbcOperations = new JdbcTemplate(this.dataSource);
    }

    public JdbcOperations getJdbcOperations() {
        return this.jdbcOperations;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
