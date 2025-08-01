package org.example.commands;

import org.example.models.Account;
import org.example.models.Transaction;
import org.example.models.TransactionType;
import org.example.utils.IdGenerator;

public class DepositCommand implements Command{

    private Account account;
    private double amount;

    public DepositCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        account.setBalance(account.getBalance() + amount);
        account.getTransactions().add(new Transaction(
                IdGenerator.generate(),
                TransactionType.CREDIT,
                amount
        ));
        System.out.println("Deposited $" + amount + " to account " + account.getId() + " | Available balance: $" + account.getBalance());
    }
}
