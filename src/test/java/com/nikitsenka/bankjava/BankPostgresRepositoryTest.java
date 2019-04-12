package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Balance;
import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.jdbc.core.JdbcOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankPostgresRepositoryTest {

    @RegisterExtension
    static final PostgresqlServerExtension SERVER = new PostgresqlServerExtension();

    private BankPostgresRepository repository = new BankPostgresRepository();


    @BeforeEach
    void createTable() {
        repository.setDataSource(SERVER.getDataSource());
        getJdbcOperations().execute("CREATE TABLE test ( value INTEGER )");
        getJdbcOperations().execute("CREATE TABLE client(id SERIAL PRIMARY KEY NOT NULL, name VARCHAR(20), email VARCHAR(20), phone VARCHAR(20));");
        getJdbcOperations().execute("CREATE TABLE transaction(id SERIAL PRIMARY KEY NOT NULL, from_client_id INTEGER, to_client_id INTEGER, amount INTEGER)");
    }

    @AfterEach
    void dropTable() {
        getJdbcOperations().execute("DROP TABLE test");
        getJdbcOperations().execute("DROP TABLE client");
        getJdbcOperations().execute("DROP TABLE transaction");
    }

    @Test
    void createClient() {

        Client client = repository.createClient(new Client(0, "", "", ""));

        assertEquals(Integer.valueOf(1), client.getId());
    }

    @Test
    void createTransaction() {
        Transaction transaction = repository.createTransaction(new Transaction(0, 1, 2, 100));

        assertEquals(Integer.valueOf(1), transaction.getId());
    }

    @Test
    void getBalance() {
        Client firstClient = repository.createClient(new Client(0, "", "", ""));
        Client secondClient = repository.createClient(new Client(0, "", "", ""));
        repository.createTransaction(new Transaction(0, firstClient.getId(), secondClient.getId(), 100));
        Balance firstClientBalance = repository.getBalance(firstClient.getId());
        assertEquals(Integer.valueOf(-100), firstClientBalance.getBalance());
        Balance secondClientBalance = repository.getBalance(secondClient.getId());
        assertEquals(Integer.valueOf(100), secondClientBalance.getBalance());
    }

    public JdbcOperations getJdbcOperations() {
        JdbcOperations jdbcOperations = SERVER.getJdbcOperations();

        if (jdbcOperations == null) {
            throw new IllegalStateException("JdbcOperations not yet initialized");
        }

        return jdbcOperations;
    }


}