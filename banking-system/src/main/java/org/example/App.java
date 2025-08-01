package org.example;

import org.example.commands.CreateAccountCommand;
import org.example.commands.DepositCommand;
import org.example.commands.TransferCommand;
import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.Transaction;
import org.example.models.TransactionType;
import org.example.service.BankService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Bank bank = new Bank();
        BankService bankService = new BankService();

        bankService.executeCommand(new CreateAccountCommand(bank, 1, 5000));
        bankService.executeCommand(new CreateAccountCommand(bank, 2, 10000));


        bankService.executeCommand(new DepositCommand(
                bankService.getAccountById(bank.getAccounts(), 1), 15000
        ));

        bankService.executeCommand(new TransferCommand(
                bankService.getAccountById(bank.getAccounts(), 1),
                bankService.getAccountById(bank.getAccounts(), 2),
                8000
        ));

        for(Account acc : bank.getAccounts()) {
            double totalCredit = acc.getTransactions()
                    .stream()
                    .filter(transaction -> transaction.getTransactionType() == TransactionType.CREDIT)
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            double totalDebit = acc.getTransactions()
                    .stream()
                    .filter(transaction -> transaction.getTransactionType() == TransactionType.DEBIT)
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            System.out.println("Account ID: " + acc.getId());
            System.out.println("  ➕ Total Credit: $" + totalCredit);
            System.out.println("  ➖ Total Debit : $" + totalDebit);
            System.out.println("  🔄 Final Balance: $" + acc.getBalance());
        }
    }
}
