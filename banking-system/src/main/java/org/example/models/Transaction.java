package org.example.models;

import java.util.Date;

public class Transaction {
    private int id;
    private TransactionType transactionType;
    private double amount;
    private Date createdAt;

    public Transaction(int id, TransactionType transactionType, double amount) {
        this.id = id;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}