package com.nikitsenka.bankjava.model;

public class Transaction {
    private Integer id;
    private Integer fromClientId;
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
}
