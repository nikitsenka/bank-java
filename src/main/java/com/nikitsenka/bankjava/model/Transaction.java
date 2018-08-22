package com.nikitsenka.bankjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
    private Integer id;
    @JsonProperty("from_client_id")
    private Integer fromClientId;
    @JsonProperty("to_client_id")
    private Integer toClientId;
    private Integer amount;

    public Transaction() {
    }

    public Transaction(Integer id, Integer fromClientId, Integer toClientId, Integer amount) {
        this.id = id;
        this.fromClientId = fromClientId;
        this.toClientId = toClientId;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Integer getFromClientId() {
        return fromClientId;
    }

    public Integer getToClientId() {
        return toClientId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFromClientId(Integer fromClientId) {
        this.fromClientId = fromClientId;
    }

    public void setToClientId(Integer toClientId) {
        this.toClientId = toClientId;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
