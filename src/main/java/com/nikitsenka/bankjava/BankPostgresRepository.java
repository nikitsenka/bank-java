package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Balance;
import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class BankPostgresRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Client createClient(Client client) {
        String sql = "INSERT INTO client(name, email, phone) VALUES (?, ?, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{client.getName(), client.getEmail(), client.getPhone()}, Integer.class);
        client.setId(id);
        return client;
    }

    public Transaction createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transaction(from_client_id, to_client_id, amount) VALUES (?, ?, ?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{
                transaction.getFromClientId(),
                transaction.getToClientId(),
                transaction.getAmount()
        }, Integer.class);
        transaction.setId(id);
        return transaction;
    }

    public Balance getBalance(Integer clientId) {
        String sql = "SELECT (COALESCE(SUM(CASE WHEN to_client_id = ? THEN amount ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN from_client_id = ? THEN amount ELSE 0 END), 0)) AS balance " +
                "FROM transaction";
        Integer balance = jdbcTemplate.queryForObject(sql, new Object[]{clientId, clientId}, Integer.class);
        return new Balance(balance);
    }

}
