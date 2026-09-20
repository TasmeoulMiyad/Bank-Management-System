public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, Customer customer, double initialBalance, double overdraftLimit) {
        super(accountNumber, customer, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Withdrawal amount must be greater than zero.");
        }
        if (getBalance() - amount >= -overdraftLimit) {
            deductBalance(amount);
            addTransaction("Withdrawal", amount);
        } else {
            throw new Exception("Overdraft ceiling exceeded! Max allowed overdraft is $" + overdraftLimit);
        }
    }
}
