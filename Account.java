import java.util.ArrayList;

public abstract class Account {
    private String accountNumber;
    private double balance;
    private Customer customer;
    private ArrayList<Transaction> transactions;

    public Account(String accountNumber, Customer customer, double initialBalance) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        addTransaction("Initial Deposit", initialBalance);
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }
    public ArrayList<Transaction> getTransactions() { return transactions; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposit", amount);
        }
    }

    protected void deductBalance(double amount) {
        this.balance -= amount;
    }

    public void addTransaction(String type, double amount) {
        transactions.add(new Transaction(type, amount));
    }

    public abstract void withdraw(double amount) throws Exception;
}
