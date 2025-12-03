package org.example;

import org.example.service.BankingService;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BankingService bank = new BankingService();

        // Level 1: Basic Operations
        Instant now = Instant.now();
        bank.createAccount("acc1", now);
        bank.createAccount("acc2", now.plusSeconds(1));
        bank.deposit("acc1", now.plusSeconds(2), 1000);
        bank.deposit("acc2", now.plusSeconds(3), 500);
        bank.transfer("acc1", "acc2", now.plusSeconds(4), 200);

        // Level 2: Ranking
        List<String> topSpenders = bank.topSpenders(now.plusSeconds(10), 5);
        System.out.println("Top Spenders: " + topSpenders);

        // Level 3: Scheduled Payments
        String paymentId = bank.schedulePayment("acc1", "acc2", now.plusSeconds(15), 100, 5.0);
        System.out.println("Scheduled Payment: " + paymentId);
        System.out.println("Payment Status: " + bank.getPaymentStatus("acc1", now.plusSeconds(10), paymentId));
        bank.processScheduledPayments(now.plusSeconds(20));
        System.out.println("Payment Status After Processing: " + bank.getPaymentStatus("acc1", now.plusSeconds(20), paymentId));

        // Level 4: Account Merging
        bank.createAccount("acc3", now.plusSeconds(25));
        bank.deposit("acc3", now.plusSeconds(26), 300);
        bank.mergeAccounts("acc1", "acc3");
        System.out.println("Accounts merged successfully");
    }
}