# Bank Management System

A desktop banking simulation application developed in Java to manage customer accounts, process deposits and withdrawals, perform intra-bank funds transfers, and maintain persistent records. 

Built for the **CSE 282 (Section: 4)** Lab Project at the Department of Computer Science and Engineering.

---

## 👥 Team Members

| SL | Member Name | Student ID | Role / Contribution |
|:---|:---|:---|:---|
| 1 | **Tasmeoul Miyad (Team Lead)** | 2024100000181 | Member 1: Customer Model & Abstract Account Class (`Customer.java`, `Account.java`) |
| 2 | **Md Shawon Chowdhury setu** | 2024100000238 | Member 2: Account Subclasses & Polymorphism (`SavingsAccount.java`, `CurrentAccount.java`) |
| 3 | **Md Farhatul Hasan Rabbi** | 2024100000411 | Member 3: Transaction Model & History Logging (`Transaction.java`) |
| 4 | **Miraj Masud Munna** | 2024100000521 | Member 4: Bank Business Logic & File Persistence (`Bank.java`) |
| 5 | **Rifat Ahamed** | 2024000000064 | Member 5: Java Swing GUI & Entry Point (`BankGUI.java`, `Main.java`) |

---

## 🚀 Key Features

* **Create Accounts**: Open either Savings or Current accounts with customer details and an initial balance.
* **Deposit Funds**: Safely credit money to any active account.
* **Withdraw Funds**: Deduct funds based on account-specific rules (minimum balance enforcement or overdraft credit limits).
* **Inter-Account Transfers**: Transfer balances between valid accounts with transactional validation.
* **Search Records**: Look up registered accounts by Account Number or customer name.
* **Transaction Ledger**: Track deposits, withdrawals, and transfers with timestamped history logs.
* **Data Persistence**: Automatically load and save accounts to a plain text flat file (`accounts.txt`) using the Java `File` API.
* **Exception Handling**: Built-in `try-catch` validation to prevent crashes from non-numerical inputs or insufficient funds.

---

## 🏛️ OOP Principles Implemented

* **Encapsulation**: Account numbers, balances, and customer data are kept `private`, accessible only via explicit getters, setters, and validated methods.
* **Abstraction**: An abstract class `Account` sets the structural template with an abstract withdrawal method `public abstract void withdraw(double amount)`.
* **Inheritance**: `SavingsAccount` and `CurrentAccount` extend the abstract `Account` parent class to reuse common properties without duplication.
* **Polymorphism**: Both account subclasses override `withdraw()` with specialized behaviors:
  * `SavingsAccount`: Blocks withdrawals if the remaining balance falls below a required minimum threshold.
  * `CurrentAccount`: Permits withdrawals below zero balance up to a designated overdraft credit ceiling.

---

## 📁 Repository Structure

```text
bank-management-system/
├── accounts.txt            # Persistent flat-file storage
├── README.md               # Project documentation
└── src/
    ├── Customer.java       # Member 1: Customer model
    ├── Account.java        # Member 1: Abstract account template
    ├── SavingsAccount.java # Member 2: Savings account subclass
    ├── CurrentAccount.java # Member 2: Current account subclass
    ├── Transaction.java    # Member 3: Transaction ledger model
    ├── Bank.java           # Member 4: Bank controller & File I/O
    ├── BankGUI.java        # Member 5: Java Swing GUI interface
    └── Main.java           # Member 5: Application entry point
