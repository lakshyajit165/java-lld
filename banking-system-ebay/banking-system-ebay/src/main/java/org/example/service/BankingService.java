package org.example.service;

import org.example.model.Account;
import org.example.model.AccountSpending;
import org.example.model.PaymentStatus;
import org.example.model.ScheduledPayment;

import java.time.Instant;
import java.util.*;

public class BankingService {
    private Map<String, Account> accounts;
    private Map<String, ScheduledPayment> scheduledPayments;
    private int paymentIdCounter;

    public BankingService() {
        this.accounts = new HashMap<>();
        this.scheduledPayments = new HashMap<>();
        this.paymentIdCounter = 1;
    }

    // Level 1: Basic Operations
    public boolean createAccount(String accountId, Instant timestamp) {
        if (accounts.containsKey(accountId)) {
            return false;
        }
        accounts.put(accountId, new Account(accountId, timestamp));
        return true;
    }

    public Optional<Long> deposit(String accountId, Instant timestamp, int amount) {
        Account account = accounts.get(accountId);
        if (account == null || amount <= 0) {
            return Optional.empty();
        }

        account.deposit(amount, timestamp);
        return Optional.of(account.getBalance());
    }

    public Optional<Long> transfer(String fromId, String toId, Instant timestamp, int amount) {
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);

        if (fromAccount == null || toAccount == null || amount <= 0) {
            return Optional.empty();
        }

        if (fromAccount.getBalance() < amount) {
            return Optional.empty();
        }

        fromAccount.withdraw(amount, timestamp, toId);
        toAccount.deposit(amount, timestamp);

        return Optional.of(fromAccount.getBalance());
    }

    // Level 2: Ranking
    public List<String> topSpenders(Instant timestamp, int n) {
        List<AccountSpending> spendingList = new ArrayList<>();

        for (Account account : accounts.values()) {
            long totalSpent = account.getTotalOutgoingAmount(timestamp);
            if (totalSpent > 0) {
                spendingList.add(new AccountSpending(account.getAccountId(), totalSpent));
            }
        }

        // Sort by amount descending, then by accountId ascending
        Collections.sort(spendingList, (a, b) -> {
            if (a.amount != b.amount) {
                return Long.compare(b.amount, a.amount);
            }
            return a.accountId.compareTo(b.accountId);
        });

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(n, spendingList.size()); i++) {
            result.add(spendingList.get(i).accountId + "(" + spendingList.get(i).amount + ")");
        }

        return result;
    }

    // Level 3: Scheduled Payments
    public String schedulePayment(String accountId, String targetAccId, Instant timestamp,
                                  int amount, double cashbackPercentage) {
        Account account = accounts.get(accountId);
        Account targetAccount = accounts.get(targetAccId);

        if (account == null || targetAccount == null || amount <= 0) {
            return null;
        }

        String paymentId = "payment" + paymentIdCounter++;
        ScheduledPayment payment = new ScheduledPayment(
                paymentId, accountId, targetAccId, timestamp, amount, cashbackPercentage
        );

        scheduledPayments.put(paymentId, payment);
        account.addScheduledPayment(paymentId);

        return paymentId;
    }

    public String getPaymentStatus(String accountId, Instant timestamp, String paymentId) {
        Account account = accounts.get(accountId);
        // Check if payment exists in the system first
        ScheduledPayment payment = scheduledPayments.get(paymentId);
        if (payment == null) {
            return null;
        }

        // Payment exists, return its status regardless of whether it's still linked to account
        // (it gets unlinked after processing/failure)
        if (account == null) {
            return null;
        }

        return payment.getStatus().toString();
    }

    public void processScheduledPayments(Instant currentTimestamp) {
        List<ScheduledPayment> paymentsToProcess = new ArrayList<>();

        for (ScheduledPayment payment : scheduledPayments.values()) {
            if (payment.getStatus() == PaymentStatus.SCHEDULED &&
                    !payment.getTimestamp().isAfter(currentTimestamp)) {
                paymentsToProcess.add(payment);
            }
        }

        for (ScheduledPayment payment : paymentsToProcess) {
            processPayment(payment, currentTimestamp);
        }
    }

    private void processPayment(ScheduledPayment payment, Instant currentTimestamp) {
        Account fromAccount = accounts.get(payment.getAccountId());
        Account toAccount = accounts.get(payment.getTargetAccountId());

        if (fromAccount.getBalance() < payment.getAmount()) {
            payment.setStatus(PaymentStatus.FAILED);
            fromAccount.removeScheduledPayment(payment.getPaymentId());
            return;
        }

        // Process transfer
        fromAccount.withdraw(payment.getAmount(), currentTimestamp, payment.getTargetAccountId());
        toAccount.deposit(payment.getAmount(), currentTimestamp);

        // Apply cashback
        long cashbackAmount = (long) (payment.getAmount() * payment.getCashbackPercentage() / 100.0);
        if (cashbackAmount > 0) {
            fromAccount.deposit(cashbackAmount, currentTimestamp);
        }

        payment.setStatus(PaymentStatus.PROCESSED);
        fromAccount.removeScheduledPayment(payment.getPaymentId());
    }

    // Level 4: Account Merging
    public void mergeAccounts(String accountId1, String accountId2) {
        Account account1 = accounts.get(accountId1);
        Account account2 = accounts.get(accountId2);

        if (account1 == null || account2 == null) {
            return;
        }

        // Determine which account to keep (lexicographically smaller ID)
        Account keepAccount, removeAccount;
        String keepId, removeId;

        if (accountId1.compareTo(accountId2) < 0) {
            keepAccount = account1;
            removeAccount = account2;
            keepId = accountId1;
            removeId = accountId2;
        } else {
            keepAccount = account2;
            removeAccount = account1;
            keepId = accountId2;
            removeId = accountId1;
        }

        // Merge balances
        keepAccount.mergeBalance(removeAccount.getBalance());

        // Merge transactions
        keepAccount.mergeTransactions(removeAccount.getTransactions());

        // Update all transaction references from removeId to keepId
        for (Account account : accounts.values()) {
            account.updateTransactionReferences(removeId, keepId);
        }

        // Transfer scheduled payments
        for (String paymentId : removeAccount.getScheduledPaymentIds()) {
            ScheduledPayment payment = scheduledPayments.get(paymentId);
            if (payment != null) {
                payment.updateAccountId(keepId);
                keepAccount.addScheduledPayment(paymentId);
            }
        }

        // Update scheduled payments that target the removed account
        for (ScheduledPayment payment : scheduledPayments.values()) {
            if (payment.getTargetAccountId().equals(removeId)) {
                payment.updateTargetAccountId(keepId);
            }
        }

        // Remove the merged account
        accounts.remove(removeId);
    }
}
