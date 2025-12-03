package org.example.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Account {
    private String accountId;
    private long balance;
    private Instant creationTimestamp;
    private List<Transaction> transactions;
    private Set<String> scheduledPaymentIds;

    public Account(String accountId, Instant creationTimestamp) {
        this.accountId = accountId;
        this.balance = 0;
        this.creationTimestamp = creationTimestamp;
        this.transactions = new ArrayList<>();
        this.scheduledPaymentIds = new HashSet<>();
    }

    public void deposit(long amount, Instant timestamp) {
        balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount, timestamp, null));
    }

    public void withdraw(long amount, Instant timestamp, String targetAccountId) {
        balance -= amount;
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, timestamp, targetAccountId));
    }

    public long getTotalOutgoingAmount(Instant timestamp) {
        long total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.WITHDRAWAL &&
                    !transaction.getTimestamp().isAfter(timestamp)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public void addScheduledPayment(String paymentId) {
        scheduledPaymentIds.add(paymentId);
    }

    public void removeScheduledPayment(String paymentId) {
        scheduledPaymentIds.remove(paymentId);
    }

    public boolean hasScheduledPayment(String paymentId) {
        return scheduledPaymentIds.contains(paymentId);
    }

    public void mergeBalance(long otherBalance) {
        this.balance += otherBalance;
    }

    public void mergeTransactions(List<Transaction> otherTransactions) {
        this.transactions.addAll(otherTransactions);
    }

    public void updateTransactionReferences(String oldAccountId, String newAccountId) {
        for (Transaction transaction : transactions) {
            if (oldAccountId.equals(transaction.getTargetAccountId())) {
                transaction.updateTargetAccountId(newAccountId);
            }
        }
    }

    // Getters
    public String getAccountId() { return accountId; }
    public long getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }
    public Set<String> getScheduledPaymentIds() { return scheduledPaymentIds; }
}
