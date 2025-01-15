package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Balance;
import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BankPostgresRepositoryTest {

    private static PostgreSQLContainer<?> postgreSQLContainer;
    private static DriverManagerDataSource dataSource;

    @BeforeAll
    public static void setUp() {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("bankdb")
                .withUsername("testuser")
                .withPassword("testpass");
        postgreSQLContainer.start();

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
    }

    @AfterAll
    public static void tearDown() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }

    @Test
    public void testClientCreation() {
        Client client = new Client(1, "John Doe", "john.doe@example.com", "1234567890");

        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals("John Doe", client.getName());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals("1234567890", client.getPhone());
    }

    @Test
    public void testBalanceUpdate() {
        Balance balance = new Balance(100);
        balance.setBalance(200);

        assertNotNull(balance);
        assertEquals(200, balance.getBalance());
    }

    @Test
    public void testTransactionFields() {
        Transaction transaction = new Transaction(1, 10, 20, 500);

        assertNotNull(transaction);
        assertEquals(1, transaction.getId());
        assertEquals(10, transaction.getFromClientId());
        assertEquals(20, transaction.getToClientId());
        assertEquals(500, transaction.getAmount());
    }
}