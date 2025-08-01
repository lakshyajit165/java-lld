package org.example.commands;

import org.example.models.Account;
import org.example.models.Transaction;
import org.example.models.TransactionType;
import org.example.utils.IdGenerator;

public class TransferCommand implements Command {

    private Account sourceAccount;
    private Account destinationAccount;
    private double amount;

    public TransferCommand(Account sourceAccount, Account destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    @Override
    public void execute() {
        if(sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient funds in source account");
            return;
        }
        // deduct amount and add it to source acc's txn
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        sourceAccount.getTransactions().add(new Transaction(
                IdGenerator.generate(),
                TransactionType.DEBIT,
                amount
        ));
        // add amount and record txn in destination account
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        destinationAccount.getTransactions().add(new Transaction(
                IdGenerator.generate(),
                TransactionType.CREDIT,
                amount
        ));
        System.out.println("Amount $ " + amount + " transferred from source account: "
                + sourceAccount.getId() + " to destination account: " + destinationAccount.getId());
    }
}
