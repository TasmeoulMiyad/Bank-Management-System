public class SavingsAccount extends Account {
    private double minimumBalance;

    public SavingsAccount(String accountNumber, Customer customer, double initialBalance, double minimumBalance) {
        super(accountNumber, customer, initialBalance);
        this.minimumBalance = minimumBalance;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Withdrawal amount must be greater than zero.");
        }
        if (getBalance() - amount >= minimumBalance) {
            deductBalance(amount);
            addTransaction("Withdrawal", amount);
        } else {
            throw new Exception("Minimum balance constraint failed! Must retain at least $" + minimumBalance);
        }
    }
}
