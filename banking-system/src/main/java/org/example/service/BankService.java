package org.example.service;

import org.example.commands.Command;
import org.example.exception.AccountNotFoundException;
import org.example.models.Account;
import org.example.models.Transaction;

import java.util.List;

public class BankService {
    public void executeCommand(Command command) {
        command.execute();
    }
    public Account getAccountById(List<Account> accounts, int accountId) throws AccountNotFoundException {
        List<Account> filteredAccounts = accounts.stream()
                .filter(account -> account.getId() == accountId)
                .toList();
        if(filteredAccounts.isEmpty()) {
            throw new AccountNotFoundException("Account ID " + accountId + " not found");
        }
        return filteredAccounts.getFirst();
    }

    public double getAccountBalance(List<Account> accounts, int accountId) throws AccountNotFoundException {
        List<Account> filteredAccounts = accounts.stream()
                .filter(account -> account.getId() == accountId)
                .toList();
        if(filteredAccounts.isEmpty()) {
            throw new AccountNotFoundException("Account ID " + accountId + " not found");
        }
        return filteredAccounts.getFirst().getBalance();
    }

    public List<Transaction> getAllAccountTransactions(List<Account> accounts, int accountId) throws AccountNotFoundException {
        List<Account> filteredAccounts = accounts.stream()
                .filter(account -> account.getId() == accountId)
                .toList();
        if(filteredAccounts.isEmpty()) {
            throw new AccountNotFoundException("Account ID " + accountId + " not found");
        }
        return filteredAccounts.getFirst().getTransactions();
    }

}
