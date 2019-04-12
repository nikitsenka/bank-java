package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Balance;
import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BankPostgresRepository {

    @Autowired
    private DataSource dataSource;

    public Client createClient(Client client) {
        try (Connection con = getConnection();
             PreparedStatement ps = insertClientStatement(con, client);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int id = rs.getInt(1);
                client.setId(id);
                System.out.println("Created client with id " + id);
            } else {
                throw new RuntimeException("No results returned from query executions");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return client;
    }

    public Transaction createTransaction(Transaction transaction) {
        try (Connection con = getConnection();
             PreparedStatement ps = insertTransactionStatement(con, transaction);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int id = rs.getInt(1);
                transaction.setId(id);
                System.out.println("Created transaction with id " + id);
            } else {
                throw new RuntimeException("No results returned from query executions");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return transaction;
    }

    public Balance getBalance(Integer clientId) {
        Balance balance = new Balance();
        try (Connection con = getConnection();
             PreparedStatement ps = getBalanceStatement(con, clientId);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int amount = rs.getInt(1);
                balance.setBalance(amount);
                System.out.println("Get balance for client with id " + clientId);
            } else {
                throw new RuntimeException("No results returned from query executions");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return balance;
    }

    private PreparedStatement getBalanceStatement(Connection con, Integer clientId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT debit - credit FROM (SELECT COALESCE(sum(amount), 0) AS debit FROM transaction WHERE to_client_id = ? ) a, ( SELECT COALESCE(sum(amount), 0) AS credit FROM transaction WHERE from_client_id = ? ) b;");
        ps.setInt(1, clientId);
        ps.setInt(2, clientId);
        return ps;
    }


    private PreparedStatement insertClientStatement(Connection con, Client client) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO client(name, email, phone) VALUES (?, ?, ?) RETURNING id");
        ps.setString(1, client.getName());
        ps.setString(2, client.getEmail());
        ps.setString(3, client.getPhone());
        return ps;
    }

    private PreparedStatement insertTransactionStatement(Connection con, Transaction transaction) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO transaction(from_client_id, to_client_id, amount) VALUES (?, ?, ?) RETURNING id");
        ps.setInt(1, transaction.getFromClientId());
        ps.setInt(2, transaction.getToClientId());
        ps.setInt(3, transaction.getAmount());
        return ps;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
