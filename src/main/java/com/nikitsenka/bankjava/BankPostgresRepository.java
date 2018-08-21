package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class BankPostgresRepository {

    @Value("${POSTGRES_HOST:localhost}")
    private String host;

    @Value("${postgres.db.user:postgres}")
    private String user;

    @Value("${postgres.db.password:test1234}")
    private String password;

    @Value("${postgres.db.name:postgres}")
    private String name;

    public Client saveClient(Client client) {
        try {
            Connection con = getConnection();
            ResultSet resultSet;
            if (client.getId() == 0) {
                String sql = "INSERT INTO client(name, email, phone) VALUES (?, ?, ?) RETURNING id";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, client.getName());
                ps.setString(2, client.getEmail());
                ps.setString(3, client.getPhone());
                resultSet = ps.executeQuery();

            } else {
                String sql = "UPDATE client SET name = $2, email = $3, phone = $4 WHERE id = $1 RETURNING id";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, client.getId());
                ps.setString(2, client.getName());
                ps.setString(3, client.getEmail());
                ps.setString(4, client.getPhone());
                resultSet = ps.executeQuery();
            }
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                client.setId(id);
                System.out.println("Created/Updated client with id " + id);
            } else {
                throw new RuntimeException("No results returned from query executions");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return client;
    }

    public Transaction createTransaction(Transaction transaction) {
        //TODO
        return null;
    }

    private Connection getConnection() {
        String url = "jdbc:postgresql://" + host + "/" + name;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("ssl", "disable");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.out.println("Error open DB connection.");
            System.out.println(e);
        }
        return conn;
    }
}
