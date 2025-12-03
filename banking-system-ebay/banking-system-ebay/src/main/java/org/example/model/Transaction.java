package org.example.model;

import java.time.Instant;

public class Transaction {
    private TransactionType type;
    private long amount;
    private Instant timestamp;
    private String targetAccountId;

    public Transaction(TransactionType type, long amount, Instant timestamp, String targetAccountId) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.targetAccountId = targetAccountId;
    }

    public void updateTargetAccountId(String newAccountId) {
        this.targetAccountId = newAccountId;
    }

    // Getters
    public TransactionType getType() { return type; }
    public long getAmount() { return amount; }
    public Instant getTimestamp() { return timestamp; }
    public String getTargetAccountId() { return targetAccountId; }
}
