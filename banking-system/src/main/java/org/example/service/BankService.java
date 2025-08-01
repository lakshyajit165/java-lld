package org.example.service;

import org.example.commands.Command;
import org.example.models.Account;

import java.util.List;

public class BankService {
    public void executeCommand(Command command) {
        command.execute();
    }
    public Account getAccountById(List<Account> accounts, int accountId) {
        List<Account> filteredAccounts = accounts.stream()
                .filter(account -> account.getId() == accountId)
                .toList();
        return filteredAccounts.getFirst();
    }
}
