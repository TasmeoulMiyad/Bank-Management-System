import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank {
    private ArrayList<Account> accounts;
    private final String FILE_NAME = "accounts.txt";

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
    }

    public Account findAccount(String accNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equalsIgnoreCase(accNumber.trim())) {
                return acc;
            }
        }
        return null;
    }

    public ArrayList<Account> searchByName(String name) {
        ArrayList<Account> matched = new ArrayList<>();
        for (Account acc : accounts) {
            if (acc.getCustomer().getName().toLowerCase().contains(name.toLowerCase().trim())) {
                matched.add(acc);
            }
        }
        return matched;
    }

    public void transfer(String fromAccNum, String toAccNum, double amount) throws Exception {
        Account from = findAccount(fromAccNum);
        Account to = findAccount(toAccNum);

        if (from == null || to == null) {
            throw new Exception("One or both account numbers are invalid.");
        }

        from.withdraw(amount);
        to.deposit(amount);

        from.addTransaction("Transfer to " + toAccNum, amount);
        to.addTransaction("Transfer from " + fromAccNum, amount);
    }

    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }

    public void saveToFile() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            for (Account acc : accounts) {
                Customer c = acc.getCustomer();
                String type = (acc instanceof SavingsAccount) ? "SAVINGS" : "CURRENT";
                double extra = (acc instanceof SavingsAccount)
                        ? ((SavingsAccount) acc).getMinimumBalance()
                        : ((CurrentAccount) acc).getOverdraftLimit();

                writer.println(type + "," + acc.getAccountNumber() + "," + acc.getBalance() + ","
                        + c.getCustomerId() + "," + c.getName() + "," + c.getPhone() + ","
                        + c.getEmail() + "," + extra);
            }
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        accounts.clear();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 8) continue;

                String type = parts[0];
                String accNum = parts[1];
                double balance = Double.parseDouble(parts[2]);
                Customer cust = new Customer(parts[3], parts[4], parts[5], parts[6]);
                double extra = Double.parseDouble(parts[7]);

                if (type.equals("SAVINGS")) {
                    accounts.add(new SavingsAccount(accNum, cust, balance, extra));
                } else {
                    accounts.add(new CurrentAccount(accNum, cust, balance, extra));
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load existing records: " + e.getMessage());
        }
    }
}

