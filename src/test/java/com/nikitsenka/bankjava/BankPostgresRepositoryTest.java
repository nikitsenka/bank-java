package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Balance;
import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class BankPostgresRepositoryTest {

    private static PostgreSQLContainer<?> postgresContainer;
    private static DataSource dataSource;
    private static BankPostgresRepository repository;

    @BeforeAll
    public static void setUp() {
        // Start the PostgreSQLContainer
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        postgresContainer.start();

        // Create the data source
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(postgresContainer.getJdbcUrl());
        ds.setUsername(postgresContainer.getUsername());
        ds.setPassword(postgresContainer.getPassword());
        dataSource = ds;

        // Initialize the repository
        repository = new BankPostgresRepository();
        repository.setDataSource(dataSource);

        // Initialize the database schema
        initializeDatabase();
    }

    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
    }

    private static void initializeDatabase() {
        String createClientTableSQL = "CREATE TABLE client(id SERIAL PRIMARY KEY NOT NULL, name VARCHAR(20), email VARCHAR(20), phone VARCHAR(20));";
        String createTransactionTableSQL = "CREATE TABLE transaction(id SERIAL PRIMARY KEY NOT NULL, from_client_id INTEGER, to_client_id INTEGER, amount INTEGER);";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(createClientTableSQL);
             PreparedStatement stmt2 = conn.prepareStatement(createTransactionTableSQL)) {
            stmt1.execute();
            stmt2.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    @Test
    public void testCreateClient() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("1234567890");

        Client createdClient = repository.createClient(client);

        assertNotNull(createdClient.getId());
        assertEquals("John Doe", createdClient.getName());
        assertEquals("john.doe@example.com", createdClient.getEmail());
        assertEquals("1234567890", createdClient.getPhone());
    }

    @Test
    public void testCreateTransaction() {
        // First, create two clients
        Client client1 = new Client();
        client1.setName("Alice");
        client1.setEmail("alice@example.com");
        client1.setPhone("1111111111");
        client1 = repository.createClient(client1);

        Client client2 = new Client();
        client2.setName("Bob");
        client2.setEmail("bob@example.com");
        client2.setPhone("2222222222");
        client2 = repository.createClient(client2);

        // Now create a transaction between them
        Transaction transaction = new Transaction();
        transaction.setFromClientId(client1.getId());
        transaction.setToClientId(client2.getId());
        transaction.setAmount(100);

        Transaction createdTransaction = repository.createTransaction(transaction);

        assertNotNull(createdTransaction.getId());
        assertEquals(client1.getId(), createdTransaction.getFromClientId());
        assertEquals(client2.getId(), createdTransaction.getToClientId());
        assertEquals(100, createdTransaction.getAmount());
    }

    @Test
    public void testGetBalance() {
        // Create clients
        Client client1 = new Client();
        client1.setName("Alice");
        client1.setEmail("alice@example.com");
        client1.setPhone("1111111111");
        client1 = repository.createClient(client1);

        Client client2 = new Client();
        client2.setName("Bob");
        client2.setEmail("bob@example.com");
        client2.setPhone("2222222222");
        client2 = repository.createClient(client2);

        // Create transactions
        // Bob sends 200 to Alice
        Transaction transaction1 = new Transaction();
        transaction1.setFromClientId(client2.getId());
        transaction1.setToClientId(client1.getId());
        transaction1.setAmount(200);
        repository.createTransaction(transaction1);

        // Alice sends 50 to Bob
        Transaction transaction2 = new Transaction();
        transaction2.setFromClientId(client1.getId());
        transaction2.setToClientId(client2.getId());
        transaction2.setAmount(50);
        repository.createTransaction(transaction2);

        // Check balances
        Balance balanceAlice = repository.getBalance(client1.getId());
        Balance balanceBob = repository.getBalance(client2.getId());

        assertEquals(150, balanceAlice.getBalance()); // Alice received 200, sent 50
        assertEquals(-150, balanceBob.getBalance()); // Bob sent 200, received 50
    }
}