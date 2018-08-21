package com.nikitsenka.bankjava;

import com.nikitsenka.bankjava.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    @Autowired
    private BankService service;

    @PostMapping("/client/new/{balance}")
    public Client newClient(@PathVariable Integer balance){
        return service.newClient(balance);
    }

}
