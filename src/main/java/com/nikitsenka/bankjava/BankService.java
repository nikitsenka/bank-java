package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    private BankPostgresRepository repository;

    public Client newClient(Integer balance) {
        Client client = repository.saveClient(new Client(0, "", "", ""));
        repository.createTransaction(new Transaction(0,0, client.getId(), balance));
        return client;
    }
}
