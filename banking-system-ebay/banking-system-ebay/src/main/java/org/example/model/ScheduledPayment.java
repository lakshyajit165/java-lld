package org.example.model;

import java.time.Instant;

public class ScheduledPayment {
    private String paymentId;
    private String accountId;
    private String targetAccountId;
    private Instant timestamp;
    private int amount;
    private double cashbackPercentage;
    private PaymentStatus status;

    public ScheduledPayment(String paymentId, String accountId, String targetAccountId,
                            Instant timestamp, int amount, double cashbackPercentage) {
        this.paymentId = paymentId;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.cashbackPercentage = cashbackPercentage;
        this.status = PaymentStatus.SCHEDULED;
    }

    public void updateAccountId(String newAccountId) {
        this.accountId = newAccountId;
    }

    public void updateTargetAccountId(String newAccountId) {
        this.targetAccountId = newAccountId;
    }

    // Getters and Setters
    public String getPaymentId() { return paymentId; }
    public String getAccountId() { return accountId; }
    public String getTargetAccountId() { return targetAccountId; }
    public Instant getTimestamp() { return timestamp; }
    public int getAmount() { return amount; }
    public double getCashbackPercentage() { return cashbackPercentage; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
}
