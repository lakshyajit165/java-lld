package org.example.commands;

import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.Transaction;
import org.example.models.TransactionType;
import org.example.utils.IdGenerator;

import java.util.ArrayList;

public class CreateAccountCommand implements Command {

    private Bank bank;
    private int accountId;
    private double initialBalance;

    public CreateAccountCommand(Bank bank, int accountId, double initialBalance) {
        this.bank = bank;
        this.accountId = accountId;
        this.initialBalance = initialBalance;
    }

    @Override
    public void execute() {
        Account newAccount = new Account(accountId, initialBalance, new ArrayList<>());
        newAccount.getTransactions().add(new Transaction(IdGenerator.generate(), TransactionType.CREDIT, initialBalance));
        bank.getAccounts().add(newAccount);
        System.out.println("Created new account: " + accountId + " with balance: $" + newAccount.getBalance());
    }
}
