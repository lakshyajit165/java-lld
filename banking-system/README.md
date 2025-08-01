# 🏦 Banking System - Java (Command Pattern)

This is a simple **Banking System** implemented in **Java**, demonstrating object-oriented design, the **Command Pattern**, and custom exception handling. It allows creation of accounts, deposits, transfers, and transaction tracking.

---

## 📌 Features

- ✅ Create new accounts with initial balance
- ✅ Deposit money
- ✅ Transfer money between accounts
- ✅ Maintain full transaction history
- ✅ View total credit, debit, and balance
- ✅ Graceful handling of invalid account operations

---

## 🧠 Design Pattern Used: Command Pattern

The **Command Pattern** encapsulates operations as command objects, promoting loose coupling between senders and receivers of requests.

### 🔹 Interface: `Command`

```java
public interface Command {
    void execute();
}
```

### 🔹 Command Implementations

- `CreateAccountCommand`
- `DepositCommand`
- `TransferCommand`

Each command encapsulates its operation and uses the `execute()` method to carry it out.

### 🔹 Invoker: `BankService`

```java
public class BankService {
    public void executeCommand(Command command) {
        command.execute();
    }

    public Account getAccountById(List<Account> accounts, int id) throws AccountNotFoundException { ... }
}
```

---

## 💥 Custom Exception

### `AccountNotFoundException`

Thrown when an operation references a non-existent account.

```java
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
```

---

## 📦 Project Structure

```
src/
├── main/java/org/example/
│   ├── App.java
│   ├── commands/
│   │   ├── Command.java
│   │   ├── CreateAccountCommand.java
│   │   ├── DepositCommand.java
│   │   └── TransferCommand.java
│   ├── exception/
│   │   └── AccountNotFoundException.java
│   ├── models/
│   │   ├── Account.java
│   │   ├── Bank.java
│   │   ├── Transaction.java
│   │   └── TransactionType.java
│   ├── service/
│   │   └── BankService.java
│   └── utils/
│       └── IdGenerator.java
└── test/java/org/example/
    └── BankServiceTest.java
```
