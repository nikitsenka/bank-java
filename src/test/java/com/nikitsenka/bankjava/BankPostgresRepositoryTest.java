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
        Balance secondClientBalance = repository.getBalance(100);
        assertEquals(Integer.valueOf(0), secondClientBalance.getBalance());
    }

}