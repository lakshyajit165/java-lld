package org.example.utils;

import org.example.models.Transaction;

import java.util.Comparator;

public class TransactionDateComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t2.getCreatedAt().compareTo(t1.getCreatedAt());
    }
}
