package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Balance;
import com.nikitsenka.bankjava.model.Client;
import com.nikitsenka.bankjava.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankService {

    @Autowired
    private BankPostgresRepository repository;

    @Transactional
    public Client newClient(Integer balance) {
        Client client = repository.createClient(new Client(0, "", "", ""));
        repository.createTransaction(new Transaction(0,0, client.getId(), balance));
        return client;
    }
    @Caching(
            evict = {
                    @CacheEvict(value = "balances", key = "#transaction.fromClientId"),
                    @CacheEvict(value = "balances", key = "#transaction.toClientId")
            }
    )
    public Transaction newTransaction(Transaction transaction) {
        return repository.createTransaction(transaction);
    }

    @Cacheable(value = "balances", key = "#clientId")
    public Balance getBalance(Integer clientId) {
        return repository.getBalance(clientId);
    }
}
