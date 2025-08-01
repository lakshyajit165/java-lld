package org.example;

import org.example.commands.DepositCommand;
import org.example.commands.TransferCommand;
import org.example.exception.AccountNotFoundException;
import org.example.models.Account;
import org.example.models.Bank;
import org.example.service.BankService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    private static Bank bank;
    private static BankService bankService;
    private static Account acc1, acc2, acc3;

    @BeforeAll
    public static void setup() {
        bank = new Bank();
        bankService = new BankService();
        // create accounts
        acc1 = new Account(1, 25000, new ArrayList<>());
        acc2 = new Account(2, 10000, new ArrayList<>());
        acc3 = new Account(3, 20000, new ArrayList<>());

        List<Account> accounts = Arrays.asList(acc1, acc2, acc3);
        bank.setAccounts(accounts);

    }

    @Test
    public void verifyAccountsCreated() {
        assertEquals(3, bank.getAccounts().size(), "Accounts size mismatch");
    }

    @Test
    public void verifyDeposit() {
        double acc1Balance = acc1.getBalance();
        bankService.executeCommand(new DepositCommand(acc1, 20000));
        double currentAcc1Balance = acc1.getBalance();
        assertEquals(
                20000,
                Math.abs(acc1Balance - currentAcc1Balance),
                "Acc balance doesn't match after deposit");
    }

    @Test
    public void verifyTransferSuccess() {
        double acc1Balance = acc1.getBalance();
        double acc2Balance = acc2.getBalance();
        double transferAmount = 10000;
        bankService.executeCommand(new TransferCommand(
                acc1,
                acc2,
                transferAmount
        ));
        double acc1BalanceDifference = Math.abs(acc1Balance - acc1.getBalance());
        double acc2BalanceDifference = Math.abs(acc2Balance - acc2.getBalance());
        assertEquals(10000, acc1BalanceDifference, "Source acc balance mismatch");
        assertEquals(10000, acc2BalanceDifference, "Destination acc balance mismatch");

    }

    @Test
    public void verifyTransferFailure() {
        double acc1CurrentBalance = acc1.getBalance();
        double transferAmount = acc1CurrentBalance + 10000;
        bankService.executeCommand(new TransferCommand(acc1, acc2, transferAmount));
        assertEquals(acc1CurrentBalance, acc1.getBalance(), "Balance mismatch after failed transfer");

    }

    @Test
    public void testGetAccountId_ThrowsExceptionForInvalidId() {
        AccountNotFoundException accountNotFoundException = assertThrows(AccountNotFoundException.class, () -> {
            bankService.getAccountById(bank.getAccounts(), 9999);
        });
        assertEquals("Account ID 9999 not found", accountNotFoundException.getMessage());
    }
}
